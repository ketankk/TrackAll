package in.kuari.trackall.couriers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.kuari.trackall.interfaces.CourierDao;
import in.kuari.trackall.utils.ConstantValues;

/**
 * Created by root on 1/25/16.
 */
public class AkrExpress implements CourierDao{
    private static final String COURIER_NAME="Akr Express";
    ;
    private String trackId;
    String url1="http://www.akrexpress.com/tracklr.php";
    private Context context;
    private WebView webView;
    private ProgressDialog dialog;
    private int COUNT;

    public AkrExpress(WebView webView, Context context) {
        this.webView = webView;
        this.context=context;
    }
    @Override
    public void hideShowContent() {

    }

    @Override
    public void load() {
        trackId=ConstantValues.TRACKID;
        webView.loadUrl(url1+ trackId);

        webView.setWebViewClient(new WebViewClient() {
            private int webViewPreviousState;
            private final int PAGE_STARTED = 0x1;
            private final int PAGE_REDIRECTED = 0x2;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                webViewPreviousState = PAGE_REDIRECTED;
                webView.loadUrl(urlNewString);
                Log.d("urlnew",urlNewString);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                COUNT++;
                webViewPreviousState = PAGE_STARTED;
                if (dialog == null || !dialog.isShowing())
                    dialog = ProgressDialog.show(context, "", "Hang on buddy..retrieving\n"+COURIER_NAME+"-"+trackId, true, true,
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
        webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].txt_lrno.value='"+trackId +"'");

        webView.loadUrl("javascript:(function(){return checkLRNo();})()");

    }
}
