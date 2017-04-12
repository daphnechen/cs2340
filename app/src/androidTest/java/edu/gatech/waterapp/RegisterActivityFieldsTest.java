package edu.gatech.waterapp;

/**
 * @author Kevin Lieu
 */

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.waterapp.Activities.PurityActivity;
import edu.gatech.waterapp.Activities.RegisterActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterActivityFieldsTest {

    private String emptyEmail = "";
    private String validEmail;
    private String invalidEmail;
    private String validPassword;
    private String emptyPassword = "";

    @Rule
    public ActivityTestRule<RegisterActivity> rActivityRule = new ActivityTestRule<>(RegisterActivity.class);

    @Before
    public void initStrings() {
        validEmail = "klieu@gatech.edu";
        invalidEmail = "kevin";
        validPassword = "password";
    }

    @Test
    public void checkEmptyEmail() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(emptyEmail, validPassword));
    }

    @Test
    public void checkEmptyPassword() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(validEmail, emptyPassword));
    }

    @Test
    public void checkInvalidEmail() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(invalidEmail, validPassword));
    }

    @Test
    public void checkAllEmpty() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(emptyEmail, emptyPassword));
    }

    @Test
    public void checkAllValid() {
        Assert.assertTrue(rActivityRule.getActivity().fieldsComplete(validEmail, validPassword));
    }

    @Test
    public void checkInvalidEmailAndEmptyPassword() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(invalidEmail, emptyPassword));
    }

}
