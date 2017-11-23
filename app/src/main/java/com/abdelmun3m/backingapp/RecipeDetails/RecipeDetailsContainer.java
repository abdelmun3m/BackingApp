package com.abdelmun3m.backingapp.RecipeDetails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.abdelmun3m.backingapp.MainActivity;
import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Recipe;

import butterknife.ButterKnife;


/*
*
* an Activity that display the content of a recipe
* contains and handle the fragment that manage Ingredient and Step Recycler View
* handle ExoPlayer
* handle Media session
*
*
*
* */
public class RecipeDetailsContainer extends AppCompatActivity {


    private final java.lang.String TAG = "RecipeDetailsContainer" ;

    Recipe mRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);


        Log.d("twid","activity created : " + savedInstanceState );

        //get the recipe object sellected from the user in the main page
        Intent in = getIntent();
        mRecipe = in.getParcelableExtra(MainActivity.RECIPE_INTENT_KEY);

        if(mRecipe != null){

            if(savedInstanceState == null) {
                FragmentDetails mdetails = new FragmentDetails(mRecipe);
                getFragmentManager().beginTransaction().replace(R.id.mainDetails, mdetails).commit();
            }

        }else {
            Toast.makeText(this, "Null intent", Toast.LENGTH_SHORT).show();
        }
    }

}
