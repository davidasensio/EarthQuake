package com.curso.androidt.earthquake.util.xml;

import android.util.Xml;

import com.curso.androidt.earthquake.Quake;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by davasens on 5/16/2015.
 */
public class XmlParser {

    private String ns = null;
    private String feedTagName = null;
    private String entryTagName = null;

    public XmlParser(String feedTagName, String entryTagName) {
        this.feedTagName = feedTagName;
        this.entryTagName = entryTagName;

        //Without namespaces by default
        this.ns = null;
    }

    public List parse(InputStream is) {
        List result = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.next();
            result = readFeed(parser);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG,ns,"feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(getEntryTagName())) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private Quake readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,ns, getEntryTagName());
        Quake quake = new Quake();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            if (parser.getName().equals("id")) {
                String strId = readTag(parser, "id");
                Matcher m = Pattern.compile("\\d+").matcher(strId);
                if (m.find()) {
                    quake.setId(Long.valueOf(m.group()));

                }
            }else if (parser.getName().equals("title")) {
                quake.setTitle(readTag(parser, "title"));
            }else if (parser.getName().equals("link")) {
                quake.setLink(readTag(parser, "link"));
            }else if (parser.getName().equals("updated")) {
                try {
                    String strDate = readTag(parser, "updated");
                    Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(strDate);
                    quake.setDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else if (parser.getName().equals("point")) {
                quake.setLink(readTag(parser, "point"));
            }else {
                skip(parser);
            }
        }
        return quake;
    }

    private String readTag(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG,ns, tagName);
        String value = "";
        if (parser.next() == XmlPullParser.TEXT) {
            value = parser.getText();
            parser.next();
        }
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return value;
    }

    //Getters & setters
    public String getEntryTagName() {
        return entryTagName;
    }

    public void setEntryTagName(String entryTagName) {
        this.entryTagName = entryTagName;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getFeedTagName() {
        return feedTagName;
    }

    public void setFeedTagName(String feedTagName) {
        this.feedTagName = feedTagName;
    }
}
