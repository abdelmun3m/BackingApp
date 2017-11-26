package com.abdelmun3m.backingapp.RecipeDetails;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdelmun3m on 22/10/17.
 * <p>
 * <p>
 * <p>
 * class that mange and display the ingredient of a recipe in the Ingredient RecyclerView
 * impalement the @LayoutManager and  @AdapterIngredient
 * <p>
 * <p>
 * it will be called from the class @FragmentDetails.
 * will take  a list of @ingredient that will be passed to the adapter to create view
 */

public class FragmentIngredient extends Fragment implements AdapterIngredient.IngredientClickListener {


    Context mContext;

    List<Ingredient> mIngredientList = new ArrayList<>();

    RecyclerView.LayoutManager manager;
    AdapterIngredient adapter;
    Parcelable ingredientState;

    @BindView(R.id.rv_ingredient)
    RecyclerView mIngredientView;


    public FragmentIngredient() {
    }


    public FragmentIngredient(List<Ingredient> ing) {
        this.mIngredientList = ing;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);
        mContext = rootView.getContext();
        ButterKnife.bind(this, rootView);
        setLayoutManger();
        return rootView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            ingredientState = savedInstanceState.getParcelable(getString(R.string.ingredient_state));
            mIngredientList = savedInstanceState.getParcelableArrayList(getString(R.string.ingredient_list));
        }
        adapter.UpdateIngredientList(mIngredientList);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (manager != null) {
            manager.onRestoreInstanceState(ingredientState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ingredientState = manager.onSaveInstanceState();
        outState.putParcelable(getString(R.string.ingredient_state), ingredientState);
        outState.putParcelableArrayList(getString(R.string.ingredient_list), (ArrayList<? extends Parcelable>) mIngredientList);
    }


    private void setLayoutManger() {
        //set Layout manager and Adapter
        manager =
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        adapter = new AdapterIngredient(this);
        mIngredientView.setAdapter(adapter);
        mIngredientView.setLayoutManager(manager);
        mIngredientView.setHasFixedSize(true);
        adapter.UpdateIngredientList(mIngredientList);
    }

    @Override
    public void OnIngredientClick(Ingredient ing) {
        /* Override from  @AdapterIngredient.IngredientClickListener to
        * in case of the content of ingredient is not full appear create a Toast
        */
        Toast.makeText(mContext, "" + ing.ingredient, Toast.LENGTH_SHORT).show();
    }

}
