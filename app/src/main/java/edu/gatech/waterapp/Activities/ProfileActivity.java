package edu.gatech.waterapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import edu.gatech.waterapp.Models.AccountType;
import edu.gatech.waterapp.Models.User;
import edu.gatech.waterapp.Models.UserList;
import edu.gatech.waterapp.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameLabel, emailLabel;
    private Spinner typeSpinner;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar a = getSupportActionBar();
        a.setHomeButtonEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);

        nameLabel = (TextView) findViewById(R.id.username);
        emailLabel = (TextView) findViewById(R.id.email);

        currentUser = UserList.currentUser;
        if (currentUser.getName() != null && !currentUser.getName().isEmpty()) {
            nameLabel.setText(currentUser.getName());
        }
        if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
            emailLabel.setText(currentUser.getEmail());
        }

        typeSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                Arrays.asList("User", "Worker", "Manager", "Admin"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        if (currentUser.getAccountType() != null) {
            typeSpinner.setSelection(currentUser.getAccountType().ordinal());
        }
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentUser.setAccountType(AccountType.values()[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.activity_profile:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onNameClicked(View v) {
        final Context c = v.getContext();
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View editView = inflater.inflate(R.layout.edit_display_name_layout, null);
        final AlertDialog popup = new AlertDialog.Builder(v.getContext()).setView(editView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
        popup.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = ((EditText)editView.findViewById(R.id.newName)).getText().toString().trim();
                if (!newName.isEmpty()) {
                    currentUser.setName(newName);
                    nameLabel.setText(newName);
                    popup.dismiss();
                    Toast.makeText(c, "Username edited successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(c, "Please enter a new username.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onEmailClicked(View v) {
        final Context c = v.getContext();
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View editView = inflater.inflate(R.layout.edit_email_layout, null);
        final AlertDialog popup = new AlertDialog.Builder(v.getContext()).setView(editView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
        popup.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = ((EditText)editView.findViewById(R.id.newEmail)).getText().toString().trim();
                if (!newEmail.isEmpty()) {
                    currentUser.setEmail(newEmail);
                    emailLabel.setText(newEmail);
                    popup.dismiss();
                    Toast.makeText(c, "Email saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(c, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
