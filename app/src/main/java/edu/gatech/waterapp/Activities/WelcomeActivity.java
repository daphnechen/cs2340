package edu.gatech.waterapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import edu.gatech.waterapp.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    /**
     * Called when the Login button is clicked.
     * Opens the LoginActivity for users to login.
     * @param v the current view
     */
    public void onLoginClick(View v) {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

    /**
     * Called when the Register button is clicked.
     * Opens the RegisterActivity for users to create an account.
     * @param v the current view
     */
    public void onRegisterClick(View v) {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }




}
