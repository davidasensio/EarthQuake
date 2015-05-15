package com.curso.androidt.earthquake.util;

import com.curso.androidt.earthquake.Quake;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by androidt on 15/05/2015.
 */
public class XMLPullParserUtil {

    private static void testPullParser() {
        //InputStream is = getInputStreamFromUrl("http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom");
        InputStream is = getInputStreamFromFile(new File("F:/david/tmp/all_hour.atom"));

        if (is != null) {

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new InputStreamReader(is));
                int eventType = parser.getEventType();
                int quakeCounter = 0;
                ArrayList<Quake> listQuakes = new ArrayList<com.curso.androidt.earthquake.Quake>();
                Quake currentQuake = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        System.out.println("Start doc");
                    } else if (eventType == XmlPullParser.END_DOCUMENT) {
                        System.out.println("End document");
                    } else if (eventType == XmlPullParser.START_TAG) {
                        System.out.println("Start tag " + parser.getName());
                        if (parser.getName().equalsIgnoreCase("entry")) {
                            quakeCounter++;
                            currentQuake = new Quake();

                        } else if (parser.getName().equalsIgnoreCase("id")) {
                            currentQuake.setId(Long.valueOf(parser.getAttributeValue(null, "id")));
                        } else if (parser.getName().equalsIgnoreCase("title")) {
                            currentQuake.setTitle(parser.getAttributeValue(null, "title"));
                        }

                    } else if (eventType == XmlPullParser.END_TAG) {
                        System.out.println("End tag " + parser.getName());
                    } else if (eventType == XmlPullParser.TEXT) {
                        System.out.println("Text " + parser.getText());
                    }

                    if (currentQuake != null) {
                        listQuakes.add(currentQuake);
                    }
                    eventType = parser.next();
                }

                System.out.println("Total quakes = " + quakeCounter);
                System.out.println("List quakes = " + listQuakes);

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static InputStream getInputStreamFromUrl(String url) {
        InputStream is = null;

        //System.setProperty("http.proxyHost", "webcache.example.com");
        //System.setProperty("http.proxyPort", "8080");

/*
        try {
            is = new URL(url).openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        return is;
    }

    private static FileInputStream getInputStreamFromFile(File file) {
        FileInputStream result = null;

        //System.setProperty("http.proxyHost", "webcache.example.com");
        //System.setProperty("http.proxyPort", "8080");

        try {
            result = new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}


