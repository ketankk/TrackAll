package in.kuari.trackall.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.analytics.FirebaseAnalytics;

import in.kuari.trackall.R;

public class ShowFlightsWeb extends AppCompatActivity {

    private String FLIGHT_NAME;
    private String ECOMM_NAME;

    private String webURl;
    private WebView webView;
    private ProgressDialog dialog;
    private Context context;
    private static final String TAG = "ShowFlightsWeb";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_flights_web);

      analytics("onCreate");

        Toolbar toolbar = (Toolbar) findViewById(R.id.flighttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;

        webView= (WebView) findViewById(R.id.flightsweb);
        initilizeWebview();
    Intent  intent=getIntent();
        if(intent!=null){
            String from =intent.getStringExtra("from");
            if(from.equals("flights")) {
                webURl = intent.getStringExtra("webURL");
                FLIGHT_NAME = intent.getStringExtra("flightName");
                webView.loadUrl(webURl);
                setWebViewFlight();

            }
            if(from.equals("ecommerce")){
                webURl = intent.getStringExtra("orderId");
                ECOMM_NAME = intent.getStringExtra("email");

                webView.loadUrl(webURl);
              //  setWebViewEcomm();


            }

        }




    }

    private void setWebViewFlight(){
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(dialog!=null)
                    dialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(dialog==null||!dialog.isShowing()){
                    dialog=ProgressDialog.show(context, FLIGHT_NAME, "Loading...", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            onBackPressed();
                        }
                    });
                }
            }
        });
    }
    private void analytics(String from){

        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(TAG,from);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog!=null)
            dialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
       analytics("onResume");
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
        // webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

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

}
