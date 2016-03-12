package in.kuari.trackall.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by root on 1/29/16.
 */
public class Colors {
    public static int getRandomColor(){
        Random rnd=new Random();
        return Color.argb(255,rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255));

    }
}
