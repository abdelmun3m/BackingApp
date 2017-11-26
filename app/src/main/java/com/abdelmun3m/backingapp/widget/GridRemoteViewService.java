package com.abdelmun3m.backingapp.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.RecipeDetails.RecipeDetailsContainer;
import com.abdelmun3m.backingapp.Utils.Ingredient;
import com.abdelmun3m.backingapp.Utils.Recipe;

import java.util.List;

/**
 * Created by abdelmun3m on 10/11/17.
 */

public class GridRemoteViewService extends RemoteViewsService {

    public static Recipe r;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        r = WidgetIntentService.curRecipe;
        return new GridRemoteViewFactory(r, this);


    }
}


class GridRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    Recipe mRecipe;
    List<Ingredient> mIngredient;
    Context mContext;
    RemoteViews views;

    GridRemoteViewFactory(Recipe recipe, Context context) {


        mContext = context;
        mRecipe = recipe;
        mIngredient = recipe.ingredients;

    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        mRecipe = WidgetIntentService.curRecipe;
        mIngredient = WidgetIntentService.curRecipe.ingredients;

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredient != null) return mIngredient.size();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Ingredient ing = mIngredient.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item);

        views.setTextViewText(R.id.tv_ingredient, ing.ingredient);
        views.setTextViewText(R.id.tv_ingredient_id, ing.quantity);
        views.setTextViewText(R.id.tv_ingredient_measure, ing.measure);


        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
