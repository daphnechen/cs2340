package edu.gatech.waterapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.R;

import static edu.gatech.waterapp.Controllers.Database.mAuth;
import static edu.gatech.waterapp.Models.UserList.currentUser;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

    }

    /**
     * Called when the logout button is clicked.
     * Reloads the WelcomeActivity
     * @param v the current view
     */
    public void onLogout(View v) {
        mAuth.signOut();
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
    }

    public void onProfileClick(View v) {
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);
    }

    public void onCreateReportClicked(View v) {
        Intent i = new Intent(getApplicationContext(), ReportActivity.class);
        startActivity(i);
    }

    public void onReportListClicked(View v) {
        Intent i = new Intent(getApplicationContext(), ListActivity.class);
        startActivity(i);
    }
}
