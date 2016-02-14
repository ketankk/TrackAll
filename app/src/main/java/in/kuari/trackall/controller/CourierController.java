package in.kuari.trackall.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import in.kuari.trackall.activities.MainActivity;
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
        trackId= ConstantValues.TRACKID.toUpperCase();
    }



    public void PopulateView(long id) {
        final int i = (int) id;
        initializeURL(i);
//        Toast.makeText(context, URL + "-" + i + "--" + id + COURIER_NAME, Toast.LENGTH_LONG).show();
        //Couriers which directly gives result from URL+trackID,like posting on php page
        if (i == 2 || i == 3 || i == 6|| i == 9 || i == 12 || i == 13 || i == 23|| i == 31|| i == 32
                ||i==33|| i == 35|| i == 42|| i == 43|| i == 47|| i == 51|| i == 56|| i == 58|| i == 72
                || i == 75|| i == 86|| i == 88|| i == 95) {
           // fillForm(i);
            if(i==56){
                //Vichare courier
                trackId=trackId+"&Type=1";
            }

            webView.loadUrl(URL+trackId);
            ProgressDialog();
        } else {
            //Couriers whose forms needed to be filled
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
                        dialog = ProgressDialog.show(context, COURIER_NAME + "-" + trackId.toUpperCase(), "Hang on buddy..retrieving\n", true, true,
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

                       // webView.setVisibility(View.VISIBLE);


                    }

                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    Activity activity= (Activity) context;
                    //activity.onBackPressed();
                    Intent intent=new Intent(activity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
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
            //Professional
            case 31:
                webView.loadUrl(URL+trackId);
                ProgressDialog();
                break;
           //dotzot
            case 32:
                webView.loadUrl(URL+trackId);
                ProgressDialog();
                break;

                    //Delhivery
            case 33:
                webView.loadUrl(URL+trackId);
                /*webView.loadUrl("javascript:var x=document.getElementById('search_ids').value='"+trackId +"'");
                //chk


                webView.loadUrl("javascript:var x=document.getElementsByName('ref_type')[0].checked=true;");
                webView.loadUrl("javascript:var x=document.getElementById('bulk_search_submit').click();");
*/
                break;
            //Continental
            case 34:
                webView.loadUrl("javascript:var x=document.getElementsByName('awbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrack').click();");
                break;
            //  Concorde Express

            case 35:
                webView.loadUrl("javascript:var x=document.getElementsByName('awbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrack').click();");
                break;
            //CNZ Express
            case 36:
                webView.loadUrl("javascript:var x=document.getElementById('TrackNums').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnSubmit').click();");
                break;
            //BookMypacket
            case 37:
                webView.loadUrl("javascript:var x=document.getElementById('txtTracking').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnSubmitReg').click();");
                break;
            //BSA Logistics
            case 38:
                webView.loadUrl("javascript:var x=document.getElementById('txtPodNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('imgBtnTrack').click();");
                break;
            //bulgerian post
            case 39:
                webView.loadUrl("http://www.bgpost.bg/IPSWebTracking/IPSWeb_item_events.asp?itemid="+trackId+"&Submit=Submit");
                break;
            //Canada post
            case 40:
                webView.loadUrl("javascript:var x=document.getElementById('tapByTrackSearch:trackSearch:trackNumbers').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('tapByTrackSearch:trackSearch:submit_button').click();");
                break;
            //Central Express
            case 41:
                webView.loadUrl("javascript:var x=document.getElementById('textfield').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('button').click();");
                break;
            //Ceska Posta
            case 42:
                webView.loadUrl("javascript:var x=document.getElementById('txtTracking').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnSubmitReg').click();");
                break;
            //chips tracking
            case 44:
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_txtAwb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_btnTrack').click();");
                break;
            //First flight
            case 45:
                webView.loadUrl("javascript:var x=document.getElementById('consNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('Submit')[0].click();");
                break;
            //Citipost
            case 46:
                webView.loadUrl("javascript:var x=document.getElementById('track_no').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('lpform-submit').click();");
                break;
            //CJ Korea Express
            case 47:
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_txtAwb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_btnTrack').click();");
                break;
            //Overnite
            case 48:
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_txtAwb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_btnTrack').click();");
                break;
            //ondot
            case 49:
                webView.loadUrl("javascript:var x=document.getElementById('txtAwb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('imgsubmit').click();");
                break;
                //Gati
            case 50:
                webView.loadUrl("javascript:var x=document.getElementById('docket_id').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('track').click();");
                break;
            //V-Express
            case 52:
                webView.loadUrl("javascript:var x=document.getElementById('txtDocket').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('submit')[0].click();");

              //  webView.loadUrl("javascript:function(){GetDocketData();};");
                break;

            //V-Trans
            case 53:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_cpabout_txtGCNO').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_cpabout_btnGC').click();");
                break;
//Vayuseva
            case 54:
                webView.loadUrl("javascript:var x=document.getElementById('txtAwbNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnShow').click();");
                break;
            //USPS
            case 55:
                webView.loadUrl("javascript:var x=document.getElementById('tLabels').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('trackNumFindBtnRedesign').click();");
                break;
//VRL
            case 57:
                webView.loadUrl("javascript:var x=document.getElementsById('txtlrno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('cmdolder').click();");
                break;
            //Zodiac
            case 59:
                webView.loadUrl("javascript:var x=document.getElementsByName('trackId').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByClass('btn-system')[0].click();");
                break;
//YogaYog
            case 60:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_txtawb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_btnTrack').click();");
                break;
            //Yanwen
            case 61:
                webView.loadUrl("javascript:var x=document.getElementById('InputTrackNumbers').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_btnTrack').click();");
                break;
            //Xpressbees
            case 62:
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_txtShippingIds').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_btnTrackShipment').click();");
                break;
            //Xfas
            case 63:
                webView.loadUrl("javascript:var x=document.getElementById('txtTracking').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrack').click();");
                break;
            //WowExpress
            case 64:
                webView.loadUrl("javascript:var x=document.getElementById('tracking-no').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('track-now').click();");
                break;
            //WorldNet
            case 65:
                webView.loadUrl("javascript:var x=document.getElementById('txtAWBNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnGo').click();");
                break;
            //WorldFirst
            case 66:
                webView.loadUrl("javascript:var x=document.getElementById('txtRefNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('lkbtnTrack').click();");
                break;
            //Satellite Cargo

            case 67:
                webView.loadUrl("javascript:var x=document.getElementsByName('awbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('B1').click();");
                break;
            //Saudi Post,

            case 68:
                webView.loadUrl("javascript:var x=document.getElementById('trackNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrack').click();");
                break;
            //SCM For You,Not working

            case 69:
                webView.loadUrl("javascript:var x=document.getElementById('docketNumber').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:(function(){return validateDocketNumber();})");
                break;
            //Seko Logistics,

            case 70:
                webView.loadUrl("javascript:var x=document.getElementById('OrderNumbers').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByClassName('button')[0].click();");
                break;
            //Sequel,

            case 71:
                webView.loadUrl("javascript:var x=document.getElementById('txtDktNum').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrack').click();");
                break;
            //72 Shree Anjani,

            //Shree Balaji,
            case 73:
                webView.loadUrl("javascript:var x=document.getElementById('CNO').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('submit').click();");
                break;
            //Sigma,

            case 74:
                webView.loadUrl("javascript:var x=document.getElementById('txtAwbNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrack').click();");
                break;
            //75 Shree Mahabali,
            //Shree Mahavir,

            case 76:
                webView.loadUrl("javascript:var x=document.getElementById('txtTrackCons').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrackCons').click();");
                break;
            //Shree Maruti,
            case 77:
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_txtShipmentNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_btnSearch').click();");
                break;
            //Shree Nandan,
            case 78:
                webView.loadUrl("javascript:var x=document.getElementById('content_ConsignmentTextbox').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('content_TrackButton').click();");
                break;
            //Shree Tirupati,

            case 79:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_txtDoc').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_imgbtn_Go').click();");
                break;
            //Shri Karni
            case 80:
                webView.loadUrl("javascript:var x=document.getElementById('Right1_txtTracking').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('Right1_btnTrack').click();");
                break;
            //Shri Sai
            case 81:
                webView.loadUrl("javascript:var x=document.getElementsByName('cnote')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('submit')[0].click();");
                break;

            //UBX,,,Not working
            case 82:
                webView.loadUrl("javascript:var x=document.getElementById('AirwayBillT').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrackByAWBNo').click();");
                break;
            //Singapore Post
            case 83:
                webView.loadUrl("javascript:var x=document.getElementById('track_items').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('calculate')[0].click();");
                break;
            //SJ Worldwide,
            case 84:
                webView.loadUrl("javascript:var x=document.getElementById('txt_consignmentno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btn_Show').click();");
                break;
