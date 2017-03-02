package edu.gatech.waterapp.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
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

public class WelcomeActivity extends AppCompatActivity {

    private CallbackManager mCallBackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Database.initialize();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_welcome);

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


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FacebookHandle", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
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


}
