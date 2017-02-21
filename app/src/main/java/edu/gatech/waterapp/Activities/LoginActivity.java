package edu.gatech.waterapp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.gatech.waterapp.Models.User;
import edu.gatech.waterapp.Models.UserList;
import edu.gatech.waterapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar a = getSupportActionBar();
        a.setHomeButtonEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.activity_login:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called When the Submit Button is Clicked.
     * Checks the username and password against the list and logs in if successful.
     * @param v The current view
     */
    public void onSubmit(View v) {
        EditText user = (EditText) findViewById(R.id.usertext);
        EditText pass = (EditText) findViewById(R.id.passwordtext);
        User match = null;
        for (User u : UserList.getUserList()) {
            if (u.getName().equals(user.getText().toString().trim()) && u.getPassword().equals(pass.getText().toString().trim())) {
                match = u;
                break;
            }
        }
        if (match != null) {
            UserList.currentUser = match;
            Intent i = new Intent(getApplicationContext(), AppActivity.class);
            startActivity(i);
        } else {
            new AlertDialog.Builder(this).setTitle("Login Failure")
                    .setMessage("Username or password is incorrect. Please try again!")
                    .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();

        }
    }
}
