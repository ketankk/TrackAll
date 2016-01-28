package in.kuari.trackall.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by root on 1/26/16.
 */
public class CourierHomeAdapter {
    private Context context;
    private WebView webView;

    private static ProgressDialog dialog;

    public CourierHomeAdapter(final Context context, WebView webView){
        this.context=context;
        this.webView=webView;

    }
    public  void loadWebsite(String courierWebURL){
        webView.loadUrl(courierWebURL);
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
                    dialog = ProgressDialog.show(context, "", "Hang on buddy..retrieving", true, true,
                            null);
                // webView.loadUrl("javascript:(function(){document.getElementById('leftPanel').style.display='none';}())");
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (webViewPreviousState == PAGE_STARTED) {

                    dialog.dismiss();
                    dialog = null;

                    webView.setVisibility(View.VISIBLE);



                }

            }
        });



    }
}
