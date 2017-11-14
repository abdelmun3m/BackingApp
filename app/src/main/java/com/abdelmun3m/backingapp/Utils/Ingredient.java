package com.abdelmun3m.backingapp.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdelmun3m on 21/10/17.
 */

public class Ingredient implements Parcelable{

    private  final String JSON_QUANTITY = "quantity";
    private  final String JSON_MEASURE = "measure";
    private  final String JSON_INGREDIENT = "ingredient";
    public int id ;
    public String quantity;
    public String measure;
    public String ingredient;


    protected Ingredient(Parcel in) {
        id = in.readInt();
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public Ingredient() {

    }


    // get String and Parse to Ingredient List
    public  List<Ingredient> getIngredients(String string_steps) throws JSONException {
        List<Ingredient> ingredientList = new ArrayList<>();

        JSONArray steps = new JSONArray(string_steps);

        for (int i = 0 ; i < steps.length();i++){
            JSONObject JsonIngredient = steps.getJSONObject(i);

            Ingredient temp = new Ingredient();
            temp.id = i;
            if(JsonIngredient.has(JSON_QUANTITY)){
                temp.quantity = JsonIngredient.getString(JSON_QUANTITY);
            }

            if(JsonIngredient.has(JSON_MEASURE)){
                temp.measure = JsonIngredient.getString(JSON_MEASURE);
            }

            if(JsonIngredient.has(JSON_INGREDIENT)){
                temp.ingredient = JsonIngredient.getString(JSON_INGREDIENT);
            }

            ingredientList.add(temp);
        }


        return ingredientList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
}
