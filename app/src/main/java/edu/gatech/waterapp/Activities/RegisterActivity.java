package edu.gatech.waterapp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.Models.AccountType;
import edu.gatech.waterapp.Models.User;
import edu.gatech.waterapp.R;

import static edu.gatech.waterapp.Controllers.Database.mAuth;

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

        Database.initialize();
    }

    @Override
    public void onStart() {
        super.onStart();
        Database.addListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        Database.removeListener();
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

    /**
     * Called when the submit button is clicked.
     * Creates a new user with the specified username, password, and AccountType
     * @param v the current view
     */
    public void onSubmit(View v) {
        final EditText emailText = (EditText) findViewById(R.id.usertext);
        final EditText passwordText = (EditText) findViewById(R.id.passwordtext);
        final String email = emailText.getText().toString().trim();
        if (!email.contains("@")) {
            new AlertDialog.Builder(RegisterActivity.this).setTitle("Error").setMessage("Please enter a valid email.")
                    .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            String password = passwordText.getText().toString().trim();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("Registration", "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(getApplicationContext(), "Your password is too weak!", Toast.LENGTH_SHORT).show();
                            passwordText.requestFocus();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(getApplicationContext(), "A user with this email address already exists!", Toast.LENGTH_SHORT).show();
                            emailText.requestFocus();
                        } catch (Exception e) {
                            Log.e("RegistrationFail", e.getMessage());
                        }
                    } else {
                        DatabaseReference ref = Database.getReference("users");
                        FirebaseUser user =  task.getResult().getUser();
                        AccountType type = AccountType.values()[typeSpinner.getSelectedItemPosition()];
                        User u = new User(email, type);
                        ref.child(user.getUid()).setValue(u.toMap());
                        finish();
                    }
                }
            });
        }


    }
}
