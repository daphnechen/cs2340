package edu.gatech.waterapp.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.Date;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.Models.Report;
import edu.gatech.waterapp.Models.WaterCondition;
import edu.gatech.waterapp.Models.WaterType;
import edu.gatech.waterapp.R;

import static com.google.android.gms.analytics.internal.zzy.m;
import static com.google.android.gms.analytics.internal.zzy.p;
import static com.google.android.gms.analytics.internal.zzy.t;
import static com.google.android.gms.cast.internal.zzl.pl;
import static com.google.android.gms.internal.zzng.fi;

public class ReportActivity extends AppCompatActivity {


    private Spinner typeSpinner, conditionSpinner;

    private int PLACE_PICKER_REQUEST = 13;
    private Place location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        conditionSpinner = (Spinner) findViewById(R.id.conditionSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                Arrays.asList("Bottled", "Well", "Stream", "Lake", "Spring", "Other"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                Arrays.asList("Waster", "Treatable-Clear", "Treatable-Muddy", "Potable"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(adapter2);
    }

    public void onSetLocationClicked(View v) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder().setLatLngBounds(
                new LatLngBounds(new LatLng(33.767359, -84.418463), new LatLng(33.788294, -84.369271)));
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "An error occurred when accessing the Google Maps API", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSubmitClicked(View v) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Please select a location!", Toast.LENGTH_SHORT).show();
        } else {
            Report report = new Report(new Date(), Database.currentUser.getEmail(), new edu.gatech.waterapp.Models.Place(location));
            report.setWaterCondition(WaterCondition.values()[conditionSpinner.getSelectedItemPosition()]);
            report.setWaterType(WaterType.values()[typeSpinner.getSelectedItemPosition()]);
            DatabaseReference ref = Database.getReference("reports");
            ref.push().setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Report created successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "There was an error creating this report!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                location = PlacePicker.getPlace(this, data);
            }
        }
    }
}
