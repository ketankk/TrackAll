package in.kuari.trackall.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.PagerAdapter;
import in.kuari.trackall.databases.MYSQLHandler;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseAnalytics mFirebaseAnalytics;
    private Activity activity;
private int displayFragment=1;

    /*private void checkGooglePlayServices() {
            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
            int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
            if(status != ConnectionResult.SUCCESS) {
                if(googleApiAvailability.isUserResolvableError(status)) {
                    googleApiAvailability.getErrorDialog(activity, status, 2404).show();
                }
            }
        }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if coming from on clicking URL open directly courier fragment
        Uri data=getIntent().getData();
        if(data!=null)
            displayFragment=2;



        setContentView(R.layout.activity_main);
        //just to check if notifcation is working or not

findViewById(R.id.qwer).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        //sendNotification("mdd","pop");
    }
});
        //

        //checkGooglePlayServices();
        //Google Analytics starts
        analytics("oncreate");
        //Google Analytics end
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("fcmToken",token+"");
registerToken(token);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity = this;
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout= (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_star_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_ecart);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_cart);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_flight_white_24dp);
        CheckSharedPreferance();

}

    private void analytics(String from){

       mFirebaseAnalytics= FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(TAG,from);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    @Override
    public void onResume() {
        super.onResume();

analytics("resume");
    }



      @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the BookMarkFragment/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                openSettingsActivity();
                break;

            case R.id.action_feedback:
                sendFeedback();
                break;
            case R.id.action_share:
                ShareAppDownloadLink();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    private void openSettingsActivity(){
        startActivity(new Intent(this,SettingsActivity.class));
    }

    private void registerToken(String token){
        if(!ifFCMReg()){
            MYSQLHandler handler=new MYSQLHandler(this);
            handler.sendfcmtokentoserver(token);
        }
    }
    private boolean ifFCMReg() {
Log.d(TAG,"iffcmreg");
        SharedPreferences pref = getSharedPreferences("TRACKALL", MODE_PRIVATE);
boolean is=pref.getBoolean("FCMreg", false);
        Log.d(TAG,"iffcmreg "+is);

        return is;

    }

    void CheckSharedPreferance() {

        SharedPreferences pref = getSharedPreferences("TRACKALL", MODE_PRIVATE);
        boolean frsttime = pref.getBoolean("FirstTime", true);
        if (frsttime) {
            SharedPreferences.Editor editor = getSharedPreferences("TRACKALL", MODE_PRIVATE).edit();
            editor.putBoolean("FirstTime", false);
            editor.apply();

        }
        //Log.d("loj",frsttime+"");
    }



    private void sendFeedback() {
        String emailId="sultanmirzadev@gmail.com";


        Intent intent = new Intent(Intent.ACTION_SENDTO);

        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for trackAll");

        Uri uri=Uri.parse("mailto:"+emailId);
        intent.setData(uri);
        activity.startActivity(intent);

    }

    private void ShareAppDownloadLink() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey! Download this amazing app trackAll and keep track of everything \n http://bit.ly/1R30Vtu";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "trackAll");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }



}

