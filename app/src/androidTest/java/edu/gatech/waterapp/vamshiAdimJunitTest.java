package edu.gatech.waterapp;

/**
 * Created by Vamshi Adimulam on 4/3/17.
 */

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import junit.framework.Assert;


import edu.gatech.waterapp.Activities.GraphActivity;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class vamshiAdimJunitTest {

    private String emptyYear = "";
    private String validYear;
    private String invalidYear;
    private String invalidOtherYear;
    private String invalidOtherYear1;
    private String invalidOtherYear2;

    @Rule
    public ActivityTestRule<GraphActivity> rActivityRule = new ActivityTestRule<>(GraphActivity.class);

    @Before
    public void initStrings() {
        validYear = "2018";
        invalidYear = "202";
        invalidOtherYear = "10004";
        invalidOtherYear1 = "10002";
        invalidOtherYear2 = "ZZZ2";

    }

    @Test
    public void checkEmptyBoth() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(emptyYear));
    }

    @Test
    public void checkEmptyEmail() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(invalidYear));
    }

    @Test
    public void checkEmptyUsername() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(invalidOtherYear));
    }

    @Test
    public void checkValidEverything() {
        Assert.assertTrue(rActivityRule.getActivity().fieldsComplete(validYear));
    }

    @Test
    public void checkSmallNumber() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(invalidOtherYear1));
    }

    @Test
    public void checkAlphabet() {
        Assert.assertFalse(rActivityRule.getActivity().fieldsComplete(invalidOtherYear2));
    }
}

