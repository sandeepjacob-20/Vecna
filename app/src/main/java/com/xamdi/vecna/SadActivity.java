package com.xamdi.vecna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class SadActivity extends AppCompatActivity {
    private ImageButton pl1,pl2,pl3;
    private ImageSlider imageSlider;
    private Button moreHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sad);
        imageSlider = findViewById(R.id.imageslider);

        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BNTVkMTFiZWItOTFkOC00YTc3LWFhYzQtZTg3NzAxZjJlNTAyXkEyXkFqcGdeQXVyODE5NzE3OTE@._V1_.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://upload.wikimedia.org/wikipedia/en/6/67/Forrest_Gump_poster.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BNjQ0ODlhMWUtNmUwMS00YjExLWI4MjQtNjVmMmE2Y2E0MGRmXkEyXkFqcGdeQXVyNDk3NzU2MTQ@._V1_FMjpg_UX1000_.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://upload.wikimedia.org/wikipedia/en/8/81/Poster-pursuithappyness.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BYmJmM2Q4NmMtYThmNC00ZjRlLWEyZmItZTIwOTBlZDQ3NTQ1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg",ScaleTypes.FIT));
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
                goToUrl("https://open.spotify.com/playlist/7xTHDEJJpWHQijz8wyGQ8E?si=e7fa81a0e35e41e4");
            }
        });
        pl2 = findViewById(R.id.playlist2);
        pl2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/playlist/00VeBjcwbdFO6a2i4dny6D?si=6e3670327ac948b0");
            }
        });
        pl3 = findViewById(R.id.playlist3);
        pl3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/playlist/7LSNikW6NfEhtziqfnoAHD?si=732d6423aac442b9");
            }
        });
    }
    private void goToUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

    }
}