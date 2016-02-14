package in.kuari.trackall.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.Date;

import in.kuari.trackall.R;
import in.kuari.trackall.controller.CourierController;
import in.kuari.trackall.controller.EcController;
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
    private long courierID;
    private Activity activity;
    private FloatingActionButton screenShot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.webviewresult);
activity=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        //Enable JS
        initilizeWebview();
                screenShot= (FloatingActionButton) findViewById(R.id.res_screenshot);

        if(intent!=null) {
            int flag=  intent.getIntExtra("comingFrom",0);
           // Toast.makeText(this, "c"+flag, Toast.LENGTH_SHORT).show();

            if(flag==0) {
                trackID = intent.getStringExtra("trackId");
                courierID = intent.getLongExtra("courierID", 2);
                onCourierTrack();}
            else if(flag==2){
                EcId = intent.getLongExtra("EcID",0);
                onECTrack();

            }
            else {
                courierWebURL= intent.getStringExtra("courierWeb");

            }

        }
       // Toast.makeText(this,trackID+"",Toast.LENGTH_LONG).show();
            // setContentView(R.layout.webviewresult);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initilizeWebview(){
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
    private void onCourierTrack(){
        ConstantValues.TRACKID=trackID;
      //  Log.d("TrackID",trackID+"vv"+courierID);
        CourierController controller=new CourierController(webView,this);
        controller.PopulateView(courierID);
    }
private void onECTrack(){
    EcController controller=new EcController(webView,activity);
    controller.PopulateView(EcId);
}


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()>MotionEvent.ACTION_POINTER_1_DOWN){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setDisplayZoomControls(false);
        }
        return super.onTouchEvent(event);
    }
    public void takeScreenShot(View view){
        FunctionTools b=new FunctionTools(activity);
        Date date=new Date();
        long time=date.getTime();
        b.takeScreenShot(courierID+"-"+trackID+"-"+time);
    }
}
