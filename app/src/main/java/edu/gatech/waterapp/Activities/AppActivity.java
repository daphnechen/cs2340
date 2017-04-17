package edu.gatech.waterapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.R;

import static edu.gatech.waterapp.Controllers.Database.mAuth;

public class AppActivity extends AppCompatActivity {

    private Button purityButton;
    private Button purityListButton;
    private Button graphButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        purityButton = (Button) findViewById(R.id.purityButton);
        purityListButton = (Button) findViewById(R.id.purityReportButton);
        graphButton = (Button)  findViewById(R.id.graphButton);
        DatabaseReference ref = Database.getReference("users/" + Database.currentUser.getUid() + "/accountType");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String accountType = dataSnapshot.getValue(String.class);
                if ("USER".equals(accountType)) {
                    purityButton.setVisibility(View.GONE);
                    purityListButton.setVisibility(View.GONE);
                } else {
                    purityButton.setVisibility(View.VISIBLE);
                    purityListButton.setVisibility(View.VISIBLE);
                }
                if ("MANAGER".equals(accountType) || "ADMIN".equals(accountType)) {
                    graphButton.setVisibility(View.VISIBLE);
                } else {
                    graphButton.setVisibility(View.GONE);
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
     * @param v the current application view
     */
    public void onLogout(View v) {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
    }

    /**
     * Called when the Edit Profile button is clicked.
     * @param v the current application view
     */
    public void onProfileClick(View v) {
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);
    }

    /**
     * Called when the Create Report button is clicked.
     * @param v the current application view
     */
    public void onCreateReportClicked(View v) {
        Intent i = new Intent(getApplicationContext(), ReportActivity.class);
        startActivity(i);
    }

    /**
     * Called when the View Report List button is clicked.
     * @param v the current application view
     */
    public void onReportListClicked(View v) {
        Intent i = new Intent(getApplicationContext(), ListActivity.class);
        startActivity(i);
    }

    /**
     * Called when the View Map button is clicked.
     * @param v the current application view
     */
    public void onMapClicked(View v) {
        Intent i = new Intent (getApplicationContext(), MapActivity.class);
        startActivity(i);
    }

    /**
     * Called when the Create Purity Report button is clicked.
     * @param v the current application view
     */
    public void onPurityReportClicked(View v) {
        Intent i = new Intent(getApplicationContext(), PurityActivity.class);
        startActivity(i);
    }

    /**
     * Called when the View Purity Report List button is clicked.
     * @param v the current application view
     */
    public void onPurityReportListClicked(View v) {
        Intent i = new Intent(getApplicationContext(), PurityListActivity.class);
        startActivity(i);
    }

    /**
     * Called when the Show Graph button is clicked
     * @param v the current application view
     */
    public void onGraphClicked(View v) {
        Intent i = new Intent(getApplicationContext(), GraphActivity.class);
        startActivity(i);
    }



}