//85,Skycom Express,
            case 85:
                webView.loadUrl("javascript:var x=document.getElementById('trackno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByClass('trackbutton')[0].click();");
                break;
//86,Skylark Express,
            case 86:
                webView.loadUrl("javascript:var x=document.getElementById('txtAWBNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrackByAWBNo').click();");
                break;
            // 87,Skynet,
            case 87:
                webView.loadUrl("javascript:var x=document.getElementById('txtAWBNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrackByAWBNo').click();");
                break;
            //88,Skynet India,
            case 88:
                webView.loadUrl("javascript:var x=document.getElementById('Textarea1').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrackByAWBNo').click();");
                break;
//SM not working
            case 89:
                webView.loadUrl("javascript:var x=document.getElementsByClass('tracktexboxinner')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:(function(){getTrackConsignmentDetails();});");
                break;
//Soham International,
            case 90:
                webView.loadUrl("javascript:var x=document.getElementById('ShipmentNumber').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrackByAWBNo').click();");
                break;
            //Speed and safe
            case 91:
                webView.loadUrl("javascript:var x=document.getElementById('trackcn').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('sub_tracking').click();");
                break;
            //Speed Express
            case 92:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtConsignmentNo')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('cmdLogin')[0].click();");
                break;
            //Speed Net Express-NOt working
            case 93:
                webView.loadUrl("javascript:var x=document.getElementsByName('q')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrackByAWBNo').click();");
                break;
            //speed man
            case 94:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno2')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('Submit')[0].click();");
                break;
            //Spoton 95
            case 95:
                webView.loadUrl("javascript:var x=document.getElementById('ShipmentNumber').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrackByAWBNo').click();");
                break;
            //SRK Communique
            case 96:
                webView.loadUrl("javascript:var x=document.getElementById('T2').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('B2')[0].click();");
                break;
            //ST Couriers
            case 97:
                webView.loadUrl("javascript:var x=document.getElementById('awb_nos').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('B1')[0].click();");
                break;
            //UC Global
            case 98:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('imageField')[0].click();");
                break;
            //Suntika
            case 99:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('Submit')[0].click();");
                break;
            //Super Trade
            case 100:
                webView.loadUrl("javascript:var x=document.getElementsByName('cono')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByClass('btn-info').click();");
                break;
           // Uncle Parcels
            case 101:
                webView.loadUrl("javascript:var x=document.getElementsByName('docketno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('submitb')[0].click();");
                break;
            //Sweden/China post-Not working
            case 102:
                webView.loadUrl("javascript:var x=document.getElementById('track_number').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrackByAWBNo').click();");
                break;
            //Swiss Freight
            case 103:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('B1')[0].click();");
                break;
            //Swiss post
            case 104:
                webView.loadUrl("javascript:var x=document.getElementById('parcelListDiv').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('launchSimpleSearch').click();");
                break;
            //UCS
            case 105:
                webView.loadUrl("javascript:var x=document.getElementById('filter_s').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('trackingsubmit').click();");
                break;
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
