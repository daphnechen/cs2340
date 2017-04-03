package edu.gatech.waterapp.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.Models.Place;
import edu.gatech.waterapp.Models.Report;
import edu.gatech.waterapp.Models.WaterCondition;
import edu.gatech.waterapp.Models.WaterType;
import edu.gatech.waterapp.R;


public class ListActivity extends AppCompatActivity {

    private List<Report> reports;

    private ReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        reports = new ArrayList<>();

        DatabaseReference ref = Database.getReference("reports");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int number = dataSnapshot.child("reportNumber").getValue(Integer.class);
                Date timestamp = dataSnapshot.child("timestamp").getValue(Date.class);
                WaterType type = WaterType.valueOf(dataSnapshot.child("waterType").getValue(String.class));
                WaterCondition condition = WaterCondition.valueOf(dataSnapshot.child("waterCondition").getValue(String.class));
                String reporter = dataSnapshot.child("reporter").getValue(String.class);
                Place location = new Place();
                location.setAddress(dataSnapshot.child("location").child("address").getValue(String.class));
                location.setName(dataSnapshot.child("location").child("name").getValue(String.class));
                GenericTypeIndicator<List<Double>> t = new GenericTypeIndicator<List<Double>>() {};
                List<Double> latlng = dataSnapshot.child("location").child("location").getValue(t);
                location.setLocation(new LatLng(latlng.get(0),latlng.get(1)));
                Report r = new Report(timestamp, reporter, location);
                r.setWaterType(type);
                r.setWaterCondition(condition);
                r.setReportNumber(number);
                Log.d("ListActivity", "Report #"+r.getReportNumber()+" retrieved from server.");
                reports.add(r);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int reportNumber = dataSnapshot.child("reportNumber").getValue(Integer.class);
                for (int i = 0; i < reports.size(); i++) {
                    if (reports.get(i).getReportNumber() == reportNumber) {
                        reports.remove(i);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RecyclerView reportView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new ReportAdapter(this, reports);
        reportView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reportView.setLayoutManager(llm);
    }

    private class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final TextView number;
            public final TextView reporter;
            public final TextView place;
            public final TextView water;

            public ViewHolder(View itemView) {
                super(itemView);
                number = (TextView) itemView.findViewById(R.id.numberLabel);
                reporter = (TextView) itemView.findViewById(R.id.reporterLabel);
                place = (TextView) itemView.findViewById(R.id.PlaceName);
                water = (TextView) itemView.findViewById(R.id.waterLabel);
            }
        }

        private final Context context;
        private final List<Report> reports;
        private DatabaseReference ref;

        public ReportAdapter(Context c, List<Report> reports) {
            context = c;
            this.reports = reports;
        }


        public Context getContext() {
            return context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View reportView = inflater.inflate(R.layout.report_list_item_layout, parent, false);
            return new ViewHolder(reportView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Report report = reports.get(position);
            holder.number.setText("Report #"+report.getReportNumber());
            String placeName = report.getLocation().getName();
            if (placeName.isEmpty()) {
                holder.place.setText("Location: "+report.getLocation().getAddress());
            } else {
                holder.place.setText("Location: "+placeName+"\nAddress: "+report.getLocation().getAddress());
            }
            ref = Database.getReference("users/"+report.getReporter());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("username").getValue(String.class);
                    SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");
                    holder.reporter.setText("Reported by "+ name + " on " + ft.format(report.getTimestamp()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            holder.water.setText("Water Type: " + report.getWaterType()+ "\nWater Condition: "+ report.getWaterCondition());




        }

        @Override
        public int getItemCount() {
            return reports.size();
        }
    }
}
