package com.abdelmun3m.backingapp.RecipeDetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * Adapter that handle ingredient Recycler View
 * it will get the activity which implement the IngredientClickListener
 * to link between the adapter and the @FragmentDetails to handle Ingredient Click to generate Toast
 */

public class AdapterIngredient extends RecyclerView.Adapter<AdapterIngredient.IngredientViewHolder>{



    List<Ingredient> mIngredientsList = new ArrayList<>();

    IngredientClickListener mIngredientListener = null;

    int position ;

    public AdapterIngredient(IngredientClickListener listenr){
        this.mIngredientListener = listenr;
    }


    public void UpdateIngredientList(List<Ingredient> newIngredient){
        this.mIngredientsList = newIngredient;
        notifyDataSetChanged();
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item,parent,false);
        IngredientViewHolder mViewHolder = new IngredientViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        holder.OnBind(position);
    }

    @Override
    public int getItemCount() {
        return mIngredientsList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        @BindView(R.id.tv_ingredient)
        TextView mIngredient;

        @BindView(R.id.tv_ingredient_id)
        TextView mIngredientID;

        @BindView(R.id.tv_ingredient_measure)
        TextView mIngredientMeasure;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
            position=getAdapterPosition();
        }

        public void OnBind(int position){
            Ingredient ing = mIngredientsList.get(getAdapterPosition());
            mIngredient.setText(ing.ingredient);
            mIngredientID.setText(""+ing.quantity);
            mIngredientMeasure.setText(ing.measure);
        }

        @Override
        public void onClick(View v) {
            if(mIngredientListener !=null){
            mIngredientListener.OnIngredientClick(mIngredientsList.get(getAdapterPosition()));
            }
        }
    }



    public int getCurrentPosition(){
     return position;
    }
    // interface to handel ingredient click
    public interface IngredientClickListener{
        void OnIngredientClick(Ingredient ing);
    }
}
