package com.xamdi.vecna;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class NeutralActivity extends AppCompatActivity {
    private ImageButton pl1,pl2,pl3;
    private ImageSlider imageSlider;
    private Button moreHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neutral);
        imageSlider = findViewById(R.id.imageslider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://images-na.ssl-images-amazon.com/images/I/71epG0m0KZL._RI_.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BMTY0NDY3MDMxN15BMl5BanBnXkFtZTcwOTM5NzMzOQ@@._V1_FMjpg_UX1000_.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://upload.wikimedia.org/wikipedia/en/0/05/Up_%282009_film%29.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://upload.wikimedia.org/wikipedia/en/a/a4/Ford_v._Ferrari_%282019_film_poster%29.png",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://upload.wikimedia.org/wikipedia/en/a/a2/C.I.D._Moosa.jpg",ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        moreHelp = findViewById(R.id.morehelp);
        moreHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://buddyhelp.org/");
            }
        });
        pl1 = findViewById(R.id.playlist1);
        pl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/playlist/5FYrhTYhYcosB5Xp4TNdJN?si=6af835c88b6946dd");
            }
        });
        pl2 = findViewById(R.id.playlist2);
        pl2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/playlist/0fDvDwm7Voy7UdD9hOV2fA?si=7dd83443235044b8");
            }
        });
        pl3 = findViewById(R.id.playlist3);
        pl3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/playlist/3Dfh3ONYa2GlXKJcuAQWcR?si=b41da2aa4a9744a1");
            }
        });
    }

    private void goToUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

    }
}