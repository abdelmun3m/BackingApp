package com.abdelmun3m.backingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.abdelmun3m.backingapp.MainActivity;
import com.abdelmun3m.backingapp.R;
import com.abdelmun3m.backingapp.RecipeDetails.FragmentDetails;
import com.abdelmun3m.backingapp.RecipeList.FragmentRecipeList;
import com.abdelmun3m.backingapp.Utils.Recipe;

/**
 * Created by abdelmun3m on 10/11/17.
 */

public class WidgetIntentService extends IntentService {


    public static final String CHANGE_INGREDIENT_ACTION  = "change_ingredient";
    public static final String Recipe_KEY  = "change_ingredient";
    public static Recipe curRecipe;
    static boolean set = false;
    AppWidgetManager manger;
    RemoteViews views;


    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null) return;

        String action  =intent.getAction();

        if(action.equals(CHANGE_INGREDIENT_ACTION)){
            UpdateWidgetIngredient();
        }
    }

    public void UpdateWidgetIngredient() {


       views = new RemoteViews(this.getPackageName(), R.layout.new_app_widget);

       manger = AppWidgetManager.getInstance(this);

        int[] ids = manger.getAppWidgetIds(new ComponentName(this,NewAppWidget.class));

        if(!set) {
            Log.d("Twid","fals");
            Intent gridIntent = new Intent(this,GridWidgetService.class);
            views.setRemoteAdapter(R.id.widget_gred, gridIntent);
            set = true;
        }else {
            Log.d("Twid","true");
            manger.notifyAppWidgetViewDataChanged(ids, R.id.widget_gred);
        }
        NewAppWidget.UpdateWidgetRecipe(this,manger,ids,views);

    }


    public static void UpdateWidgetRecipe(Context con , Recipe r){

        curRecipe = r;
        Intent intent = new Intent(con,WidgetIntentService.class);

        intent.setAction(WidgetIntentService.CHANGE_INGREDIENT_ACTION);

        con.startService(intent);

    }
}
