package com.abdelmun3m.backingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
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

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,RemoteViews views) {


        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("Twid","A7a ");
    }


    public static void UpdateWidgetRecipe(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,RemoteViews views){

        for (int appWidgetId : appWidgetIds) {
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

}

