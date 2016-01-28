package in.kuari.trackall.couriers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.kuari.trackall.interfaces.CourierDao;
import in.kuari.trackall.utils.ConstantValues;

/**
 * Created by root on 1/25/16.
 */
public class AkashGanga implements CourierDao{
String url1="http://www.agconline.in/Doc_Tracking.aspx?No=";
    private Context context;
    private WebView webView;
     private ProgressDialog dialog;
    private int COUNT;

    public AkashGanga(WebView webView, Context context) {
        this.webView = webView;
        this.context=context;
    }
    @Override
    public WebView hideShowContent() {
        return null;
    }

    @Override
    public void load() {
        webView.loadUrl(url1+ ConstantValues.TRACKID);

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
                COUNT++;
                webViewPreviousState = PAGE_STARTED;
                if (dialog == null || !dialog.isShowing())
                    dialog = ProgressDialog.show(context, "", "Getting information from server", true, true,
                            null);
                // webView.loadUrl("javascript:(function(){document.getElementById('leftPanel').style.display='none';}())");
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (webViewPreviousState == PAGE_STARTED) {
                    if (COUNT == 1) {//stop page from reloading same page

                        fillForm();
                    }
                    dialog.dismiss();
                    dialog = null;

                    hideShowContent();
                    webView.setVisibility(View.VISIBLE);



                }

            }
        });
    }

    @Override
    public void fillForm() {

    }
}
