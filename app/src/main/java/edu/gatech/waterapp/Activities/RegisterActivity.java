package edu.gatech.waterapp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;

import edu.gatech.waterapp.Models.AccountType;
import edu.gatech.waterapp.Models.User;
import edu.gatech.waterapp.Models.UserList;
import edu.gatech.waterapp.R;

public class RegisterActivity extends AppCompatActivity {

    private Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar a = getSupportActionBar();
        a.setHomeButtonEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);

        typeSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                Arrays.asList("User", "Worker", "Manager", "Admin"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.activity_register:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSubmit(View v) {
        EditText user = (EditText) findViewById(R.id.usertext);
        EditText pass = (EditText) findViewById(R.id.passwordtext);
        String username = user.getText().toString();
        if (UserList.getUser(username) != null) {
            String password = pass.getText().toString();
            AccountType type = AccountType.values()[typeSpinner.getSelectedItemPosition()];
            User u = new User(username, password, type);
            if (UserList.addUser(u)) {
                Intent i = new Intent(getApplicationContext(), AppActivity.class);
                i.putExtra("id", username);
                startActivity(i);
            } else {
                new AlertDialog.Builder(this).setTitle("Creation Failure").setMessage("Unable to create account.  Please try again later.")
                        .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        } else {
            new AlertDialog.Builder(this).setTitle("Creation Failure").setMessage("Username already taken!")
                    .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }


    }
}
