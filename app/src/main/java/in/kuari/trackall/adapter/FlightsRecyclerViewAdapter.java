package in.kuari.trackall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.kuari.trackall.R;
import in.kuari.trackall.activities.ShowFlightsWeb;
import in.kuari.trackall.bean.FlightBean;
import in.kuari.trackall.utils.Colors;


public class FlightsRecyclerViewAdapter extends RecyclerView.Adapter<FlightsRecyclerViewAdapter.ViewHolder> {

    private final List<FlightBean> flights;
    private final Context context;

    public FlightsRecyclerViewAdapter(List<FlightBean> flights, Context context) {
        this.flights = flights;
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

        final FlightBean flight=flights.get(position);
holder.mView.setBackgroundColor(Colors.getRandomColor());
        holder.flightName.setText(flight.getFlightName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFlightWeb(flight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flights.size();
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
    void loadFlightWeb(FlightBean flight){
        Toast.makeText(context,flight.getFlightName(),Toast.LENGTH_LONG).show();
        Intent intent=new Intent(context, ShowFlightsWeb.class);
        intent.putExtra("webURL",flight.getFlightWebsite());
        context.startActivity(intent);
    }
}
