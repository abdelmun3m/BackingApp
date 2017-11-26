package com.abdelmun3m.backingapp.RecipeList;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abdelmun3m.backingapp.MainActivity;
import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.RecipeDetails.RecipeDetailsContainer;
import com.abdelmun3m.backingapp.SimpleIdlingResource;
import com.abdelmun3m.backingapp.Utils.Data;
import com.abdelmun3m.backingapp.Utils.Recipe;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdelmun3m on 20/10/17.
 * <p>
 * this class is to create Fragment which used in the main activity to load the Recipes from the the Recipe Json data
 * and Display it in a recycler view.
 */

public class FragmentRecipeList extends Fragment implements RecipesListAdapter.RecipeCardListener,
        android.app.LoaderManager.LoaderCallbacks<String> {

    @BindView(R.id.rv_recipes)
    RecyclerView mRecipesRecyclerView;

    @BindView(R.id.pb_loading_cards)
    ProgressBar mLoadingProgress;

    @BindView(R.id.tv_error_message)
    TextView errorMessage;

    @BindView(R.id.listParent)
    LinearLayout parent;

    View rootView;
    Context mContext;
    RecipesListAdapter mRecipesAdapter;
    RecyclerView.LayoutManager manger;
    Boolean dataLoaded = false;


    private final int LOADER_ID = 720;
    private URL mResultUrl;
    private final String RECIPE_URL
            = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private Recipe mRecipe;
    public boolean rotate = false;
    static InDualModeRecipeItemClick dualListener = null;//used in dual mode
    Parcelable detailsState;//to store recyclerViewState
    List<Recipe> mRecipeList;
    @Nullable
    private SimpleIdlingResource mIdlingResource;//used for testing


    public FragmentRecipeList() {
    }

    public FragmentRecipeList(InDualModeRecipeItemClick dual) {
        dualListener = dual;
    }


    public void setDualListener(InDualModeRecipeItemClick dual) {

        dualListener = dual;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        /* initiate the layout and set the fragment_recipe_card_list.xml to this fragment class
        * which contains a list of the recipes
        */

        rootView = inflater.inflate(R.layout.fragment_recipe_card_list, container, false);
        mContext = rootView.getContext();

        ButterKnife.bind(this, rootView);
        if (savedInstanceState == null) {
            //Build the Data URL
            mResultUrl = null;

            try {
                mResultUrl = Data.BuildUrl(RECIPE_URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //check network Connection Before Loading Data.
            if (Data.NetworkConnectivityAvailable(mContext)) {
                errorMessage.setVisibility(View.GONE);
                setLayoutManger();
                getLoaderManager().initLoader(LOADER_ID, null, this);
                dataLoaded =true;
            } else {
                dataLoaded = false;
                showErrorMessage(getString(R.string.Network_Error));
            }
        }
        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            dataLoaded = savedInstanceState.getBoolean("loaded");
            if(dataLoaded){
                detailsState = savedInstanceState.getParcelable(getString(R.string.detail_state));
                mRecipeList = savedInstanceState.getParcelableArrayList(getString(R.string.detail_list));
                setLayoutManger();
                mRecipesAdapter.UpdateListOfRecipes(mRecipeList);
            }else {
                showErrorMessage(getString(R.string.Network_Error));
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(manger != null) {
            manger.onRestoreInstanceState(detailsState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("loaded",dataLoaded);
        if(dataLoaded){
            detailsState = manger.onSaveInstanceState();
            outState.putParcelableArrayList(getString(R.string.detail_list), (ArrayList<? extends Parcelable>) mRecipeList);
            outState.putParcelable(getString(R.string.detail_state), detailsState);
        }
    }

    private void setLayoutManger() {

        manger = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecipesRecyclerView.setVisibility(View.VISIBLE);
        mRecipesRecyclerView.setLayoutManager(manger);
        mRecipesAdapter = new RecipesListAdapter(this);
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);
    }

    @Override
    public void onRecipeCardClick(Recipe r) {

        //decide to create a new Activity or load fragment in the Dual Mode View
        // value of dualListener will be not NUll if we create
        // this fragment from Dual Mode MainActivity  Class
        if (dualListener != null && !rotate) {
            dualListener.changeDualModeRecipe(r);
        } else {
            Intent in = new Intent(mContext, RecipeDetailsContainer.class);
            in.putExtra(MainActivity.RECIPE_INTENT_KEY, r);
            startActivity(in);
        }

    }

    @Override
    public android.content.Loader<String> onCreateLoader(int id, Bundle args) {

        return new android.content.AsyncTaskLoader<String>(mContext) {
            String cash;

            @Override
            public void deliverResult(String data) {
                cash = data;

                super.deliverResult(data);
            }

            @Override
            protected void onStartLoading() {

                if (mIdlingResource != null) {
                    //used in testing
                    mIdlingResource.setIdleState(false);
                }
                if (cash != null) {
                    deliverResult(cash);
                } else {
                    changeListVisibility(false);
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                //TODO change this string to String Builder As Carlos Said
                String mRecipeList = "";
                try {
                    mRecipeList = Data.getResponse(mResultUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mRecipeList;
            }
        };
    }

    @Override
    public void onLoadFinished(android.content.Loader<String> loader, String data) {
        if (mIdlingResource != null) {
            //used in testing
            mIdlingResource.setIdleState(true);
        }
        if (data != null) {
            changeListVisibility(true);
            try {
                mRecipeList = new Recipe().ParseJson(data);
                mRecipesAdapter.UpdateListOfRecipes(mRecipeList);
                mRecipe = mRecipeList.get(0);
                if (dualListener != null && !rotate) {
                    //TODO try to find another solution
                    handler.sendEmptyMessage(1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showErrorMessage(getString(R.string.No_Data));
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader<String> loader) {

    }


    private void changeListVisibility(boolean show) {
        if (show) {
            mRecipesRecyclerView.setVisibility(View.VISIBLE);
            mLoadingProgress.setVisibility(View.GONE);
        } else {
            // parent.setGravity(View.TEXT_ALIGNMENT_CENTER);
            mRecipesRecyclerView.setVisibility(View.GONE);
            mLoadingProgress.setVisibility(View.VISIBLE);
        }

    }

    private void showErrorMessage(String message) {
        errorMessage.setVisibility(View.VISIBLE);
        mRecipesRecyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setText(message);
    }


    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getMyIdlingResource() {

        //Used In tessting
        if (mIdlingResource == null) {
            Log.d("MYTEST:Fragment", "Not null");
            mIdlingResource = new SimpleIdlingResource();
        } else {

            Log.d("MYTEST:Fragment", "null");
        }
        return mIdlingResource;
    }


    public interface InDualModeRecipeItemClick {
        void changeDualModeRecipe(Recipe r);
    }


    //used to set fragmentDetails content to the first element in initiating Dual Mode view
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dualListener.changeDualModeRecipe(mRecipe);
        }
    };

}
