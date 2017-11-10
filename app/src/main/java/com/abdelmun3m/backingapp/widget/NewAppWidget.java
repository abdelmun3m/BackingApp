package com.abdelmun3m.backingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.abdelmun3m.backingapp.MainActivity;
import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Ingredient;
import com.abdelmun3m.backingapp.Utils.Recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider{


    public static final String INGREDIENT_KEY = "widget_ingredient_item";
    static Context context;
    static  AppWidgetManager appWidgetManager;
    static int appWidgetIds;
    static Recipe mRecipe = null;
    static List<Ingredient> ingredients =null;
    int currentIngredient=-1;

    public static void setMyfavoriteRecipe(Context context,Recipe r){
        mRecipe =r;
        ingredients =r.ingredients;
       // updateAppWidget(context,appWidgetManager,appWidgetIds);
    }



    public static String getMyFavoriteRecipeId(){
        if(mRecipe != null){

            return mRecipe.Id;
        }

        return null;

    }




    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,RemoteViews views) {
        Log.d("Twid","7");
        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setPendingIntentTemplate(R.id.widget_gred,pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        this.appWidgetManager = appWidgetManager;

        for (int appWidgetId : appWidgetIds) {
            this.appWidgetIds = appWidgetId;
           // updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    public static void UpdateWidgetRecipe(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,RemoteViews views){
        Log.d("Twid","5");
        for (int appWidgetId : appWidgetIds) {
            Log.d("Twid","6");
            updateAppWidget(context, appWidgetManager, appWidgetId,views);
        }
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static int RandomImageResource(){
        int min = 1;
        int max = 4;

        Random r = new Random();
        int id = r.nextInt(max - min + 1) + min;

        /*Log.d("ttttt",""+id);
        */
        switch (id){
            case 1: return R.drawable.chassecake;
            case 2: return R.drawable.brownes;
            case 3: return R.drawable.yellow_cake;
            case 4: return R.drawable.nutella_pie;
        }


        return R.drawable.cake;

    }

}

