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
import android.widget.Toast;

import in.kuari.trackall.interfaces.CourierDao;
import in.kuari.trackall.utils.ConstantValues;
import in.kuari.trackall.utils.ReadData;

/**
 * Created by root on 1/26/16.
 */
public class Default implements CourierDao{

    private static final String COURIER_NAME="DTDC";
    private String trackId;

    private Context context;
    private WebView webView;
    private int id;
    private ProgressDialog dialog;
    private int COUNT;
    private String URL;

    public Default(WebView webView, Context context,int id) {
        this.webView = webView;
        this.context=context;
        trackId= ConstantValues.TRACKID;
        this.id=id;
    }
    @Override
    public void hideShowContent() {

    }


    public void load() {
initializeURL(id);
        webView.loadUrl(URL);

        webView.setWebViewClient(new WebViewClient() {
            private int webViewPreviousState;
            private final int PAGE_STARTED = 0x1;
            private final int PAGE_REDIRECTED = 0x2;
            String prevURl=URL;

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
    public void fillForm(){
        Toast.makeText(context,"Def"+id,Toast.LENGTH_SHORT).show();
Log.d("Def",id+"def");
        switch (id) {
            //Aramex-
            case 16:
                webView.loadUrl("javascript:var x=document.getElementById('ShipmentNumber').value='"+trackId +"'");
                //
                webView.loadUrl("javascript:(function(){document.getElementById('ctl00_ctl00_MainContent_InnerMainContent_btnGo').click();})()");
                break;
           //Ashok Airways-
            case 17:
                webView.loadUrl("javascript:var x=document.getElementById('con_no').value='"+trackId +"'");
               //chk
                webView.loadUrl("javascript:(function(){document.getElementById('Button1').click();})()");
                break;
            //Associated Road-
            case 18:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_txtCNSMCNNO').value='"+trackId +"'");
                //
                webView.loadUrl("javascript:(function(){document.getElementsByName('ctl00$ctl00$MainContent$InnerMainContent$btnGo').click();SubmitForm1();})()");
                break;
            //Atlantic-w
            case 19:
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_txtAwb').value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){document.getElementById('Tracking1_btnTrack').click();})()");
                break;
           //ATS-w
            case 20:
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_txtAwb').value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){document.getElementById('Tracking1_btnTrack').click();})()");
                break;
            //AXL
            case 21:
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_txtAwb').value='"+trackId +"'");
               //nw
                webView.loadUrl("javascript:(function(){document.getElementById('Tracking1_btnTrack'.click();})()");
                break;
            //Beacon-working
            case 22:
                webView.loadUrl("javascript:var x=document.getElementById('form1').elements['REPORT'].value='awbno'");
                webView.loadUrl("javascript:var x=document.getElementById('form1').elements['AWBNO'].value='"+trackId +"'");
                webView.loadUrl("javascript:var x=document.getElementById('form1').elements['Submit'].click();");
                break;
            //Bhavna Roadways-w
            case 23:
                webView.loadUrl("http://bhavna.cloudapp.net/Bhavna_Live/GUI/Tracking_New/Website/TrackConsignment.Aspx?CONSIGNMENT="+trackId +"'");
                break;
            //Bibha courier-w
            case 24:
                webView.loadUrl("http://www.bibhacourier.com/trackingDetails.php?textfield="+trackId+"&textfield1="+trackId +"'");
                break;
            //Blazeflash-w
            case 25:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_txtAwbNoDom').value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){document.getElementById('ctl00_ContentPlaceHolder1_btnTrackDom').click();})()");
                break;
            //Bluedart-
            case 26:
                webView.loadUrl("javascript:var x=document.getElementsByName('numbers')[0].value='"+trackId +"'");
              //chk
                webView.loadUrl("javascript:(function(){fnSubmitQuery();})()");
                break;
            //BNL
            case 27:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
               //chk
                webView.loadUrl("javascript:var x=document.getElementByName('imageField')[0].click();");
                break;
            //BOM-GIM-w
            case 28:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){document.getElementById('Submit').click();})()");
                break;
            //DHL-nw
            case 29:
                webView.loadUrl("javascript:var x=document.getElementById('AWB').value='"+trackId +"'");
                webView.loadUrl("javascript:var x=document.getElementsByName('AWB')[0].value='"+trackId +"'");

                //  webView.loadUrl("javascript:var x=document.getElementById('trackingIndex').click();");
            //Bengal force-
            case 30:
                webView.loadUrl("javascript:var x=document.getElementById('textinput').value='"+trackId +"'");
              //chk
                 webView.loadUrl("javascript:var x=document.getElementByTagName('form').elements['submit'].click();");

            default:
                webView.loadUrl("javascript:var x=document.getElementById('AWB').value='"+trackId +"'");
               // webView.loadUrl("javascript:(function(){document.getElementsByClassName('tracking-button')[0].click();})()");
                break;


        }

    }
    void initializeURL(int id){
        ReadData data=new ReadData(context);
        URL= data.getCourierByID(id).getCourierTrackLink();

    }

}
