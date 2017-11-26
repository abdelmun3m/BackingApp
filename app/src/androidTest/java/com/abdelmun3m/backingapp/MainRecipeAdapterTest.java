package com.abdelmun3m.backingapp;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.abdelmun3m.backingapp.RecipeList.FragmentRecipeList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;
import static org.hamcrest.Matchers.anything;

/**
 * Created by abdelmun3m on 05/11/17.
 */


@RunWith(AndroidJUnit4.class)
public class MainRecipeAdapterTest {

    public static final String recipe = "Nutella Pie";
    private static final String TAG = "MYTEST";

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> myTest =
            new ActivityTestRule<MainActivity>(MainActivity.class);


    @Before
    public void getIdeaResource() {

        // myTest.launchActivity(null);
        mIdlingResource = myTest.getActivity().mMainFragment.getMyIdlingResource();

        if (mIdlingResource != null) {

            Espresso.registerIdlingResources(mIdlingResource);

        } else {

            Log.d(TAG, "Resource Idelling is Null");

        }

    }


    @Test
    public void clickRecipeListAdapterItemSelectionTest() {
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.tv_detail_recipe_name)).check(matches(withText(recipe)));
    }

    @After
    public void unregistResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
