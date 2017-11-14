package com.abdelmun3m.backingapp.RecipeDetails;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 *
 *
 *
 * class that mange and display the ingredient of a recipe in the Ingredient RecyclerView
 * impalement the @LayoutManager and  @AdapterIngredient
 *
 *
 * it will be called from the class @FragmentDetails.
 * will take  a list of @ingredient that will be passed to the adapter to create view
 */

public class FragmentIngredient extends Fragment implements AdapterIngredient.IngredientClickListener{


    Context mContext;

    List<Ingredient> mIngredientList = new ArrayList<>();

    @BindView(R.id.rv_ingredient)
    RecyclerView mIngredientView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredient,container,false);
        mContext = rootView.getContext();
        ButterKnife.bind(this,rootView);
        setLayoutManger();
        return rootView;
    }

    public FragmentIngredient(){
    }


    public FragmentIngredient(List<Ingredient> ing){
        this.mIngredientList = ing;
    }


    private void setLayoutManger() {
        //set Layout manager and Adapter
        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        AdapterIngredient adapter = new AdapterIngredient(this);
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
        Toast.makeText(mContext,""+ing.ingredient, Toast.LENGTH_SHORT).show();
    }
}
