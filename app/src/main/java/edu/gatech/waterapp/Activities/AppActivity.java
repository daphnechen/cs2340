package edu.gatech.waterapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.gatech.waterapp.R;

public class AppActivity extends AppCompatActivity {

    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        currentUser = getIntent().getStringExtra("id");
    }

    public void onLogout(View v) {
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
    }
}
