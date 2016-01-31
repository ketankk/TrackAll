package in.kuari.trackall.couriers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.kuari.trackall.interfaces.CourierDao;
import in.kuari.trackall.utils.ConstantValues;

/**
 * Created by root on 1/29/16.
 */
public class SafeExpress implements CourierDao{
    private static final String COURIER_NAME="Safe-Express";
    private String trackId;

    private Context context;
    private WebView webView;
    String url1 = "http://www.safexpress.com/faces/TrackShipment.jspx";
    private ProgressDialog dialog;
    private int COUNT;

    public SafeExpress(WebView webView,Context context) {
        this.webView = webView;
        this.context=context;
        trackId= ConstantValues.TRACKID;
    }
    @Override
    public void hideShowContent() {

    }

    @Override
    public void load() {

        webView.loadUrl(url1);

        webView.setWebViewClient(new WebViewClient() {
            private int webViewPreviousState;
            private final int PAGE_STARTED = 0x1;
            private final int PAGE_REDIRECTED = 0x2;
            String prevURl=url1;

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
                    dialog = ProgressDialog.show(context, "", "Hang on buddy..retrieving\n"+COURIER_NAME+"-"+trackId.toUpperCase(), true, true,
                            null);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (webViewPreviousState == PAGE_STARTED) {
                    if (COUNT == 1) {//stop page from reloading same page
                        fillForm();
                    }
                    if(dialog!=null)
                     dialog.dismiss();
                    dialog = null;

                    hideShowContent();

                    webView.setVisibility(View.VISIBLE);


                }

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webView.loadUrl("file:///assests/error.html");
            }
        });
            }




    @Override
    public void fillForm() {
        webView.loadUrl("javascript:var x=document.getElementById('pt1:ptsts:it1::content').value='"+trackId +"'");
        webView.loadUrl("javascript:(function(){document.getElementById('pt1:ptsts:cbts1').click();})()");

    }
}
