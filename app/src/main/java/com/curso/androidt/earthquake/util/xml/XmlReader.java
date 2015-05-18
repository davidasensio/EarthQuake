package com.curso.androidt.earthquake.util.xml;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by androidt on 15/05/2015.
 */
public class XmlReader implements Serializable {
    private final static int CONNECTION_AND_READ_TIMEOUT = 10000;

    public static InputStream getFeedInputStreamFromAssets(Context context, String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static InputStream getFeedInputStreamFromUrl(String urlStr) {
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(CONNECTION_AND_READ_TIMEOUT);
            httpURLConnection.setReadTimeout(CONNECTION_AND_READ_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
}


