package com.xamdi.vecna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import android.media.FaceDetector;
import android.os.Bundle;


import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int request_camera_code = 100;
    private Bitmap croppedbmp;
    public Classifier classifier;
    private Button btn;
    private TextView tv;
    SurfaceView surface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.CAMERA
            },request_camera_code);
        }
        surface = findViewById(R.id.surfaceView);
        btn = findViewById(R.id.button);
        tv = findViewById(R.id.tv);
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new CameraThread();
                t.start();
            }
        });
    }

    private void init() throws IOException {
        classifier = new Classifier(getAssets(), "converted_model2.tflite", "labels.txt", 224);
    }


    class SavePictureCallback implements Camera.PictureCallback {
        SavePictureCallback() {
        }

        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            Toast.makeText(MainActivity.this, "Detecting mood...", Toast.LENGTH_SHORT).show();
            int fcount;
            Bitmap facedet = convert(bmp, Bitmap.Config.RGB_565);
            fcount = setFace(facedet);
            Toast.makeText(MainActivity.this, "Face count - " + fcount, Toast.LENGTH_SHORT).show();

            //calling mood detection method
            if (fcount == 1) {
                //Toast.makeText(MainActivity.this, "Detecting mood...", Toast.LENGTH_SHORT).show();
                detect(facedet);
            }
            if (fcount > 1) {
                Toast.makeText(MainActivity.this, "MULTIPLE FACES DETECTED - RETAKE SNAP WITH SINGLE FACE", Toast.LENGTH_SHORT).show();
                tv.setText("No mood detected");
            }
            if (fcount == 0) {
                Toast.makeText(MainActivity.this, "NO FACE DETECTED - RETAKE SNAP", Toast.LENGTH_SHORT).show();
                tv.setText("No mood detected");
            }
        }

        //convert bitmap to RGB 565 for FaceDetector
        private Bitmap convert(Bitmap bitmap, Bitmap.Config config) {
            Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth() + 1, bitmap.getHeight(), config);
            Canvas canvas = new Canvas(convertedBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            return convertedBitmap;
        }

        //detection of faces
        public int setFace(Bitmap b) {
            Bitmap mFaceBitmap = b;
            FaceDetector fd;
            FaceDetector.Face[] faces = new FaceDetector.Face[4];
            int count = 0;
            int w = mFaceBitmap.getWidth();
            int h = mFaceBitmap.getHeight();
            if (w % 2 == 1) {
                w++;
                mFaceBitmap = Bitmap.createScaledBitmap(mFaceBitmap,
                        mFaceBitmap.getWidth() + 1, mFaceBitmap.getHeight(), false);
            }
            if (h % 2 == 1) {
                h++;
                mFaceBitmap = Bitmap.createScaledBitmap(mFaceBitmap,
                        mFaceBitmap.getWidth(), mFaceBitmap.getHeight() + 1, false);
            }
            int mFaceHeight = mFaceBitmap.getHeight();
            int mFaceWidth = mFaceBitmap.getWidth();
            try {
                fd = new FaceDetector(mFaceWidth, mFaceHeight, 4);
                count = fd.findFaces(mFaceBitmap, faces);
                //return count;
            } catch (Exception e) {
                Log.e("FaceDetection - ", "setFace(): " + e.toString());
                //return count;
            }

            //get cropped face
            PointF midpoint = new PointF();
            int[] fpx = null;
            int[] fpy = null;
            int i = 0, width = 0, height = 0;
            int myEyesDistance = 0;
            if (count > 0) {
                fpx = new int[count];
                fpy = new int[count];

                for (i = 0; i < count; i++) {
                    try {
                        faces[i].getMidPoint(midpoint);
                        fpx[i] = (int) midpoint.x;
                        fpy[i] = (int) midpoint.y;
                        myEyesDistance = (int) faces[i].eyesDistance();
                    } catch (Exception e) {
                        Log.e("Cropped image - ", "setFace(): face " + i + ": " + e.toString());
                    }
                }

                int x = fpx[0] - myEyesDistance * 2;
                int y = fpy[0] - myEyesDistance * 2;
                int x2 = fpx[0] + myEyesDistance * 2;
                int y2 = fpy[0] + myEyesDistance * 2;
                width = x2 - x;
                height = y2 - y;

                System.out.println("###############################################################################");
                System.out.println("midpt.x = " + fpx[0] + " midpt.y = " + fpy[0] + " eyedis = " + myEyesDistance);
                System.out.println("x, y = " + x + " , " + y + " x2, y2 = " + x2 + " , " + y2);
                System.out.println("crop width, height = " + width + " , " + height);
                System.out.println("bitmap w, h = " + b.getWidth() + " , " + b.getHeight());
                if ((width + x) > b.getWidth()) {
                    x = fpx[0] - myEyesDistance;
                    x2 = fpx[0] + myEyesDistance;
                    width = x2 - x;
                    System.out.println("New x = " + x + " x2 = " + " width = " + width);
                }
                if ((height + y) > b.getHeight()) {
                    y = fpy[0] - myEyesDistance;
                    y2 = fpy[0] + myEyesDistance;
                    height = y2 - y;
                    System.out.println("New y = " + y + " y2 = " + y2 + " height = " + height);
                }

                if (x < 0 || y < 0 || x2 < 0 || y2 < 0) {
                    Toast.makeText(MainActivity.this, "PLEASE RETAKE SNAP AND POSITION YOURSELF A BIT FAR FROM CAMERA", Toast.LENGTH_LONG).show();
                } else {
                    Bitmap croppedbmp = Bitmap.createBitmap(b, x, y, width, height);
                    Bitmap done = Bitmap.createScaledBitmap(croppedbmp, croppedbmp.getWidth(), croppedbmp.getHeight(), false);
                }
            }
            return count;

        }

        private void detect(Bitmap b) {
            Log.e("CAMERA", "reached here");
            String result;
            result = classifier.recognizemood(b);

            tv.setText(result);
        }

    }


    class CameraThread extends Thread {
        public CameraThread() {
        }

        @Override
        public void run() {
            super.run();
            try {

                Camera c = Camera.open(1);
                Camera.Parameters params = c.getParameters();
                if (params.getSupportedFocusModes().contains(
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                c.setParameters(params);
                c.setPreviewDisplay(surface.getHolder());
                c.startPreview();
                c.takePicture(
                        null,
                        null,
                        new SavePictureCallback()
                );

                c.startPreview();
                c.stopPreview();
                c.release();

            } catch (Exception e) {
                // TODO Do something about this exception.
                Log.e("CAMERA", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}