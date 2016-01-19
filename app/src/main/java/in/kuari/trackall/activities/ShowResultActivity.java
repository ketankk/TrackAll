package in.kuari.trackall.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import in.kuari.trackall.R;
import in.kuari.trackall.controller.CourierController;
import in.kuari.trackall.utils.ConstantValues;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ShowResultActivity extends AppCompatActivity {


    private WebView webView;
  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.resultwebview);
        webView=(WebView)findViewById(R.id.webview);
        onclick();
    }


    void initilizeWebview(){
        webView = (WebView) findViewById(R.id.webview);
        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

    }
    public void onclick(){
        initilizeWebview();
        ConstantValues.TRACKID="v27213156";
        CourierController controller=new CourierController(this,webView);
        controller.populateView(1);
    }
}
