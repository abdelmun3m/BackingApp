package com.abdelmun3m.backingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Recipe;

/**
 * Created by abdelmun3m on 10/11/17.
 */

public class WidgetIntentService extends IntentService {


    public static final String CHANGE_INGREDIENT_ACTION  = "change_ingredient";
    public static final String Recipe_KEY  = "change_ingredient";
    public static Recipe curRecipe;


    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null) return;

        String action  =intent.getAction();

        if(action.equals(CHANGE_INGREDIENT_ACTION)){
            UpdateWidgetIngredient(this,null);
        }
    }

    public  void UpdateWidgetIngredient(Context context,Recipe m) {

        AppWidgetManager manger;
        RemoteViews views;
        views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        manger = AppWidgetManager.getInstance(context);
        int[] ids = manger.getAppWidgetIds(new ComponentName(context,RecipeWidgetProvider.class));

        Intent gridIntent = new Intent(this,GridRemoteViewService.class);
        views.setRemoteAdapter(R.id.widget_gred, gridIntent);


        manger.notifyAppWidgetViewDataChanged(ids,R.id.widget_gred);
        RecipeWidgetProvider.UpdateWidgetRecipe(context,manger,ids,views);

    }


    public static void UpdateWidgetRecipe(Context context , Recipe r){



        /*
        *
         *  this function is called when star Button click in RecipeDetails fragment
        */
        curRecipe = r;

        Intent intent = new Intent(context,WidgetIntentService.class);

        intent.setAction(WidgetIntentService.CHANGE_INGREDIENT_ACTION);

        context.startService(intent);

    }
}
