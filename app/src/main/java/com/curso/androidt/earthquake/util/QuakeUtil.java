package com.curso.androidt.earthquake.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.curso.androidt.earthquake.Quake;

/**
 * Created by davasens on 6/10/2015.
 */
public class QuakeUtil {

    public static void showOnMap(Context context, Quake quake) {
        double latitude = quake.getLatitude();
        double longitude = quake.getLongitude();
        String label = "Earthquake";
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query =  latitude+ "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=15";
        Uri uri = Uri.parse(uriString);
        Intent intentMap = new Intent(android.content.Intent.ACTION_VIEW, uri);
        context.startActivity(intentMap);
    }
}
