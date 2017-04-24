package edu.gatech.waterapp.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.Models.Place;
import edu.gatech.waterapp.Models.Report;
import edu.gatech.waterapp.Models.WaterCondition;
import edu.gatech.waterapp.Models.WaterType;
import edu.gatech.waterapp.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final LatLngBounds boundaries = new LatLngBounds(new LatLng(33.77, -84.4073), new LatLng(33.7815, -84.3878));
    private List<Report> reports;
    private Map<Marker,Report> markerMap;
    private boolean isLocationEnabled;
    private int MY_PERMISSIONS_REQUEST_LOCATION = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            isLocationEnabled = true;
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        mMap.setLatLngBoundsForCameraTarget(boundaries);
        mMap.setMinZoomPreference(13);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(boundaries.getCenter(), 16));

        reports = new ArrayList<>();
        markerMap = new HashMap<>();
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
                final Report r = new Report(timestamp, reporter, location);

                DatabaseReference refer = Database.getReference("users/"+r.getReporter());
                refer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("username").getValue(String.class);
                        r.setReporter(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                r.setWaterType(type);
                r.setWaterCondition(condition);
                r.setReportNumber(number);
                Log.d("ListActivity", "Report #"+r.getReportNumber()+" retrieved from server.");
                reports.add(r);
                updateMarkers();
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
                updateMarkers();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                markerMap.get(marker);
            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout layout = new LinearLayout(getApplicationContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                Report currentReport = markerMap.get(marker);

                TextView reportNumber = new TextView(getApplicationContext());
                reportNumber.setText("Report #" + currentReport.getReportNumber());
                layout.addView(reportNumber);

                TextView reportUser = new TextView(getApplicationContext());
                SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");
                reportUser.setText("Reported by " + currentReport.getReporter() + " on " + ft.format(currentReport.getTimestamp()));
                layout.addView(reportUser);

                TextView reportLocation = new TextView(getApplicationContext());
                reportLocation.setText("Location: " + currentReport.getLocation().getName() + "\nAddress: " + currentReport.getLocation().getAddress());
                layout.addView(reportLocation);

                TextView reportWater = new TextView(getApplicationContext());
                reportWater.setText("Water Type: " + currentReport.getWaterType() + "\nWater Condition: " + currentReport.getWaterCondition());
                layout.addView(reportWater);

                return layout;
            }
        });
    }

    /**
     * Updates the location markers on the map display.
     */
    private void updateMarkers() {
        mMap.clear();
        markerMap.clear();
        for (Report report : reports) {
            MarkerOptions marker = new MarkerOptions().position(report.getLocation().getLocation());
            Marker m = mMap.addMarker(marker);
            markerMap.put(m, report);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                isLocationEnabled = true;
                try {
                    mMap.setMyLocationEnabled(true);
                } catch (SecurityException e) {}
            } else {
                // permission denied. Disable the functionality that depends on this permission.
                isLocationEnabled = false;
                try {
                    mMap.setMyLocationEnabled(false);
                } catch (SecurityException e) {}
            }
        }
    }


}
