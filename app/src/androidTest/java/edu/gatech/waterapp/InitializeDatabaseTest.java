package edu.gatech.waterapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.waterapp.Controllers.Database;

@RunWith(AndroidJUnit4.class)
public class InitializeDatabaseTest {

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        FirebaseApp.initializeApp(context);
        Database.mAuth = null;
        Database.mAuthListener = null;
        Database.initialized = false;
    }

    @Test
    public void testDefaultInitialization() {
        FirebaseUser oldUser = Database.currentUser;
        Database.initialize();
        Assert.assertEquals("intialized should equal true after call", true, Database.initialized);
        Assert.assertNotNull("mAuth should not be null", Database.mAuth);
        Assert.assertNotNull("mAuthListener should not be null", Database.mAuthListener);
        Assert.assertEquals("currentUser should not have changed", oldUser, Database.currentUser);
    }

    @Test
    public void testInitializeWithBooleanTrue() {
        FirebaseUser oldUser = Database.currentUser;
        Database.initialized = true;
        Database.initialize();
        Assert.assertTrue("intialized should equal true after call", Database.initialized);
        Assert.assertNull("mAuth should not be reinitialized", Database.mAuth);
        Assert.assertNull("mAuthListener should not be reinitialized", Database.mAuthListener);
        Assert.assertEquals("currentUser should not have changed", oldUser, Database.currentUser);
    }

    @Test
    public void testInitializeTwice() {
        testDefaultInitialization();
        FirebaseAuth currentAuth = Database.mAuth;
        FirebaseAuth.AuthStateListener listener = Database.mAuthListener;
        Database.initialize();
        Assert.assertTrue("initialized should equal true after both calls", Database.initialized);
        Assert.assertEquals("mAuth should not change after second call", currentAuth, Database.mAuth);
        Assert.assertEquals("mAuthListener should not have changed after second call", listener, Database.mAuthListener);
    }


}
