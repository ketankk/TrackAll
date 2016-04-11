package in.kuari.trackall.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ShowFlightsWeb;
import in.kuari.trackall.activities.ShowResultActivity;
import in.kuari.trackall.bean.BookMark;
import in.kuari.trackall.bean.CourierBean;
import in.kuari.trackall.bean.ECommerce;
import in.kuari.trackall.bean.FlightBean;
import in.kuari.trackall.databases.SQLiteDBHandler;
import in.kuari.trackall.utils.FunctionTools;
import in.kuari.trackall.utils.ReadData;


public class ECommerceAdapter extends RecyclerView.Adapter<ECommerceAdapter.ViewHolder> {

    private Activity activity;
    private List<ECommerce> eCommerces;
    private List<ECommerce> filteredEC;
    private Context context;
    public ECommerceAdapter(Activity activity) {
        this.activity = activity;
        context=activity;
        populateList();
    }
   void populateList(){
       ReadData data=new ReadData(context);
       eCommerces=data.getAllECommerce();
       filteredEC=data.getAllECommerce();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ecommerce, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ECommerce eCommerce = filteredEC.get(position);

        holder.ECName.setText(eCommerce.getName());
        SharedPreferences pref=activity.getSharedPreferences("TRACKALL",Context.MODE_PRIVATE);
        boolean loadLogo=pref.getBoolean("LoadLogo",true);
        boolean ff=pref.getBoolean("FirstTime",true);
        //Log.d("logo",loadLogo+" "+ff);
        if(loadLogo)
            Picasso.with(activity).load(eCommerce.getImgPath()).error(R.drawable.ic_menu_ecom).into(holder.ECLogo);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FunctionTools.isConnected(activity)) {
                    Snackbar.make(v, "No Internet Connection", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }else {
                  //  Snackbar.make(v, "This Section is not functional now", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                    inputIDEmail(eCommerce);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredEC.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView ECName;
        private View view;
        private ImageView ECLogo;
        public ViewHolder(View itemView) {
            super(itemView);
            ECName= (TextView) itemView.findViewById(R.id.ec_name);
            ECLogo = (ImageView)itemView.findViewById(R.id.ec_logo);
            this.view=itemView;


        }

    }
    public void filter(String ECName){
        filteredEC.clear();
        int i=0;

        if(ECName.length()==0)
            filteredEC.addAll(eCommerces);
        else
        {
            for(ECommerce s:eCommerces){

                if((s.getName().toLowerCase()).contains(ECName)){
                    filteredEC.add(s);

                }
            }
        }

        notifyDataSetChanged();
    }


    private String orderID;
    private String email;

   private void  startActivity(ECommerce eCommerce){
        if (orderID != null && email != null) {
            Intent intent = new Intent(activity, ShowResultActivity.class);

            if (email.length() > 0 && orderID.length() > 0) {
                SaveSearchHistory(eCommerce, email, orderID);
                intent.putExtra("trackId", orderID+"|"+email);//orderid
              //  intent.putExtra("email", email);
            }


            intent.putExtra("comingFrom", 3); //1-courier,2-flights,3- ecommerce
            intent.putExtra("courierID", eCommerce.getId());


            ClipboardManager manager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("TrackingID", "trackID");
            manager.setPrimaryClip(clip);
            Log.d("KK", eCommerce.toString() + orderID + "g" + email);
            activity.startActivity(intent);//, ActivityOptions.makeSceneTransitionAnimation((Activity)context).toBundle());

        }        }


    private void inputIDEmail(final ECommerce ecommName){
            Activity activity= (Activity) context;
            final EditText inputOrderID=new EditText(activity);
        inputOrderID.setHint("Order ID");
        final EditText inputEmail=new EditText(activity);
        inputEmail.setHint("Email id used for order");
        LinearLayout layout=new LinearLayout(activity);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputOrderID);
        layout.addView(inputEmail);


        android.app.AlertDialog.Builder dialog=new android.app.AlertDialog.Builder(activity);
            dialog.setMessage(ecommName.getName()).setView(layout)
                    .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    orderID=inputOrderID.getText().toString();
                    email=inputEmail.getText().toString();
                    Log.d("KK",""+orderID+"g"+email);

                    startActivity(ecommName);
                }
            }).setNegativeButton("Cancel",null);

            dialog.show();
            //Toast.makeText(context,flight.getFlightName()+pnrTrain, Toast.LENGTH_LONG).show();

        }





    void SaveSearchHistory(ECommerce eCommerce,String email,String orderID){
        SQLiteDBHandler handler=new SQLiteDBHandler(activity);
        BookMark history=new BookMark();
        history.setName(eCommerce.getName());
        history.setTrackId(orderID+"|"+ email);
        history.setCourierID(eCommerce.getId()+"");
        history.setbType(3);//1-courier,2-flights,3- ecommerce

        handler.addSearch(history);

    }

    void Popup(final ECommerce eCommerce, final String trackingID){
        new AlertDialog.Builder(activity)
                .setTitle("No Internet Connection")
                .setMessage("BookMark -"+trackingID.toUpperCase()+" with "+eCommerce.getName())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                       // SaveSearchHistory(eCommerce,trackingID);
                    }
                }).setNegativeButton("No",null)
                .show();
    }
}
