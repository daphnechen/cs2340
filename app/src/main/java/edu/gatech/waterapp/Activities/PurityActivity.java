package edu.gatech.waterapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.Models.PurityReport;
import edu.gatech.waterapp.Models.WaterCondition;
import edu.gatech.waterapp.Models.WaterType;
import edu.gatech.waterapp.R;

public class PurityActivity extends AppCompatActivity {

    private Spinner typeSpinner, conditionSpinner;

    private int PLACE_PICKER_REQUEST = 13;
    private Place location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purity);

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        conditionSpinner = (Spinner) findViewById(R.id.conditionSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                Arrays.asList("Bottled", "Well", "Stream", "Lake", "Spring", "Other"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                Arrays.asList("Waste", "Treatable-Clear", "Treatable-Muddy", "Potable"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(adapter2);
    }

    /**
     * When user clicks setLocation button, this method makes the map appear
     * @param v the current view
     */
    public void onSetLocationClicked(View v) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder().setLatLngBounds(
                new LatLngBounds(new LatLng(33.767359, -84.418463), new LatLng(33.788294, -84.369271)));
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "An error occurred when accessing the Google Maps API", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * When the user clicks the submit button, this method will submit the user's report
     * @param v the current view
     */
    public void onSubmitClicked(View v) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Please select a location!", Toast.LENGTH_SHORT).show();
        } else {
            final PurityReport report = new PurityReport(new Date(), Database.currentUser.getUid(), new edu.gatech.waterapp.Models.Place(location));
            report.setWaterCondition(WaterCondition.values()[conditionSpinner.getSelectedItemPosition()]);
            report.setWaterType(WaterType.values()[typeSpinner.getSelectedItemPosition()]);
            EditText virusPPM = (EditText) findViewById(R.id.virusPPM);
            EditText contaminantPPM = (EditText) findViewById(R.id.contaminantPPM);
            report.setVirusCount(Float.parseFloat(virusPPM.getText().toString()));
            report.setContaminantCount(Float.parseFloat(contaminantPPM.getText().toString()));
            final Map<String, Object> map = report.toMap();
            map.put("reporter", Database.currentUser.getUid());
            final DatabaseReference reportnumref = Database.getReference("nextReportNumber");
            reportnumref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final int reportNum = dataSnapshot.getValue(Integer.class);
                    map.put("reportNumber", reportNum);
                    final DatabaseReference ref = Database.getReference("purityReports");
                    ref.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Purity Report created successfully!", Toast.LENGTH_SHORT).show();
                                reportnumref.setValue(reportNum + 1);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "There was an error creating this purity report!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                location = PlacePicker.getPlace(this, data);
                ((TextView)findViewById(R.id.locationText)).setText("Selected: " + location.getName());
            }
        }
    }

}
