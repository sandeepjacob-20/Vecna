package com.xamdi.vecna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class HappyActivity extends AppCompatActivity {
    private ImageButton btn;
    private ImageSlider imageSlider;
    private Button moreHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy);
        imageSlider = findViewById(R.id.imageslider);

        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://upload.wikimedia.org/wikipedia/en/5/59/The_Gray_Man_poster.png", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BNDIzNDU0YzEtYzE5Ni00ZjlkLTk5ZjgtNjM3NWE4YzA3Nzk3XkEyXkFqcGdeQXVyMjUzOTY1NTc@._V1_.jpg",ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        moreHelp = findViewById(R.id.morehelp);
        moreHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://buddyhelp.org/");
            }
        });
        btn = findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/track/60nZcImufyMA1MKQY3dcCH?si=30799f0e20ef4b8d");
            }
        });
    }

    private void goToUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

    }
}