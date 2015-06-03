package com.curso.androidt.earthquake.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by davasens on 5/22/2015.
 */
public class CommonUtils {

    private static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdfDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Calc distance in Meters between 2 poits
     *
     * @param longitudP1
     * @param latitudP1
     * @param longitudP2
     * @param latitudP2
     * @return
     */
    public static Double getDistanceInMeters(Double latitudP1, Double longitudP1, Double latitudP2, Double longitudP2) {

        Double lats = Double.valueOf(latitudP1.doubleValue() - latitudP2.doubleValue());
        Double lngs = Double.valueOf(longitudP1.doubleValue() - longitudP2.doubleValue());

        Double latitudMeters = Double.valueOf(lats.doubleValue() * 60.0D * 1852.0D);
        Double longitudMeters = Double.valueOf(lngs.doubleValue() * Math.cos(latitudP1.doubleValue() * 3.141592653589793D / 180.0D) * 60.0D * 1852.0D);
        Double distInMeters = Double.valueOf(Math.sqrt(Math.pow(latitudMeters.doubleValue(), 2.0D) + Math.pow(longitudMeters.doubleValue(), 2.0D)));

        return distInMeters;
    }

    /**
     * Return String text of a Date
     * @param date
     * @return
     */
    public static String getDateToString(Date date) {
        return sdfDate.format(date);
    }

    /**
     * Return String text of a Date
     * @param date
     * @return
     */
    public static Date getStringToDate(String date) {
        if (date != null) {
            try {
                return sdfDate.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Return String text of a Date
     * @param datetime
     * @return
     */
    public static String getDatetimeToString(Date datetime) {
        return sdfDatetime.format(datetime);
    }

    /**
     * Return String text of a Date
     * @param datetime
     * @return
     */
    public static Date getStringToDatetime(String datetime) {
        if (datetime != null) {
            try {
                return sdfDatetime.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
