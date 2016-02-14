package in.kuari.trackall.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Layout;
import android.webkit.WebView;
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
    private String contact;
    private String Email;
    private String trackId;

    public EcController(WebView webView, Context context){
        this.webView=webView;
        this.context=context;
    }
    public void PopulateView(long ECId){
        Toast.makeText(context,ECId+"ju",Toast.LENGTH_SHORT).show();
        int id=(int)(ECId);
        initializeURL(id);
        getExtrainfo(id);
    }

private void initializeURL(int id){
    ReadData data=new ReadData(context);
    URL=data.getECommerceByID(id).getURL();

}
    private void getExtrainfo(int id){
        switch (id){
            case 1:
                break;
            case 2:flipkart();
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
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
