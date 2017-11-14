package com.abdelmun3m.backingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

    public static Recipe r ;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.d("Twid","ser");
        r = WidgetIntentService.curRecipe;
       // GridWidgetFactory gridWidgetFactory = new GridWidgetFactory(r, this);

        return new GridWidgetFactory(r, this);


    }
}


class GridWidgetFactory implements RemoteViewsService.RemoteViewsFactory{

    static Recipe mRecipe;
    List<Ingredient> mIngredient;
    Context mContext;
    RemoteViews views;

    GridWidgetFactory(Recipe recipe,Context context){

        Log.d("Twid","fact");
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
        Log.d("Twid","chng : " + mRecipe.name);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mIngredient != null) return mIngredient.size();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("Twid","viw : " + mRecipe.name);

        Ingredient ing =  mIngredient.get(position);
        views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);
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
