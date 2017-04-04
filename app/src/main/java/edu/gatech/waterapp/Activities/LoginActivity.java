package edu.gatech.waterapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar a = getSupportActionBar();
        assert a != null;
        a.setHomeButtonEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);
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
        EditText emailText = (EditText) findViewById(R.id.usertext);
        EditText passText = (EditText) findViewById(R.id.passwordtext);
        String email = emailText.getText().toString().trim();
        if (!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
        } else {
            String password = passText.getText().toString().trim();
            Database.mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("EmailPassword", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("SignIn", "signInWithEmail", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent j = new Intent(getApplicationContext(), AppActivity.class);
                                startActivity(j);
                            }
                        }
                    });
        }
    }
}
