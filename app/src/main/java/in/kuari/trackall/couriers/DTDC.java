package in.kuari.trackall.couriers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.kuari.trackall.utils.ConstantValues;

/**
 * Created by sultan_mirza on 1/17/16.
 */
public class DTDC {
    private Context context;
    private WebView webView;
    String url1 = "http://dtdc.in/tracking/tracking_results.asp";
    String url11 = "http://dtdc.in";
    private ProgressDialog dialog;
    private int COUNT;

    public DTDC(WebView webView,Context context) {
        this.webView = webView;
        this.context=context;
    }

    WebView hideShowContent() {

       /* webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].strCnno2.value='v27213156'");

        webView.loadUrl("javascript:(function(){document.getElementsByClassName('submit-button')[0].click();})()");
*/

        webView.loadUrl("javascript:(function(){document.getElementById('leftPanel').style.display='none';}())");
        webView.loadUrl("javascript:(function(){document.getElementById('topNavigation').style.display='none';}())");
        webView.loadUrl("javascript:(function(){document.getElementById('footer').style.display='none';}())");
        webView.loadUrl("javascript:(function(){document.getElementById('wraper').style.width='100%';}())");

        webView.loadUrl("javascript:(function(){document.getElementsByClassName('rightPanel_inpage')[0].style.width='100%';}())");

        return webView;
    }


   public void load() {
       Log.d("load","kkk");

       webView.loadUrl(url1);

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


    private void fillForm(){
        webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].strCnno2.value='"+ ConstantValues.TRACKID+"'");

        webView.loadUrl("javascript:(function(){document.getElementsByClassName('submit-button')[0].click();})()");

    }
}