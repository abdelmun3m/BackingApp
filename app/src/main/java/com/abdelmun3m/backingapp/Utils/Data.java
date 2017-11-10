package com.abdelmun3m.backingapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by abdelmun3m on 21/10/17.
 */

public class Data {



    public static final URL BuildUrl(String s) throws MalformedURLException {
        URL url = new URL(s);
        return url;
    }

    public static String getResponse(URL url) throws IOException{
        HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            InputStream response = connection.getInputStream();
            Scanner scan= new Scanner(response);
            scan.useDelimiter("\\A");

            if(scan.hasNext()){
                connection.disconnect();
                return scan.next();
            }
            connection.disconnect();
        return null;
    }



    public static boolean NetworkConnectivityAvailable(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}
