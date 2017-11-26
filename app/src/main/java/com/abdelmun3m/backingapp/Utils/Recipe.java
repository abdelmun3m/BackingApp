package com.abdelmun3m.backingapp.Utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by abdelmun3m on 21/10/17.
 */

public class Recipe implements Parcelable {

    private final String JSON_ID = "id";
    private final String JSON_NAME = "name";
    private final String JSON_INGREDIENTS = "ingredients";
    private final String JSON_STEPS = "steps";
    private final String JSON_IMAGE = "image";

    public String Id;
    public String name;
    public int servings;
    public int imageId = -1;
    public String imageUrl;
    public List<Ingredient> ingredients = new ArrayList<>();
    public List<Step> steps = new ArrayList<>();


    protected Recipe(Parcel in) {
        Id = in.readString();
        imageId = in.readInt();
        name = in.readString();
        servings = in.readInt();
        imageUrl = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe() {

    }


    // get string and return List Of Recipes
    // calls ParseJson of Ingredient JsonParse
    //calls ParseJson of Steps JsonParse
    public List<Recipe> ParseJson(String queryResult) throws JSONException {

        List<Recipe> recipeList = new ArrayList<>();
        JSONArray result = new JSONArray(queryResult);

        for (int i = 0; i < result.length(); i++) {

            JSONObject JsonRecipe = result.getJSONObject(i);
            Recipe temp = new Recipe();

            if (JsonRecipe.has(JSON_ID)) {
                temp.Id = JsonRecipe.getString(JSON_ID);
            }

            if (JsonRecipe.has(JSON_NAME)) {
                temp.name = JsonRecipe.getString(JSON_NAME);
            }

            if (JsonRecipe.has(JSON_IMAGE)) {

                temp.imageUrl = JsonRecipe.getString(JSON_IMAGE);
            }

            if (JsonRecipe.has(JSON_INGREDIENTS)) {

                temp.ingredients = new Ingredient().getIngredients(JsonRecipe.getString(JSON_INGREDIENTS));

            }

            if (JsonRecipe.has(JSON_STEPS)) {
                temp.steps = new Step().getSteps(JsonRecipe.getString(JSON_STEPS));

            }
            recipeList.add(temp);
        }
        return recipeList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeInt(imageId);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(imageUrl);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }


    public void loadMovieImage(String uri, ImageView im) {
        if (uri == null || uri.equals("")) {
            return;
        }
        String Image_URL = uri;
        Picasso.with(im.getContext()).load(Image_URL).error(android.R.drawable.ic_menu_gallery)
                .into(im, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}

