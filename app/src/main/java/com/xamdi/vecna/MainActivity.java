package com.xamdi.vecna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
        // If camera permissions is not granted, will request for permission
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

        //creates a button listener to capture image on button click
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On button click starts the camera thread
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
            //Creates a bitmap image from the camera stream
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            // Used to rotate image for better face detection
            Matrix matrix = new Matrix();
            matrix.postRotate(270);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

            Toast.makeText(MainActivity.this, "Detecting mood...", Toast.LENGTH_SHORT).show();

            //Convert Image to RGB_565 format
            Bitmap facedet = convert(rotatedBitmap, Bitmap.Config.RGB_565);

            //Determines the faces in the image and crop out the face region
            int fcount;
            Helper h = setFace(facedet);
            fcount = h.count; //Number of faces in the image
            croppedbmp = h.cropped_img; //Bitmap image of the face only
            Toast.makeText(MainActivity.this, "Face count - " + fcount, Toast.LENGTH_SHORT).show();

            //calling mood detection method
            if (fcount == 1) {
                detect(croppedbmp);
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
        public Helper setFace(Bitmap b) {
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
            //Counting the number of faces in the image
            try {
                fd = new FaceDetector(mFaceWidth, mFaceHeight, 4);
                count = fd.findFaces(mFaceBitmap, faces);
            } catch (Exception e) {
                Log.e("FaceDetection - ", "setFace(): " + e.toString());
            }
            Log.e("count in main",String.valueOf(count));


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
                    croppedbmp = Bitmap.createBitmap(b, x, y, width, height);
                }
            }
            //Returns both the cropped image and the count as objects of helper class
            Helper ret_h = new Helper(croppedbmp, count);
            return ret_h;
        }

        private void detect(Bitmap b) {
            //Sends the image to the Model to detect emotion and calls appropriate activity
            Log.e("CAMERA", "reached here");
            String result;
            Intent intent = new Intent(MainActivity.this,MainActivity.class);
            result = classifier.recognizemood(b);
            switch (result){
                case "Angry":
                    intent = new Intent(MainActivity.this, AngryActivity.class);
                    break;
                case "Happy":
                    intent = new Intent(MainActivity.this,HappyActivity.class);
                    break;


            }
            startActivity(intent);
            tv.setText(result);
        }

    }


    class CameraThread extends Thread {
        //Thread that deals with taking the picture
        public CameraThread() {
        }

        @Override
        public void run() {
            super.run();
            try {

                Camera c = Camera.open(1); // cameraID 1 is the front facing camera
                Camera.Parameters params = c.getParameters();
                //setting autofocus
                if (params.getSupportedFocusModes().contains(
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }

                //Disabling camera sound if possible
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(1, info);
                if (info.canDisableShutterSound) {
                    c.enableShutterSound(false);
                }

                //Taking the photo
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