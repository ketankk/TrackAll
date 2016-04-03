package in.kuari.trackall.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;

import in.kuari.trackall.activities.MainActivity;
import in.kuari.trackall.bean.CourierBean;
import in.kuari.trackall.utils.ConstantValues;
import in.kuari.trackall.utils.ReadData;

/**
 * Created by root on 1/26/16.
 * This controller class first loads the websites than it fills th form with trcking id
 * than clicks the submit button,
 * if request is of get type than directly loading url
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
    private String resData;

    /**
     *
     * @param webView view where page is laoded
     * @param context
     */

    public CourierController(WebView webView, Context context) {
        this.webView = webView;
        this.context=context;
        trackId= ConstantValues.TRACKID.toUpperCase();
    }

    /**
     *
     * @param id id of the courier as defined in first column of courier detail
     *           it loads the url in webview
     */

    private void postData(long id){

        Log.d("post", "" + trackId);
        String data="strCnno="+trackId+"&strCnno2="+trackId+"&TrkType2=awb_no"+"&Ttype:awb_no&action:track&sec=tr&ctlActiveVal:1";
        webView.postUrl(URL, Base64.encode(data.getBytes(), Base64.DEFAULT));

        StringRequest request=new StringRequest(Request.Method.POST,"http://dtdc.in/tracking/tracking_results.asp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resData=response;
                        Log.d("res",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d("err",error.toString());
                    }
                }){

            /*@Override
            public Map<String, String> getParams() {
                Map<String,String> params=new HashMap<>();
                params.put("strCnno",trackId);
                params.put("strCnno2",trackId);
                params.put("TrkType2","awb_no");
                return params;
            }*/
        };
        //Volley.newRequestQueue(context).add(request);
    }
    public void PopulateView(long id) {
        final int i = (int) id;
       initializeURL(i);

//        Toast.makeText(context, URL + "-" + i + "--" + id + COURIER_NAME, Toast.LENGTH_LONG).show();
        //Couriers which directly gives result from URL+trackID,like posting on php page
        if (i == 2 || i == 3 || i == 6|| i == 9 || i == 12 || i == 13 || i == 23|| i == 31|| i == 32
                ||i==33|| i == 35|| i == 42|| i == 43|| i == 47|| i == 50||i == 51|| i == 56|| i == 58|| i == 72
                || i == 75|| i == 86|| i == 88|| i == 95||id==122||id==128||id==135||id==139||id==140
                ||id==141||id==146 ||id==147||id==153||id==157||id==163||id==165||id==182||id==178
                ||id==186||id==188||id==193||id==196||id==199||id==203||id==205||id==206||id==207
                ||id==208||id==209||id==213||id==218) {
           // fillForm(i);
            if(i==56){
                //Vichare courier
                trackId=trackId+"&Type=1";
            }
            if(id==139){
                trackId=trackId+"&accountGroupId=201";
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

    /**
     *
     * @param id
     * According to the id of courier,tracking id is filled in form of webpage
     */

    public void fillForm(int id){
        //Toast.makeText(context,"D1q"+id,Toast.LENGTH_SHORT).show();
        switch (id) {
            //DTDC
            case 1:
               // String postData="action=\"track\"&strCnno2=\"V27213156\"+strCnno=\"V27213156\"";
                //webView.postUrl(URL,postData.getBytes());
                webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[0].strCnno2.value='"+trackId +"'");
                webView.loadUrl("javascript:(function(){document.getElementsByClassName('submit-button')[0].click();})()");
                break;
            //2 Bombino

            //3 AFL

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
            // 6 Airborne Intl
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
            //9 Akash Ganga

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
            //23Bhavna Roadways-w

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
            //31 Professional

           //32dotzot
                // 33 Delhivery

            //34Continental
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
            //Priority Express
            case 106:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('imageField')[0].click();");
                break;
            //107,Pacific Express,
            case 107:
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_txtAwb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_btnTrack').click();");
                break;
                   // 108,Palande,
            case 108:
                webView.loadUrl("javascript:var x=document.getElementsByName('ctl00_txtTrackNumber')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('ctl00$btnTrack')[0].click();");
                break;
                   // 109,Parcel Force,
            case 109:
                webView.loadUrl("javascript:var x=document.getElementById('edit-parcel-tracking-number').value='"+trackId +"'");
                //chk
               // webView.loadUrl("javascript:var x=document.getElementsByName('imageField')[0].click();");
                break;
                   // 110,Parcel2Go,
            case 110:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('imageField')[0].click();");
                break;
                   // 111,Parcelled,
            case 111:
                webView.loadUrl("javascript:var x=document.getElementById('booking_booking_id').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('imageField')[0].click();");
                break;
                    //112,Parveen Express,
            case 112:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_Txt_WayBillNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_Btn_Search').click();");
                break;
                    //113,Patel Roadways,
            case 113:
                webView.loadUrl("javascript:var x=document.getElementById('dockno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('Submit')[0].click();");
                break;
                    //114,Pavan,
            case 114:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_Tracking1_txtAwb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_Tracking1_btnTrack').click();");
                break;
                    //115,Pick Speed,
            case 115:
                webView.loadUrl("javascript:var x=document.getElementById('txtAwbNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnSearch').click();");
                break;
                    //116,Pickme Express,
            case 116:
                webView.loadUrl("javascript:var x=document.getElementById('Consignment').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_btnTrack').click();");
                break;
                    //117,Red Express,
            case 117:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('imageField')[0].click();");
                break;
                    //118,Pigeon Express,
            case 118:
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_txtTrackNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_btnTrack').click();");
                break;
                    //119,Pionexxco,
            case 119:
                webView.loadUrl("javascript:var x=document.getElementsByName('awbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('Submit')[0].click();");
                break;
                    //120,Polish Post
            case 120:
                webView.loadUrl("javascript:var x=document.getElementById('numer').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('BSzukaj').click();");
                break;
            //121,Poonam,
            case 121:
                webView.loadUrl("javascript:var x=document.getElementsByName('ctl00$ContentHolder$txtTrack')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('ctl00$ContentHolder$btnSubmit')[0].click();");
                break;
              //      122,POS Malaysia,

                //    123,PostNL,
            case 123:
                webView.loadUrl("javascript:var x=document.getElementById('ofq').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnZoek-revised').click();");
                break;
                  //  124,PRG EXPRESS,
            case 124:
                webView.loadUrl("http://www.prgexpress.com/tracking.php?trackid='"+trackId+"&submit=Track");
                //k
               // webView.loadUrl("javascript:var x=document.getElementsByName('submit')[0].click();");
                break;
                    //125,Prime Express,
            case 125:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('imageField')[0].click();");
                break;
            // 126,Prime Track/Trackon,
            case 126:
                webView.loadUrl("javascript:var x=document.getElementById('txtConsgNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrack').click();");
                break;
               //     127,Royal mail,
            case 127:
                webView.loadUrl("javascript:var x=document.getElementById('edit-tracking-number').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('edit-tracking-submit').click();");
                break;
                 //   128,Procure,
            //129,PSS,
            case 129:
                webView.loadUrl("javascript:var x=document.getElementById('awn').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('findno').click();");
                break;
              //      130,Purolator,
            case 130:
                webView.loadUrl("javascript:var x=document.getElementById('search').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('buttonTrackSearch').click();");
                break;
          //  131,Pushpak
            case 131:
                webView.loadUrl("javascript:var x=document.getElementById('txtDoc_No').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('Track')[0].click();");
                break;
            //  132,Q Express
            case 132:
                webView.loadUrl("javascript:var x=document.getElementById('ShipmentNumber').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('button').click();");
                break;

            //Rapid connect
            case 133:
                webView.loadUrl("javascript:var x=document.getElementsByName('T2')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('B2')[0].click();");
                break;
            //Rajdhani
            case 134:
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_txtConsinmentNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_btnTrack').click();");
                break;
           // 136,Khubani,
            case 136:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_DOCREFNO').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_PbTrack').click();");
                break;
                   // 137,King World Wide,
            case 137:
                webView.loadUrl("javascript:var x=document.getElementById('txttrackno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('Button1').click();");
                break;
           // 138,Megacity,
            case 138:
                webView.loadUrl("javascript:var x=document.getElementById('Txtcn').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnSearch').click();");
                break;
                   // 139,Lalji Mulji,
                   // 140,Linex Solutions,
            case 140:
                webView.loadUrl("javascript:var x=document.getElementById('txtNumbers').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnContinue').click();");
                break;
                  //  141,Logi Buddy,
            case 141:
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_txtConsinmentNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_btnTrack').click();");
                break;
                    //142,Ludhiana Express,
            case 142:
                webView.loadUrl("javascript:var x=document.getElementById('search').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('sub')[0].click();");
                break;
                    //143,Madhur,
            case 143:
                webView.loadUrl("javascript:var x=document.getElementById('txtcnoteno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('btn_search')[0].click();");
                break;
                   // 144,Madurai Radha Transport,
            case 144:
                webView.loadUrl("javascript:var x=document.getElementById('txtlrno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.frmlrsearch.submit()");
                break;
          //  145,MailEx,
            case 145:
                webView.loadUrl("javascript:var x=document.getElementsByName('awbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('tracknow')[0].click();");
                break;
                  //  146,Maruti Air,
                   // 147,Max Pacific,
            //  148,KGS Cargo,grno
            case 148:
                webView.loadUrl("javascript:var x=document.getElementById('grno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('imageField')[0].click();");
                break;
           // 149 Mirakle
            case 149:
                webView.loadUrl("javascript:var x=document.getElementById('ewd-otp-tracking-number').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('Login_Submit')[0].click();");
                break;

            // 150 MML Logistic
            case 150:
                webView.loadUrl("javascript:var x=document.getElementById('txtAWBNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('btnTrack').click();");
                break;
            // 151 Msk worldwide
            case 151:
                webView.loadUrl("javascript:var x=document.getElementById('MainContent_txtMultiCon').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('MainContent_btnTrack').click();");
                break;
            // 152 mypacco
            case 152:
                webView.loadUrl("javascript:var x=document.getElementById('trackingNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('trackOrder').click();");
                break;
           // 153,Daakiyaa
            //154,Dakiya
            case 154:
                webView.loadUrl("javascript:var x=document.getElementsByName('consignment_no')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('Track')[0].click();");
                break;
           // 155,DCS,
            case 155:
                webView.loadUrl("javascript:var x=document.getElementById('TrackingNumber').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('btnSubmit')[0].click();");
                break;
             //       156,Delex
            case 156:
                webView.loadUrl("javascript:document.getElementsByName('DocketType')[0].checked=true");


                webView.loadUrl("javascript:var x=document.getElementById('samplees').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByName('Tracking_submit')[0].click();");
                break;
          //  157,DPD
                 //   158,Delnet Express,
            case 158:
                webView.loadUrl("javascript:var x=document.getElementById('txtAwb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('BtnSearch').click();");
                break;
                  //  159,Deus India,
            case 159:
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_text').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_Button1').click();");
                break;
                   // 160 DHL Sameday,
            case 160:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_MainContent_txtTicket').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_MainContent_ImageButton2').click();");
                break;
                   // 161Dreamco Express,
            case 161:
                webView.loadUrl("javascript:var x=document.getElementsByName('awbCodeList')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ShowCmd').click();");
                break;
            //162,E-Vahan,http:
            case 162:
                webView.loadUrl("javascript:var x=document.getElementById('inputAWB').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:test();");
                break;
            //163,Ecom Express,
            case 163:
                webView.loadUrl("javascript:var x=document.getElementsByName('awbCodeList')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('ShowCmd').click();");
                break;
                   // 164,HighFly,
            case 164:
                webView.loadUrl("javascript:var x=document.getElementById('username').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByTagName('form')[1].submit();");
                break;
                    //165,Elbex,

                   // 166,Elta,
            case 166:
                webView.loadUrl("javascript:var x=document.getElementById('voucher').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:track();");
                break;
                    //167,Emirates Post,
            case 167:
                webView.loadUrl("javascript:var x=document.getElementById('txtMailID').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('form').submit();");
                break;
                  //  168,EMS Korea,
            case 168:
                webView.loadUrl("javascript:var x=document.getElementById('POST_CODE').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('kform').submit();");
                break;
                 //   169,eParcel,
            case 169:
                webView.loadUrl("javascript:var x=document.getElementsByName('tracking_numbers')[0] .value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementsByTag('download-form')[0] .submit()");
                break;
                 //   170,Excess,
            case 170:
                webView.loadUrl("javascript:var x=document.getElementById('search_term').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('search_button').click();");
                break;
                    //171,Expan,
            case 171:
                webView.loadUrl("javascript:var x=document.getElementById('txtfldawb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:var x=document.getElementById('Button1').click();");
                break;
                  //  172,Falcon,
            case 172:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('frmtracking').submit();");
                break;
                    //173,Fardar,
            case 173:
                webView.loadUrl("javascript:var x=document.getElementById('AWBNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.form1.submit();");
                break;
                   // 174,Faspeed,
            case 174:
                webView.loadUrl("javascript:var x=document.getElementById('track_number').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:getOrderInfo();");
                break;
          ///  175,Fast & First,
            case 175:
                webView.loadUrl("javascript:var x=document.getElementById('txtTracking').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('btnTrackP').click();");
                break;
                  //  176,Good luck,
            case 176:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtConsignment')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('btn').click();");
                break;
                   // 177,First Flight canada
            case 177:
                webView.loadUrl("javascript:var x=document.getElementsByName('track')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.sik.submit();");
                break;

// 178,Flight despatch,

           // 179,Flyking,
            case 179:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_txtCNoteNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('ctl00_ContentPlaceHolder1_btnSubmit_input').click();");
                break;
               //     180,Franch Express,
            case 180:
                webView.loadUrl("javascript:var x=document.getElementById('awbno').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('frmtracking').submit();");
                break;
                   // 181,FSC,

                   // 182,Global Mail Express,
            case 182:
                webView.loadUrl("javascript:var x=document.getElementsByName('hawbno2')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementsByName('Go')[0].click();");
                break;
           // 183,GMS Worldwide,
            case 183:
                webView.loadUrl("javascript:var x=document.getElementById('message').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementsByClassName('fsSubmitButton1')[0].click()");
                break;
            // 184,ICC World,
            case 184:
                webView.loadUrl("javascript:var x=document.getElementsByName('txtawbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('imageField').submit();");
                break;

           // 185,ICL,
            case 185:
                webView.loadUrl("javascript:var x=document.getElementById('txtpodtrack').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('RightColumn_imgGO').click()");
                break;
                   // 186,ICS,

                    //187,Inland World,
            case 187:
                webView.loadUrl("javascript:var x=document.getElementById('cphbody1_cphbody2_txtDocketNumber').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('cphbody1_cphbody2_imgsubmit').click()");
                break;
                   // 188,Innovex,
                         //   189,International Post,
            case 189:
                webView.loadUrl("javascript:var x=document.getElementById('txtNumbers').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('btnContinue').click()");
                break;
                   // 190,Jaipur Golden,
            case 190:
                webView.loadUrl("javascript:var x=document.getElementById('ContentPlaceHolder1_txtsearch').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementsByClassName('btngo')[0].click()");
                break;
                  //  191,Jetline,
            case 191:
                webView.loadUrl("javascript:var x=document.getElementById('consignment').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementsByClassName('fsSubmitButton1')[0].click()");
                break;
                    //192,JV Express
            case 192:
                webView.loadUrl("javascript:var x=document.getElementById('ctl00_ContentPlaceHolder1_txtBillNo').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('ctl00_ContentPlaceHolder1_btnSearch').click()");
                break;
           // 193,Nitco,

            //194,NONSTOP,
            case 194:
                webView.loadUrl("javascript:var x=document.getElementsByName('search_no')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('image2').click()");
                break;
                   // 195,Nuvo Ex,
            case 195:
                webView.loadUrl("javascript:var x=document.getElementById('track-id').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('track-order').click()");
                break;
                  //  196,OCS Worldwide,

                   // 197,Om International,
            case 197:
                webView.loadUrl("javascript:var x=document.getElementById('Tracking1_txtAwb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('Tracking1_btnTrack').click()");
                break;
                   // 198,Om Logistics,
            case 198:
                webView.loadUrl("javascript:var x=document.getElementsByName('cnn')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementsByName('imageField')[0].click()");
                break;
            //        199,Omni Express,

                 //   200,Orbit Worldwide,
            case 200:
                webView.loadUrl("javascript:var x=document.getElementsByName('Awbno')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementsByTagName('form')[0].submit();");
                break;
                   // 201,Overseas Express,
            case 201:
                webView.loadUrl("javascript:var x=document.getElementsByName('track')[0].value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('Submit_slr').click()");
                break;
//203,Smsa-Express,
          //  204,KGM-Hub
            case 204:
                webView.loadUrl("javascript:var x=document.getElementById('txtSearchAgain').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('cmdSearchAgain').click();");
                break;
           // 205,AirSpeed intl Corp
           // 206,Matkahuolto
           // 207,InPost,
            //208,Mondial relay,
            //209,sure post
           // 210,Old Dominion,
            case 210:
                webView.loadUrl("javascript:var x=document.getElementsByName('traceForm:j_idt31').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementsByName('traceForm:j_idt32').click();");
                break;

             //       211,echo,
            case 211:
                webView.loadUrl("javascript:var x=document.getElementById('tbTrackNum').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('showData').click();");
                break;
           // 212,Alfaa Logistics,
            case 212:
                webView.loadUrl("javascript:var x=document.getElementById('txt_Awb').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:document.getElementById('Track_Button').click();");
                break;
           // 213,BookAWheel,
             //       214,City-Link Express,
            case 214:

                webView.loadUrl("javascript:var x=document.getElementById('track').value='"+trackId +"'");
            //chk
            webView.loadUrl("javascript:document.getElementsByName('Submit').click();");
            break;
               //     215,Despatch Bay,
            case 215:

                webView.loadUrl("javascript:var x=document.getElementById('tracking-number').value='"+trackId +"'");
            //chk
            webView.loadUrl("javascript:document.getElementById('submit-tracking').click();");
            break;
                 //   216,Esskay Logistics,
            case 216:

                webView.loadUrl("javascript:var x=document.getElementById('message').value='"+trackId +"'");
            //chk
            webView.loadUrl("javascript:document.getElementsByClass('btn-primary').click();");
            break;
                   // 217,Motherland,
            case 217:

                webView.loadUrl("javascript:var x=document.getElementById('txttrack').value='"+trackId +"'");
            //chk
            webView.loadUrl("javascript:document.getElementById('btnsubmit').click();");
            break;
                    //218,Roadrunner,
                    //219,Send From China,
            case 219:

                webView.loadUrl("javascript:var x=document.getElementsByName('tracknumbers').value='"+trackId +"'");
            //chk
            webView.loadUrl("javascript:document.getElementById('showData').click();");
            break;
           // 220,Sharp Century,
            case 220:
                webView.loadUrl("javascript:var x=document.getElementById('orders').value='"+trackId +"'");
                //chk
                webView.loadUrl("javascript:track();");
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
