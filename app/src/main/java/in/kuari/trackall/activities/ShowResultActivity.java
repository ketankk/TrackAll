package in.kuari.trackall.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.CourierHomeAdapter;
import in.kuari.trackall.controller.CourierController;
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
        initilizeWebview();
                screenShot= (FloatingActionButton) findViewById(R.id.res_screenshot);

        if(intent!=null) {
            int flag=  intent.getIntExtra("comingFrom",0);
            if(flag==0) {
                trackID = intent.getStringExtra("trackId");
                courierID = intent.getLongExtra("courierID", 2);
                onCourierTrack();
            }else {
                courierWebURL= intent.getStringExtra("courierWeb");
                Log.d("url",courierWebURL+"");

                onCourierDetail();

            }

        }
        Toast.makeText(this,trackID+"",Toast.LENGTH_LONG).show();
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
    void initilizeWebview(){
        webView = (WebView) findViewById(R.id.resultwebview);
        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

    }
    public void onCourierTrack(){
        ConstantValues.TRACKID=trackID;
        Log.d("TrackID",trackID);
        CourierController controller=new CourierController(this,webView);
        controller.populateView(courierID);
    }
    public void onCourierDetail(){
        initilizeWebview();
        CourierHomeAdapter adp=new CourierHomeAdapter(this,webView);
        adp.loadWebsite(courierWebURL);


    }/*private class TouchHandler implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View view, MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                params.leftMargin = Math.round(event.getX() * 160f / getBaseContext().getResources().getDisplayMetrics().densityDpi);
                params.topMargin = Math.round(event.getY() * 160f / getBaseContext().getResources().getDisplayMetrics().densityDpi);
                webView.setPivotX(event.getX());
                webView.setPivotY(event.getY());
                webView.setScaleX(2f);
                webView.setScaleY(2f);
            }
            return true;
        }
    }*/

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
        b.takeScreenShot("abc");
    }
}
