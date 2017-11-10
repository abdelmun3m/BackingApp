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

public class Step implements Parcelable{



    public int id;
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;

    public static final String SHORT_DESCRIPTION ="shortDescription";
    public static final String DESCRIPTION ="description";
    public static final String VIDEO_URL ="videoURL";
    public static final String THUMBNAIL_URL ="thumbnailURL";


    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public Step() {

    }

    public static List<Step> getSteps(String stringSteps) throws JSONException{

        List<Step> stepsList = new ArrayList<>();

        JSONArray steps = new JSONArray(stringSteps);

        for (int i = 0 ; i < steps.length();i++){
            JSONObject JsonStep = steps.getJSONObject(i);
            Step temp = new Step();

            if(JsonStep.has(SHORT_DESCRIPTION)){
                temp.shortDescription = JsonStep.getString(SHORT_DESCRIPTION);
            }

            if(JsonStep.has(DESCRIPTION)){
                temp.description = JsonStep.getString(DESCRIPTION);
            }

            if(JsonStep.has(VIDEO_URL)){
                temp.videoURL = JsonStep.getString(VIDEO_URL);
            }

            if (JsonStep.has(THUMBNAIL_URL)){
                temp.thumbnailURL = JsonStep.getString(THUMBNAIL_URL);
            }

            stepsList.add(temp);
        }

        return stepsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }
}
