package edu.gatech.waterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
    }

    public void onLogout(View v) {
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
    }
}
