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

public class FearActivity extends AppCompatActivity {
    private ImageButton pl1,pl2,pl3;
    private ImageSlider imageSlider;
    private Button moreHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fear);
        imageSlider = findViewById(R.id.imageslider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BOTY2NzFjODctOWUzMC00MGZhLTlhNjMtM2Y2ODBiNGY1ZWRiXkEyXkFqcGdeQXVyMDM2NDM2MQ@@._V1_.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BOWM0YWMwMDQtMjE5NS00ZTIwLWE1NWEtODViMWZjMWI2OTU3XkEyXkFqcGdeQXVyMTEyMjM2NDc2._V1_FMjpg_UX1000_.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BMDNkODA5ZGQtODczOS00OTQxLThhMTItMjk0ZmNhMDM0YjNmXkEyXkFqcGdeQXVyMDM2NDM2MQ@@._V1_.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BODQ0NDhjYWItYTMxZi00NTk2LWIzNDEtOWZiYWYxZjc2MTgxXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BYzE5MjY1ZDgtMTkyNC00MTMyLThhMjAtZGI5OTE1NzFlZGJjXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_FMjpg_UX1000_.jpg",ScaleTypes.FIT));
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
                goToUrl("https://open.spotify.com/playlist/59nXDqu2HH0a29kstSDtIt?si=f5bf90476cec4d4d");
            }
        });
        pl2 = findViewById(R.id.playlist2);
        pl2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/playlist/5guQ9wepudF424LrgYwlEk?si=d2d64f6ea5a7417d");
            }
        });
        pl3 = findViewById(R.id.playlist3);
        pl3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/playlist/7smq9JFtBbwuyFc6dUB2GO?si=107200e3f4774d70");
            }
        });
    }

    private void goToUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

    }
}