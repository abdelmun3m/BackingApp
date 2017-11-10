package com.abdelmun3m.backingapp.RecipeList;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdelmun3m on 20/10/17.
 */

public class FragmentRecipeList extends Fragment implements RecipesListAdapter.RecipeCardListener,
        android.app.LoaderManager.LoaderCallbacks<String> {

    @BindView(R.id.rv_recipes)
    RecyclerView mRecipesRecyclerView;
    @BindView(R.id.pb_loading_cards)
    ProgressBar mLoadingProgress;
    @BindView(R.id.tv_error_message)
    TextView errorMessage ;
    @BindView(R.id.listParent)
    LinearLayout parent;
    View rootView;
    Context mContext;
    RecipesListAdapter mRecipesAdapter;
    public boolean done = false;
    private static final int LOADER_ID = 720;
    private URL mResultUrl ;
    private static final String RECIPE_URL
            = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public Recipe mRecipe ;
    public boolean rotate = false;
    InDualModeRecipeItemClick dualListener = null;



    @Nullable
    private SimpleIdlingResource mIdlingResource;


    public FragmentRecipeList(){}
    public FragmentRecipeList(InDualModeRecipeItemClick dual){



        this.dualListener = dual;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        /* intiat the layout and set the fragment_recipe_card_list.xml to this fragment class
        * which contains a list of the recipes
        */

        Log.d("mylog","onCreateView");
         rootView = inflater.inflate(R.layout.fragment_recipe_card_list,container,false);
         mContext = rootView.getContext();
         ButterKnife.bind(this,rootView);
         setLayoutManger();
        mResultUrl = null;
        try {
            mResultUrl = Data.BuildUrl(RECIPE_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(Data.NetworkConnectivityAvailable(mContext)) {
            getLoaderManager().initLoader(LOADER_ID,null, this);
        }else {
            showErrorMessage(getString(R.string.Network_Error));
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();



    }

    private void setLayoutManger() {
        RecyclerView.LayoutManager manger = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false);
        mRecipesRecyclerView.setLayoutManager(manger);
        mRecipesAdapter = new RecipesListAdapter(this);
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);

    }

    @Override
    public void onRecipeCardClick(Recipe r) {

        if(dualListener != null){
            dualListener.changeDualModeRecipe(r);
        }else {
            Intent in = new Intent(mContext, RecipeDetailsContainer.class);
            in.putExtra(MainActivity.RECIPE_INTENT_KEY,r);
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

                if(mIdlingResource != null){
                    mIdlingResource.setIdleState(false);
                }

                if (cash !=null){
                    deliverResult(cash);

                }else {
                    changeListVisibility(false);
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
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
        if(mIdlingResource != null){
            mIdlingResource.setIdleState(true);
        }
        if (data!=null){
            changeListVisibility(true);
            try {
                List<Recipe> l = Recipe.ParseJson(data);
                mRecipesAdapter.UpdateListOfRecipes(null,l);
                mRecipe = l.get(0);
                if(dualListener != null && !rotate){
                    handler.sendEmptyMessage(1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            showErrorMessage(getString(R.string.No_Data));
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader<String> loader) {

    }


    private void changeListVisibility(boolean show){
        if (show){
            mRecipesRecyclerView.setVisibility(View.VISIBLE);
            mLoadingProgress.setVisibility(View.GONE);
        }else {
           // parent.setGravity(View.TEXT_ALIGNMENT_CENTER);
            mRecipesRecyclerView.setVisibility(View.GONE);
            mLoadingProgress.setVisibility(View.VISIBLE);
        }

    }

    private void showErrorMessage(String message){
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }


    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getMyIdlingResource(){

        if(mIdlingResource == null){
            Log.d("MYTEST:Fragment","Not null");
            mIdlingResource = new SimpleIdlingResource();
        }else {

            Log.d("MYTEST:Fragment","null");
        }
        return  mIdlingResource;
    }


    public interface InDualModeRecipeItemClick{
        void changeDualModeRecipe(Recipe r);
    }



        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
               dualListener.changeDualModeRecipe(mRecipe);
            }
        };

}
