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

public class AngryActivity extends AppCompatActivity {
    private ImageButton pl1,pl2,pl3;
    private ImageSlider imageSlider;
    private Button moreHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angry);
        imageSlider = findViewById(R.id.imageslider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://flxt.tmsimg.com/assets/p8051212_p_v8_ax.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://upload.wikimedia.org/wikipedia/en/thumb/f/fa/Spider-Man_Into_the_Spider-Verse_poster.png/220px-Spider-Man_Into_the_Spider-Verse_poster.png",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://m.media-amazon.com/images/M/MV5BZGFmMjM5OWMtZTRiNC00ODhlLThlYTItYTcyZDMyYmMyYjFjXkEyXkFqcGdeQXVyNDUzOTQ5MjY@._V1_FMjpg_UX1000_.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://i.ytimg.com/vi/OVTGHtHbG60/movieposter_en.jpg",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://static.wikia.nocookie.net/dreamworks/images/5/5f/B3B13E6A-B311-4264-8353-76F72E4E5D20.jpeg/revision/latest?cb=20180812164058",ScaleTypes.FIT));
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
                goToUrl("https://open.spotify.com/playlist/4f6tpmRrnm16JyZb8COpnC?si=60d36c0d53054ab9");
            }
        });
        pl2 = findViewById(R.id.playlist2);
        pl2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/playlist/3Kkyci0F5Bwf5zA8gD5ITQ?si=7c6e5b57de0a474d");
            }
        });
        pl3 = findViewById(R.id.playlist3);
        pl3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToUrl("https://open.spotify.com/playlist/3RY8bxHLCbIJPFNkgxzEAQ?si=8cdee9291e5149a5");
            }
        });
    }

    private void goToUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

    }
}