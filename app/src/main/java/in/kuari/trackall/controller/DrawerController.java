package in.kuari.trackall.controller;

import android.app.Activity;
import android.content.Intent;

import in.kuari.trackall.activities.ShowResultActivity;

/**
 * Created by sultan_mirza on 1/17/16.
 */
public class DrawerController {
    public static void track(int type, Activity activity)
    {
        Intent intent=new Intent(activity, ShowResultActivity.class);
        activity.startActivity(intent);
    }
}
