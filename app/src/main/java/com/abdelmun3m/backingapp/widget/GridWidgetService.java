package com.abdelmun3m.backingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Ingredient;
import com.abdelmun3m.backingapp.Utils.Recipe;

import java.util.List;

/**
 * Created by abdelmun3m on 10/11/17.
 */

public class GridWidgetService extends RemoteViewsService {

    Recipe r ;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.d("Twid","cal ser");
        r = WidgetIntentService.cRecipe;
        GridWidgetFactory gridWidgetFactory = new GridWidgetFactory(r, this);
        Log.d("Twid","gridWidgetFactory return ");
        return gridWidgetFactory;


    }
}


class GridWidgetFactory implements RemoteViewsService.RemoteViewsFactory{

    Recipe mRecipe;
    List<Ingredient> mIngredient;
    Context mContext;

    GridWidgetFactory(Recipe recipe,Context context){
        Log.d("Twid","creat con");
        this.mContext = context;
        this.mRecipe = recipe;
        this.mIngredient = recipe.ingredients;

    }

    public GridWidgetFactory(Context context) {
        Log.d("Twid","creat con");
        this.mContext = context;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d("Twid"," ggg "+mIngredient.size());
        if(mIngredient != null) return mIngredient.size();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Log.d("Twid","get Viw");
        Ingredient ing =  mIngredient.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);
        views.setTextViewText(R.id.tv_widget_ingredient,ing.ingredient);
        views.setTextViewText(R.id.tv_widget_ingredient_id,ing.quantity);
        views.setTextViewText(R.id.tv_widget_ingredient_measure,ing.measure);


        Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.layout.widget_ingredient_item, fillInIntent);


        return views;
    }

    @Override
    public RemoteViews getLoadingView() {return null;}

    @Override
    public int getViewTypeCount() {
        return 0;
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
