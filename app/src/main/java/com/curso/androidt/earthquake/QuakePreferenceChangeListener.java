package com.curso.androidt.earthquake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by davasens on 5/29/2015.
 */
public class QuakePreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String LOG_TAG = QuakePreferenceChangeListener.class.getSimpleName();
    private final int START_SERVICE_REQUEST_CODE = 1;

    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    public QuakePreferenceChangeListener(Context context) {
        this.context = context;

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getService(context, START_SERVICE_REQUEST_CODE, new Intent(context, QuakeUpdaterService.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        if (key.equals(context.getResources().getString(R.string.key_url_rss))) {
            //No action is needed
            Log.d(LOG_TAG, "Url rss feed changed to: " + sharedPreferences.getString(key, ""));
        }

        if (key.equals(context.getResources().getString(R.string.key_frequency))) {
            String value = sharedPreferences.getString(context.getResources().getString(R.string.key_frequency), "0");
            Log.d(LOG_TAG, "Frequency preference changed to: " + value);

            Long frequencyValue = Long.valueOf(value);

            //Cancel updates
            alarmManager.cancel(pendingIntent);

            //Program updates
            if (frequencyValue != 0) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, frequencyValue * 60 * 1000, pendingIntent);
                Log.w(LOG_TAG, "Update Service preference setted in " + frequencyValue + " minute(s)");
            }
        }

        if (key.equals(context.getResources().getString(R.string.key_chk_wifi))) {
            //No action is needed
            Log.d(LOG_TAG, "Check WIFI preference changed to: " + sharedPreferences.getBoolean(key, false));
        }
    }
}
