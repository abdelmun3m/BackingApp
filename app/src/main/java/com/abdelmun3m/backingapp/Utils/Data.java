package com.abdelmun3m.backingapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by abdelmun3m on 21/10/17.
 */

public class Data {


    //build URL
    public static final URL BuildUrl(String s) throws MalformedURLException {
        URL url = new URL(s);
        return url;
    }


    //get url and return its
    public static String getResponse(URL url) throws IOException {
        HttpURLConnection connection = null;
        connection = (HttpURLConnection) url.openConnection();
           /*
            InputStream response = connection.getInputStream();
            Scanner scan= new Scanner(response);
            scan.useDelimiter("\\A");
            if(scan.hasNext()){
                connection.disconnect();
                return scan.next();
            }

            */
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        StringBuilder s = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            s.append(inputLine);
        }

        in.close();
        connection.disconnect();
        return s.toString();
    }

    //check network connectivity
    public static boolean NetworkConnectivityAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}
