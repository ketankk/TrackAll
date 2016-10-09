package in.kuari.trackall.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by root on 4/12/16.
 */
public class FlightController {

    private static final String TAG = "FlightController";
    private Context context;
    private WebView webView;
    private ProgressDialog dialog;
    private String AirLineName;
    private FirebaseAnalytics mFirebaseAnalytics;

    public FlightController(WebView webView, Context context){
        this.webView=webView;
        this.context=context;
    }

    public void ProgressDialog(String name,String URL, ProgressDialog dialog1){

        analytics("ProgressDialog: name="+name+" url "+URL);
        this.dialog=dialog1;
        AirLineName=name;
        webView.loadUrl(URL);

        webView.setWebViewClient(new WebViewClient() {
            private int webViewPreviousState;
            private final int PAGE_STARTED = 0x1;
            private final int PAGE_REDIRECTED = 0x2;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                webViewPreviousState = PAGE_REDIRECTED;

                webView.loadUrl(urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webViewPreviousState = PAGE_STARTED;
                if (dialog == null || !dialog.isShowing())
                    dialog = ProgressDialog.show(context, "", "Loading..." + AirLineName , true, true,
                            null);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (webViewPreviousState == PAGE_STARTED) {

                    if (dialog != null)
                        dialog.dismiss();
                    dialog = null;

                    //hideShowContent();

                    webView.setVisibility(View.VISIBLE);


                }

            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webView.loadUrl("http://trackall.kuari.in");
            }

        });

    }
    private void analytics(String from){

        mFirebaseAnalytics= FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(TAG,from);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
