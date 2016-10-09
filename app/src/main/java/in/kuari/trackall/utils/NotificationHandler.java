package in.kuari.trackall.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Random;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.MainActivity;
import in.kuari.trackall.bean.BookMark;
import in.kuari.trackall.databases.SQLiteDBHandler;

/**
 * Created by root on 3/13/16.
 */
public class NotificationHandler extends BroadcastReceiver{

    private static final String TAG = "NotificationHandler";
    private String courierName;
    private String awb;
    private Context context;
    private FirebaseAnalytics mFirebaseAnalytics;

    public NotificationHandler() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
      //  Log.d("br","rec");

        if(checkNotificationSetting()) {
         BookMark bookMark=getBookMarksFromDB();
            if(bookMark!=null)
            triggerActivity(bookMark);
        }
        analytics("onReceive");
    }
    private void analytics(String from){

        mFirebaseAnalytics= FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(TAG,from);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

private boolean checkNotificationSetting(){
   SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
    boolean reminderNoti=  pref.getBoolean("notifications_reminder",false);
    //Toast.makeText(context,reminderNoti+"-",Toast.LENGTH_SHORT).show();
return reminderNoti;

}
    public void triggerActivity(BookMark bookMark){

        courierName=bookMark.getName();
        awb=bookMark.getTrackId();
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra("comingFrom","notification");
        PendingIntent pendingIntent=PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

            Notification.Builder notification = new Notification.Builder(context)
                    .setContentTitle("trackAll")
                    .setContentText("Track your consignment from " + courierName + " AwbNo: " + awb)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.trans)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);




            NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notification.build());
            Log.d("service", "created");


    }
   private BookMark getBookMarksFromDB(){
        SQLiteDBHandler  sqLiteDBHandler=new SQLiteDBHandler(context);
        List<BookMark>bookMarks= sqLiteDBHandler.getAllBookMarks();
       int bmSize=bookMarks.size();
       if(bmSize==0)
           return null;
        return bookMarks.get(new Random().nextInt(bmSize));

    }

}