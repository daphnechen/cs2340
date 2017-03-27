package edu.gatech.waterapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.R;

import static edu.gatech.waterapp.Controllers.Database.mAuth;

public class AppActivity extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        b = (Button) findViewById(R.id.purityButton);
        DatabaseReference ref = Database.getReference("users/" + Database.currentUser.getUid() + "/accountType");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String accountType = dataSnapshot.getValue(String.class);
                if ("USER".equals(accountType)) {
                    b.setVisibility(View.GONE);
                } else {
                    b.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Called when the Logout button is clicked.
     * Reloads the WelcomeActivity
     * @param v the current view
     */
    public void onLogout(View v) {
        mAuth.signOut();
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
    }

    /**
     * Called when the Edit Profile button is clicked.
     * @param v the current view
     */
    public void onProfileClick(View v) {
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);
    }

    /**
     * Called when the Create Report button is clicked.
     * @param v the current view
     */
    public void onCreateReportClicked(View v) {
        Intent i = new Intent(getApplicationContext(), ReportActivity.class);
        startActivity(i);
    }

    /**
     * Called when the View Report List button is clicked.
     * @param v the current view
     */
    public void onReportListClicked(View v) {
        Intent i = new Intent(getApplicationContext(), ListActivity.class);
        startActivity(i);
    }

    /**
     * Called when the View Map button is clicked.
     * @param v the current view
     */
    public void onMapClicked(View v) {
        Intent i = new Intent (getApplicationContext(), MapActivity.class);
        startActivity(i);
    }

    /**
     * Method to click button for Purity Report
     * @param v the current application view
     */
    public void onPurityReportClicked(View v) {
        Intent i = new Intent(getApplicationContext(), PurityActivity.class);
        startActivity(i);
    }



}
