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


    public static final String INGREDIENT_KEY = "ing";
    static Context context;
    static  AppWidgetManager appWidgetManager;
    static int appWidgetIds;
    static Recipe mRecipe = null;
    static List<Ingredient> ingredients =null;
    int currentIngredient=-1;

    public static void setMyfavoriteRecipe(Context context,Recipe r){
        mRecipe =r;
        ingredients =r.ingredients;
        updateAppWidget(context,appWidgetManager,appWidgetIds);
    }



    public static String getMyFavoriteRecipeId(){
        if(mRecipe != null){

            return mRecipe.Id;
        }

        return null;

    }




    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

         CharSequence widgetText = context.getString(R.string.appwidget_text);
         RemoteViews views;
        if(mRecipe != null){
            views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

            //Log.d("tttt",mRecipe.name+":"+mRecipe.imageId);
            /*views.setTextViewText(R.id.tv_ingredient,ingredients.get(0).ingredient);
            views.setTextViewText(R.id.tv_ingredient_id,ingredients.get(0).quantity);
            views.setTextViewText(R.id.tv_ingredient_measure,ingredients.get(0).measure);*/

            
            
            /**
            *An intent that call the RemotoViews Service to vreate remote view adapter 
            *i need to sent list 'arr' with this intent but when i use  in.putParcelableArrayListExtra
            * intent dosn't launch.
            **/
            Log.d("Twid","before");
            Intent in = new Intent(context,GridWidgetService.class);

            //Ingredient[] arr = new Ingredient[mRecipe.ingredients.size()];

            ArrayList<Ingredient> arr = new ArrayList<>();
            for(int i =  0; i < mRecipe.ingredients.size() ; i++){
               arr.add(mRecipe.ingredients.get(i));

             // arr[i] = mRecipe.ingredients.get(i);
            }
            in.putParcelableArrayListExtra(INGREDIENT_KEY,arr);
           // in.putExtra(INGREDIENT_KEY,arr);
            views.setRemoteAdapter(R.id.widget_gred,in);

        }
        else {
             views = new RemoteViews(context.getPackageName(), R.layout.ingredient_item);
          //  Log.d("tttt","nu");
        //    views.setImageViewResource(R.id.appwidget_img, RandomImageResource());
        }
        //Intent intent = new Intent(context,MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        //views.setOnClickPendingIntent(R.id.appwidget_img,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;
        this.appWidgetManager = appWidgetManager;

      /*  if( ingredients !=null && currentIngredient <= ingredients.size()){
            currentIngredient++;
        }else {
            currentIngredient =-1;
        }*/
        for (int appWidgetId : appWidgetIds) {
            this.appWidgetIds = appWidgetId;
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

