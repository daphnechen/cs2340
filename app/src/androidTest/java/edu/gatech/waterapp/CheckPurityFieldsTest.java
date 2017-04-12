package edu.gatech.waterapp;

/**
 * @author Corey Caskey
 */

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import edu.gatech.waterapp.Activities.PurityActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CheckPurityFieldsTest {

    private String emptyLocationString = "";
    private String validLocationString;
    private String invalidFirstVirusString;
    private String invalidOtherVirusString;
    private String emptyVirusString = "";
    private String validVirusString;
    private String invalidFirstContaminantString;
    private String invalidOtherContaminantString;
    private String emptyContaminantString = "";
    private String validContaminantString;

    @Rule
    public ActivityTestRule<PurityActivity> rActivityRule = new ActivityTestRule<>(PurityActivity.class);

    @Before
    public void initStrings() {
        validLocationString = "Georgia Tech";
        invalidFirstVirusString = "@42";
        invalidOtherVirusString = "42@";
        validVirusString = "10.2";
        invalidFirstContaminantString = "&.35";
        invalidOtherContaminantString = "35&";
        validContaminantString = "20.5";
    }

    @Test
    public void checkEmptyLocation() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(
                emptyLocationString, validVirusString, validContaminantString));
    }

    @Test
    public void checkEmptyVirus() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(
                validLocationString, emptyVirusString, validContaminantString));
    }

    @Test
    public void checkEmptyContaminant() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(
                validLocationString, validVirusString, emptyContaminantString));
    }

    @Test
    public void checkAllEmpty() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(
                emptyLocationString, emptyVirusString, emptyContaminantString));
    }

    @Test
    public void checkInvalidFirstVirus() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(
                validLocationString, invalidFirstVirusString, validContaminantString));
    }

    @Test
    public void checkInvalidFirstContaminant() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(
                validLocationString, validVirusString, invalidFirstContaminantString));
    }

    @Test
    public void checkInvalidOtherVirus() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(
                validLocationString, invalidOtherVirusString, validContaminantString));
    }

    @Test
    public void checkInvalidOtherContaminant() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(
                validLocationString, validVirusString, invalidOtherContaminantString));
    }

    @Test
    public void checkValidEverything() {
        Assert.assertTrue(rActivityRule.getActivity().fieldsComplete(
                validLocationString, validVirusString, validContaminantString));
    }

}
