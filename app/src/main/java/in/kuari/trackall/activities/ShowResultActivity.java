package in.kuari.trackall.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Date;
import java.util.jar.Manifest;

import in.kuari.trackall.R;
import in.kuari.trackall.controller.CourierController;
import in.kuari.trackall.controller.EcController;
import in.kuari.trackall.controller.FlightController;
import in.kuari.trackall.utils.AppController;
import in.kuari.trackall.utils.ConstantValues;
import in.kuari.trackall.utils.FunctionTools;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ShowResultActivity extends AppCompatActivity {

    private WebView webView;
    private String trackID;
    private String courierWebURL;

    private long EcId;
    private String orderID;
    private String orderEmail;
    private long courierID;
    private Activity activity;
    private String FLIGHT_NAME;
    private String FLIGHT_URL;
    private String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUST_WRITE_EXT_PERMISSION_CODE = 117;
    private static final String TAG = "ShowResultActivity";


    private FloatingActionButton screenShot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewresult);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Google Analytics starts
       analytics();
        //Google Analytics end

        Intent intent = getIntent();
        //Enable JS
        initilizeWebview();
        screenShot = (FloatingActionButton) findViewById(R.id.res_screenshot);

        chooseIntent(intent);
        // Toast.makeText(this,trackID+"",Toast.LENGTH_LONG).show();
        // setContentView(R.layout.webviewresult);

    }
private void chooseIntent(Intent intent){
    if (intent != null) {
        int flag = intent.getIntExtra("comingFrom", 0);
         //Toast.makeText(this, "c"+flag, Toast.LENGTH_SHORT).show();

        if (flag == 1) {
            //coming from courier fragmanet

            trackID = intent.getStringExtra("trackId");
            courierID = intent.getLongExtra("courierID", 2);
            onCourierTrack();
        } else if (flag == 3) {
            //coming from Ecoomerce fragmanet
            EcId = intent.getLongExtra("courierID", 0);
            String z=intent.getStringExtra("trackId");
            Log.d("flg3",EcId+z);
            if(z!=null) {
                orderID = z.split("\\|")[0];
                orderEmail = z.split("\\|")[1];
            }
            onECTrack();

        } else if (flag == 2) {
            //coming from flights fragmanet

            FLIGHT_URL = intent.getStringExtra("webURL");
            FLIGHT_NAME = intent.getStringExtra("flightName");
            loadFlightWeb();

        } else {
            courierWebURL = intent.getStringExtra("courierWeb");

        }

    }
}

    Tracker mTracker;
    private void analytics(){
        AppController appController= (AppController) getApplication();
        mTracker=appController.getDefaultTracker();
    }
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initilizeWebview() {
        webView = (WebView) findViewById(R.id.resultwebview);
        // webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

    }

    private void onCourierTrack() {
        ConstantValues.TRACKID = trackID;
          Log.d("TrackID", trackID + "vv" + courierID);
        CourierController controller = new CourierController(webView, this);
        controller.PopulateView(courierID);
    }
private void loadFlightWeb(){
    FlightController controller=new FlightController(webView,activity);
controller.ProgressDialog(FLIGHT_NAME,FLIGHT_URL);
}
    private void onECTrack() {
        EcController controller = new EcController(webView, activity);
        controller.PopulateView(EcId,orderEmail,orderID);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() > MotionEvent.ACTION_POINTER_1_DOWN) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setDisplayZoomControls(false);
        }
        return super.onTouchEvent(event);
    }

    public void takeScreenShot(View view) {

        if (checkForPermission()) {
            FunctionTools b = new FunctionTools(activity);
            Date date = new Date();
            long time = date.getTime();
            b.takeScreenShot(courierID + "-" + trackID + "-" + time);
        } else
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
    }

    private boolean checkForPermission() {

        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, REQUST_WRITE_EXT_PERMISSION_CODE);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUST_WRITE_EXT_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    break;
                }


        }
    }
}
