package edu.gatech.waterapp;

import android.support.v7.app.AppCompatActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.gatech.waterapp.Controllers.Database;


public class LoginActivityTest extends AppCompatActivity {

    //private Database database;

    @Before
    public void setUp() throws Exception {
        Database.initialize();
        Assert.assertTrue("Database is initialized", Database.isInitialized());
    }

//    @After
//    public void tearDown() throws Exception {
//
//    }

    @Test
    public void testOnSubmit() throws Exception {

    }

}