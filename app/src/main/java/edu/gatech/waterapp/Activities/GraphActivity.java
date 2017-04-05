package edu.gatech.waterapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

public class GraphActivity extends AppCompatActivity {

    private List<Report> reports = new ArrayList<>();
    private List<String> locations = new ArrayList<>();
    private Spinner locationSpinner;
    private ArrayAdapter<String> adapter;
    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        graph = (GraphView) findViewById(R.id.graph);
        graph.setVisibility(View.GONE);
        locationSpinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

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
                if (!locations.contains(location.getName())) {
                    locations.add(location.getName());
                    adapter.notifyDataSetChanged();
                }
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
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onCreateGraphClicked(View v) {
        String year = ((EditText)findViewById(R.id.year)).getText().toString().trim();
        if (!year.equals("2017")) {
            Toast.makeText(getApplicationContext(), "There are no reports from before 2017!", Toast.LENGTH_SHORT).show();
            graph.setVisibility(View.GONE);
        } else {
            String location = locations.get(locationSpinner.getSelectedItemPosition());
            List<DataPoint> data = new ArrayList<>();
            for (Report report : reports) {
                if (location.equals(report.getLocation().getName()) && year.equals("2017")) {
                    data.add(new DataPoint(report.getTimestamp().getTime(), report.getWaterCondition().ordinal()));
                }
            }
            if (data.size() <= 1) {
                Toast.makeText(getApplicationContext(), "There have not been enough reports to make a graph for this location!", Toast.LENGTH_SHORT).show();
                graph.setVisibility(View.GONE);
            } else {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data.toArray(new DataPoint[data.size()]));
                graph.addSeries(series);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
                graph.getGridLabelRenderer().setNumVerticalLabels(4);
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(3);
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            return dateFormat.format(new Date((long) value));
                        } else {
                            return WaterCondition.values()[(int) value].toString();
                        }
                    }
                });
                graph.setVisibility(View.VISIBLE);
            }
        }
    }
}

