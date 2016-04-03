package in.kuari.trackall.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

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

    private String courierName;
    private String awb;
    private Context context;

    public NotificationHandler() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
      //  Log.d("br","rec");
        triggerActivity(getBookMarksFromDB());
    }



    public void triggerActivity(BookMark bookMark){

        courierName=bookMark.getName();
        awb=bookMark.getTrackId();
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra("comingFrom","notification");
        PendingIntent pendingIntent=PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        if(Build.VERSION.SDK_INT>15) {
            Notification.Builder notification = new Notification.Builder(context)
                    .setContentTitle("trackAll")
                    .setContentText("Track your consignment from " + courierName + " AwbNo: " + awb)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notification.setColor(context.getResources().getColor(android.R.color.white));

            }

            NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notification.build());
            Log.d("service", "created");

        }
    }
   private BookMark getBookMarksFromDB(){
        SQLiteDBHandler  sqLiteDBHandler=new SQLiteDBHandler(context);
        List<BookMark>bookMarks= sqLiteDBHandler.getAllBookMarks();
        return bookMarks.get(new Random().nextInt(bookMarks.size()));

    }

}