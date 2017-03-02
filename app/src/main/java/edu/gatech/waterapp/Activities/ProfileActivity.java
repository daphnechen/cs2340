package edu.gatech.waterapp.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.Models.AccountType;
import edu.gatech.waterapp.Models.User;
import edu.gatech.waterapp.Models.UserList;
import edu.gatech.waterapp.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameLabel, emailLabel;
    private Spinner typeSpinner;
    private User currentUser;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar a = getSupportActionBar();
        a.setHomeButtonEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);

        nameLabel = (TextView) findViewById(R.id.username);
        emailLabel = (TextView) findViewById(R.id.email);

        typeSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                Arrays.asList("User", "Worker", "Manager", "Admin"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        ref = Database.getReference("users/"+Database.currentUser.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                String email = Database.currentUser.getEmail();
                AccountType acctype = AccountType.valueOf(dataSnapshot.child("accountType").getValue(String.class));
                if (username != null && !username.isEmpty()) {
                    nameLabel.setText(username);
                }
                emailLabel.setText(email);
                typeSpinner.setSelection(acctype.ordinal());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ref.child("accountType").setValue(AccountType.values()[i].toString());
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
                    ref.child("username").setValue(newName);
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
                final String newEmail = ((EditText)editView.findViewById(R.id.newEmail)).getText().toString().trim();
                if (!newEmail.isEmpty()) {
                    Database.currentUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                emailLabel.setText(newEmail);
                                popup.dismiss();
                                Toast.makeText(c, "Email saved successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(c, "An error occurred while changing your email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(c, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
