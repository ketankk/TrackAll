package in.kuari.trackall.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import in.kuari.trackall.utils.ReadData;

/**
 * Created by root on 2/14/16.
 */
public class EcController {
    private WebView webView;
    private Context context;

    private String URL;
    private String email;
    private String Email;
    private String trackId;

    int COUNT;
    private String ECommerceName;
    private ProgressDialog dialog;

    public EcController(WebView webView, Context context){
        this.webView=webView;
        this.context=context;
    }
    public void PopulateView(long ECId,String orderEmail,String orderID,ProgressDialog progressDialog){
        dialog=progressDialog;
       // Toast.makeText(context,ECId+"ju"+orderEmail+orderID,Toast.LENGTH_SHORT).show();
        trackId=orderID;
        email=orderEmail;

        int id=(int)(ECId);
        initializeURL(id);
        webView.loadUrl(URL);
ProgressDialog(id);
    }
    void ProgressDialog(final int i){
        webView.setWebViewClient(new WebViewClient() {
            private int webViewPreviousState;
            private final int PAGE_STARTED = 0x1;
            private final int PAGE_REDIRECTED = 0x2;
            String prevURl = URL;

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
                    dialog = ProgressDialog.show(context, "", "Hang on buddy..retrieving\n" + ECommerceName + "-" + trackId.toUpperCase(), true, true,
                            null);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (webViewPreviousState == PAGE_STARTED) {
                    if (COUNT == 1) {//stop page from reloading same page
                        fillForm(i);
                    }
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

private void fillForm(int i)
    {
        Log.d("ID"+i,email+" "+trackId);
        switch (i){
           // 1,Amazon
            case 1:
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_txtAwb').value='"+trackId +"'");
                webView.loadUrl("javascript:var x=document.getElementById('order-id').value='"+email +"'");

                webView.loadUrl("javascript:(function(){document.getElementById('Tracking1_btnTrack').click();})()");
               break;
            //2,Flipkart
            case 2:webView.loadUrl("javascript:var x=document.getElementById('trackingId').value='"+trackId +"'");
                webView.loadUrl("javascript:var x=document.getElementById('emailId').value='"+email +"'");

                webView.loadUrl("javascript:(function(){document.getElementsByClassName('button-secondary')[0].click();})()");
            break;
            //3,Shopclues
            case 3:
                webView.loadUrl("javascript:var x=document.getElementById('order-id').value='"+trackId +"'");
                webView.loadUrl("javascript:var x=document.getElementById('user-id').value='"+email +"'");

                webView.loadUrl("javascript:(function(){document.getElementsByName('submit')[0].click();})()");
                break;
            //4,Snapdeal
            case 4:
                webView.loadUrl("javascript:var x=document.getElementById('orderId').value='"+trackId +"'");
                webView.loadUrl("javascript:var x=document.getElementById('email').value='"+email +"'");

                webView.loadUrl("javascript:(function(){document.getElementsByClassName('trackButton')[0].click();})()");
                break;
            //5,Infibeam,
            case 5:
                webView.loadUrl("javascript:var x=document.getElementById('email').value='"+email +"'");
                webView.loadUrl("javascript:var x=document.getElementById('purchaseId').value='"+trackId +"'");

                webView.loadUrl("javascript:(function(){document.getElementById('trackSubmit').click();})()");
                break;
        }
}
    private void initializeURL(int id){
    ReadData data=new ReadData(context);
    URL=data.getECommerceByID(id).getURL();
        ECommerceName=data.getECommerceByID(id).getName();

}

    private void flipkart(){
        LinearLayout layout=new LinearLayout(context);
        final EditText emailInput=new EditText(context);
        final EditText ekartTrackingID=new EditText(context);
    layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(emailInput);
        layout.addView(ekartTrackingID);
        emailInput.setHint("Enter Email id used for order");
        ekartTrackingID.setHint("Enter Tracking id");

        new AlertDialog.Builder(context)
                .setMessage("Enter your email id for this order id")
                .setView(layout)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Email=emailInput.getText().toString();
                        trackId=ekartTrackingID.getText().toString();
                        if(Email.trim().length()>0&&trackId.trim().length()>0) {
                            webView.loadUrl(URL);
                            webView.loadUrl("javascript:var x=document.getElementById('trackingId').value=" + trackId);
                            webView.loadUrl("javascript:var x=document.getElementById('emailId')[0].value=" + Email);
                            webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].submit();");
                        }else{
                            emailInput.setError("Enter Email");
                            ekartTrackingID.setError("Enter TrackId");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                })
                .show();
    }

}
