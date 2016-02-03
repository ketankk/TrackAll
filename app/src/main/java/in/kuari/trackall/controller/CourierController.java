package in.kuari.trackall.controller;

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

import in.kuari.trackall.bean.CourierBean;
import in.kuari.trackall.utils.ConstantValues;
import in.kuari.trackall.utils.ReadData;

/**
 * Created by root on 1/26/16.
 */
public class CourierController{

    private static  String COURIER_NAME;
    private String trackId;

    private Context context;
    private WebView webView;
    private int id;
    private ProgressDialog dialog;
    private int COUNT;
    private String URL;

    public CourierController(WebView webView, Context context) {
        this.webView = webView;
        this.context=context;
        trackId= ConstantValues.TRACKID;
    }



    public void PopulateView(long id) {
        final int i = (int) id;
        initializeURL(i);
//        Toast.makeText(context, URL + "-" + i + "--" + id + COURIER_NAME, Toast.LENGTH_LONG).show();
        if (i == 2 || i == 3 || i == 6|| i == 9 || i == 12 || i == 13 || i == 23) {
            fillForm(i);
        } else {
            webView.loadUrl(URL);

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
                        dialog = ProgressDialog.show(context, "", "Hang on buddy..retrieving\n" + COURIER_NAME + "-" + trackId.toUpperCase(), true, true,
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
    }


    public void fillForm(int id){
        //Toast.makeText(context,"D1q"+id,Toast.LENGTH_SHORT).show();
        switch (id) {
            //DTDC
            case 1:
                webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].strCnno2.value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){document.getElementsByClassName('submit-button')[0].click();})()");
                break;
            //Bombino
            case 2:
                webView.loadUrl("http://www.bombinoexp.com/Tracking.aspx?txtAwb="+trackId +"'");
ProgressDialog();                break;
            //AFL
            case 3:
                webView.loadUrl("https://www.fedex.com/apps/fedextrack/?tracknumbers="+trackId +"'");
                ProgressDialog();
                 break;
            //Air Star Xpress
            case 4:
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_DOCREFNO').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:(function(){document.getElementById('ctl00_ctl00_MainContent_InnerMainContent_btnGo').click();})()");
                break;
            //Air State
            case 5:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_txtAWB').value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){document.getElementById('ctl00_btnTrack').click();})()");
                break;
            //Airborne Intl
            case 6:
                webView.loadUrl("http://www.airborneinternational.in/tracking.aspx?txtawbno="+trackId +"'");
                ProgressDialog();
               break;
            //AirWings-nw
            case 7:
                webView.loadUrl("javascript:var x=document.getElementById('ShipmentNumber').value='"+trackId +"'");
                //
                webView.loadUrl("javascript:(function(){document.getElementById('ctl00_ctl00_MainContent_InnerMainContent_btnGo').click();})()");
                break;
            //AJ Express
            case 8:
                webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].txtTrackID.value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){document.getElementsByTagName('form')[0].btnTrack.click();})()");
                break;
            //Akash Ganga
            case 9:
                webView.loadUrl("http://www.agconline.in/Doc_Tracking.aspx?No="+trackId +"'");
                ProgressDialog();
                break;
            //Akr Express
            case 10:
                webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].txt_lrno.value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){return checkLRNo();})()");
                break;
            //Alleppey Parcel
            case 11:
                webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].wbrefno.value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){return trackingTrack();})()");
                break;
            //ANL
            case 12:
                webView.loadUrl("http://anlexpress.in?id="+trackId +"'");
                break;
            //Antron Express
            case 13:
                webView.loadUrl("http://www.antronexpress.com/main/TrackingResult.aspx?AWBNos="+trackId +"'");
                ProgressDialog();
                break;
            //Safe Express
            case 14:
                webView.loadUrl("javascript:var x=document.getElementById('pt1:ptsts:it1::content').value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){document.getElementById('pt1:ptsts:cbts1').click();})()");
                break;
           // Indian Post
            case 15:
                webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].Txt_ArticleTrack.value='"+trackId +"'");
                break;

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
                ProgressDialog();
                break;
            //Bibha courier-w
            case 24:
                webView.loadUrl("http://www.bibhacourier.com/trackingDetails.php?textfield="+trackId+"&textfield1="+trackId +"'");
                ProgressDialog();
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
        CourierBean courier=data.getCourierByID(id);
        URL= courier.getCourierTrackLink();
        COURIER_NAME=courier.getCourierName();

    }
void ProgressDialog(){
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
                dialog = ProgressDialog.show(context, "", "Hang on buddy..retrieving\n" + COURIER_NAME + "-" + trackId.toUpperCase(), true, true,
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


}
