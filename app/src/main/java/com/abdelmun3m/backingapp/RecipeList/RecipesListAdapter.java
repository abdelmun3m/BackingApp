package com.abdelmun3m.backingapp.RecipeList;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abdelmun3m on 21/10/17.
 */

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder> {



    List<Recipe> mRecipes = new ArrayList<>();

    RecipeCardListener mRecipeCardListener;
    
    public void UpdateListOfRecipes(List<Recipe> newRecipes){
        this.mRecipes = newRecipes;
        notifyDataSetChanged();
    }

    public RecipesListAdapter(RecipeCardListener listener){

        mRecipeCardListener = listener;

    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card,parent,false);
        RecipeViewHolder mViewHolder = new RecipeViewHolder(mView);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {

         return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_recipe_name) TextView mRecipename;
        @BindView(R.id.iv_recipe_img)
        ImageView mRecipeImage;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe){
            mRecipename.setText(recipe.name);

            if(recipe.imageId == -1){
                recipe.imageId = chooseRecipeImage(Integer.valueOf(recipe.Id));
            }

            mRecipeImage.setImageResource( recipe.imageId);

        }

        @Override
        public void onClick(View v) {
            mRecipeCardListener.onRecipeCardClick(mRecipes.get(getAdapterPosition()));
        }
    }

    private int chooseRecipeImage(int id) {

        if (id == 1){
            return R.drawable.nutella_pie;
        }else if (id == 2){
            return R.drawable.brownes;
        }
        else if(id==3){
            return R.drawable.yellow_cake;
        }else if(id == 4){
            return R.drawable.chassecake;
        }

        return R.drawable.pie;
    }


    public interface RecipeCardListener{
        void onRecipeCardClick(Recipe r);
    }

}
