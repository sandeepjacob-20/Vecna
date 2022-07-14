package com.xamdi.vecna;

import android.graphics.Bitmap;
import android.util.Log;

public final class Helper {
    Bitmap cropped_img;
    int count;

    public Helper(Bitmap cropped_img, int count) {
        this.cropped_img = cropped_img;
        this.count = count;
        Log.e("count in class", String.valueOf(count));
    }
}
