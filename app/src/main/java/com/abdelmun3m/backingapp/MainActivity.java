package com.abdelmun3m.backingapp;

import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.abdelmun3m.backingapp.RecipeDetails.FragmentDetails;
import com.abdelmun3m.backingapp.RecipeList.FragmentRecipeList;
import com.abdelmun3m.backingapp.RecipeList.RecipesListAdapter;
import com.abdelmun3m.backingapp.Utils.Recipe;


public class MainActivity extends AppCompatActivity
        implements FragmentRecipeList.InDualModeRecipeItemClick {

    public static final String RECIPE_INTENT_KEY = "recipe";
    private static final String RECIPE_KEY = "recipe";
    public FragmentRecipeList mMainFragment;
    public FragmentDetails mDetailFragment;
    Recipe mRecipe;
    boolean dualMod = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* defined to be used in testing to retrieve Idle Resource Variable */
        if (findViewById(R.id.main) != null) {
            /**
             *In screen size less than 600dp will create
             * Fragment that load and display the recipes to the main Activity
             */
            if (savedInstanceState == null) {
                mMainFragment = new FragmentRecipeList();
                getFragmentManager().beginTransaction().replace(R.id.main, mMainFragment, getString(R.string.detail_fragment)).commit();

            } else {
                mMainFragment = (FragmentRecipeList) getFragmentManager().findFragmentByTag(getString(R.string.detail_fragment));
            }
        }

        if (findViewById(R.id.dual_main_view) != null) {
            /**
             *In screen size 600dp or more will create
             * Fragment that load and display the recipes to the main section of Activity
             *
             * and will pass the function changeDualModeRecipe to
             * fragmentRecipeList to change the content of detail view according to clicked recipe
             */
            dualMod = true;
            if (savedInstanceState == null) {
                mMainFragment = new FragmentRecipeList(this);
                getFragmentManager().beginTransaction().replace(R.id.dual_main_view, mMainFragment, getString(R.string.detail_fragment)).commit();
            } else {
                mMainFragment = (FragmentRecipeList) getFragmentManager().findFragmentByTag(getString(R.string.detail_fragment));
                mMainFragment.setDualListener(this);
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dualMod) {
            outState.putParcelable(RECIPE_KEY, this.mRecipe);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void changeDualModeRecipe(Recipe r) {
        /**
         * function that create Details fragment view according to given recipe
         *  it only works in screen size more than 600dp
         *
         * */
        this.mRecipe = r;
        addDetailsFragment(r);
    }

    public void addDetailsFragment(Recipe r) {
        if (findViewById(R.id.dual_detail_view) != null) {
            mDetailFragment = new FragmentDetails(r);
            getFragmentManager().beginTransaction().replace(R.id.dual_detail_view, mDetailFragment).commit();
        }
    }

}
