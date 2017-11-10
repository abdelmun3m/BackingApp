package com.abdelmun3m.backingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdelmun3m on 10/11/17.
 */

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Log.d("Twid","cal ser");
        return new GridWidgetFactory(
                 intent.getParcelableArrayListExtra(NewAppWidget.INGREDIENT_KEY),getApplicationContext());


     /*   Log.d("Twid","cal ser");
        return new GridWidgetFactory(getApplicationContext());*/
    }
}


class GridWidgetFactory implements RemoteViewsService.RemoteViewsFactory{

    ArrayList<Parcelable> mIngredient;
    Context mContext;

    GridWidgetFactory(ArrayList<Parcelable> ing,Context context){
        Log.d("Twid","creat con");
        this.mContext = context;
        this.mIngredient = ing;

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
        if(mIngredient != null) return mIngredient.size();
        return 5;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Log.d("Twid","get Viw");
        Ingredient ing = (Ingredient) mIngredient.get(position);
        Log.d("Twid","get Viw : "+ing.id);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item);

        views.setTextViewText(R.id.tv_ingredient,ing.ingredient);
        views.setTextViewText(R.id.tv_ingredient_id,ing.quantity);
        views.setTextViewText(R.id.tv_ingredient_measure,ing.measure);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

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
