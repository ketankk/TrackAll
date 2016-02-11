package in.kuari.trackall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ShowFlightsWeb;
import in.kuari.trackall.bean.ECommerce;
import in.kuari.trackall.bean.FlightBean;
import in.kuari.trackall.utils.Colors;


public class ECommerceAdapter extends RecyclerView.Adapter<ECommerceAdapter.ViewHolder> {

    private final List<ECommerce> eCommerces;
    private final Context context;

    public ECommerceAdapter(List<ECommerce> eCommerces, Context context) {
        this.eCommerces = eCommerces;
        this.context =context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_flights, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ECommerce eCommerce=eCommerces.get(position);
holder.mView.setBackgroundColor(Colors.getRandomColor());
        holder.flightName.setText(eCommerce.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFlightWeb(eCommerce);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eCommerces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView flightName;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            flightName = (TextView) view.findViewById(R.id.flightname);
        }

    }
    void loadFlightWeb(ECommerce eCommerce){
        //Toast.makeText(context,flight.getFlightName(),Toast.LENGTH_LONG).show();
        Intent intent=new Intent(context, ShowFlightsWeb.class);
        intent.putExtra("webURL",eCommerce.getURL());
        intent.putExtra("flightName",eCommerce.getName());

        context.startActivity(intent);
    }
}
