package com.abdelmun3m.backingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.Utils.Recipe;

/**
 * Created by abdelmun3m on 10/11/17.
 */

public class WidgetIntentService extends IntentService {


    private static final String CHANGE_INGREDIENT_ACTION  = "change_ingredient";
    public static final String Recipe_KEY  = "change_ingredient";
    public static Recipe cRecipe ;



    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null) return;

        String action  =intent.getAction();

        if(action.equals(CHANGE_INGREDIENT_ACTION)){
            Log.d("Twid","2");
            Recipe recipe = intent.getParcelableExtra(Recipe_KEY);
            Log.d("Twid","2  " + recipe);
            UpdateWidgetIngredient(recipe);
        }
    }

    public void UpdateWidgetIngredient(Recipe recipe) {
        Log.d("Twid","3 ");
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.new_app_widget);
        Intent gridIntent = new Intent(this,GridWidgetService.class);
       // Bundle b  = new Bundle();
        //b.putParcelable("recip",recipe);
        //    b.putInt("t",15);
        //gridIntent.putExtra(Recipe_KEY,recipe);
     //   gridIntent.setData(Uri.parse(gridIntent.toUri(Intent.URI_INTENT_SCHEME)));
    //        Log.d("Twid","3 : "+b.getInt("t"));
  //      gridIntent.putExtras(b);

       // gridIntent.
        this.cRecipe = recipe;
        views.setRemoteAdapter(R.id.widget_gred,gridIntent);

        Log.d("Twid","4");
        AppWidgetManager manger = AppWidgetManager.getInstance(this);
        int[] ids = manger.getAppWidgetIds(new ComponentName(this,NewAppWidget.class));


        NewAppWidget.UpdateWidgetRecipe(this,manger,ids,views);



    }


    public static void StartChangeIngredientService(Context context , Recipe recipe){


        Log.d("Twid","1");
        Intent intent = new Intent(context,WidgetIntentService.class);

        intent.setAction(CHANGE_INGREDIENT_ACTION);

        intent.putExtra(Recipe_KEY,recipe);

        context.startService(intent);

    }
}
