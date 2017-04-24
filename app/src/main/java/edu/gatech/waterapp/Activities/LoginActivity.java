package edu.gatech.waterapp.Activities;
import android.media.MediaPlayer;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.gatech.waterapp.Controllers.Database;
import edu.gatech.waterapp.Models.AccountType;
import edu.gatech.waterapp.Models.User;
import edu.gatech.waterapp.R;

import static edu.gatech.waterapp.Controllers.Database.mAuth;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager mCallBackManager;
    public static MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar a = getSupportActionBar();
        assert a != null;
        a.setHomeButtonEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);
        Database.initialize();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mCallBackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.facebook);
        loginButton.setReadPermissions("email", "public_profile");
        if (AccessToken.getCurrentAccessToken() != null) {
            handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
        }
        loginButton.registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FacebookLogin", "The login was cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "There was an error logging in through Facebook.", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FacebookHandle", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Database.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FacebookHandle", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("FacebookHandle", "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            final DatabaseReference ref = Database.getReference("users");
                            final FirebaseUser user =  task.getResult().getUser();
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.hasChild(user.getUid())) {
                                        User u = new User(user.getEmail(), AccountType.USER);
                                        ref.child(user.getUid()).setValue(u.toMap());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            Intent i = new Intent(getApplicationContext(), AppActivity.class);
                            startActivity(i);

                        }


                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
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
            if (password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a password!", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
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
                                    mp = MediaPlayer.create(getApplicationContext(), R.raw.piano);
                                    mp.start();
                                    mp.setLooping(true);
                                    startActivity(j);
                                }
                            }
                        });
            }
        }
    }

    public void onPasswordResetClicked(View v) {
        EditText emailText = (EditText) findViewById(R.id.usertext);
        String email = emailText.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter an email address!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Password Reset Email Sent", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(getApplicationContext(), "There is no account associated with this email", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "There was an error sending a reset email", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
