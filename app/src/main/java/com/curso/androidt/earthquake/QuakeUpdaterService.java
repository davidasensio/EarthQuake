package com.curso.androidt.earthquake;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.curso.androidt.earthquake.dao.QuakeDao;
import com.curso.androidt.earthquake.dao.QuakeDaoImpl;
import com.curso.androidt.earthquake.util.xml.XmlReader;

import java.io.InputStream;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class QuakeUpdaterService extends IntentService {
    private final static String LOG_TAG = QuakeUpdaterService.class.getSimpleName();
    private static final int QUAKE_NOTIFICATION_ID = 1;

    private QuakeSQLiteOpenHelper quakeSQLiteOpenHelper = null;
    private SQLiteDatabase db = null;
    private QuakeDao quakeDao = null;


    public QuakeUpdaterService() {
        super("QuakeUpdaterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Read preferences
        String urlRss = defaultSharedPreferences.getString(getString(R.string.key_url_rss), getResources().getString(R.string.hour_all));
        String frequencyStr =  defaultSharedPreferences.getString(getString(R.string.key_frequency), "0");
        Long frequency = Long.valueOf(frequencyStr);
        boolean checkIfWifiConnected = defaultSharedPreferences.getBoolean(getString(R.string.key_chk_wifi), false);

        //If WIFI check selected --> if wifi is disconnected return
        if (checkIfWifiConnected) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null && !networkInfo.isConnected()) {
                Log.d(LOG_TAG, "Skip earthquakes update from rss cause WIFI is not connected");
                return;
            }
        }

        //Notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);

        builder.setTicker("Progress")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setAutoCancel(true)
                .setContentTitle("Retrieving earthquakes...")
                .setProgress(100, 0, false);

        notificationManager.notify(QUAKE_NOTIFICATION_ID, builder.build());

        try {
            InputStream is = XmlReader.getFeedInputStreamFromUrl(urlRss);
            XmlQuakeParser xmlQuakeParser = new XmlQuakeParser("feed", "entry");
            List<Quake> listQuakes = xmlQuakeParser.parse(is);
            int totalQuakes = listQuakes.size();
            int counter = 0;

            if (totalQuakes > 0) {
                for (Quake quake: listQuakes) {

                    persistQuake(quake);

                    counter++;
                    int progress = (counter * 100 / totalQuakes);
                    builder.setProgress(100,progress,false);
                    notificationManager.notify(QUAKE_NOTIFICATION_ID, builder.build());
                }
            }

            Intent intentSearchActivity = new Intent(this, SearchActivity.class);
            intentSearchActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntentSearchActivity = PendingIntent.getActivity(this, 1, intentSearchActivity,PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentText("Finished")
                    .setProgress(100,100,false)
                    .setContentIntent(pendingIntentSearchActivity);
            notificationManager.notify(QUAKE_NOTIFICATION_ID, builder.build());


        }catch (Exception e) {
            e.printStackTrace();

            Notification.Builder builderError = new Notification.Builder(this);

            builderError.setTicker("Error")
                    .setSmallIcon(android.R.drawable.stat_notify_error)
                    .setAutoCancel(true)
                    .setContentText(e.getMessage());

            Notification notificationError = builderError.build();
            notificationManager.notify(QUAKE_NOTIFICATION_ID, notificationError);
        }


    }

    private void initBBDD() {
        QuakeSQLiteOpenHelper quakeSQLiteOpenHelper = new QuakeSQLiteOpenHelper(this, getResources().getString(R.string.database_name), null, getResources().getInteger(R.integer.database_version));

        try {
            db = quakeSQLiteOpenHelper.getWritableDatabase();
        }catch (Exception e) {
            db = quakeSQLiteOpenHelper.getReadableDatabase();
        }
        quakeDao = new QuakeDaoImpl(this, db);
    }

    private void persistQuake(Quake quake) {
        if (quakeDao == null) {
            initBBDD();
        }

        db.beginTransaction();
        try {
            quakeDao.insert(quake);
            db.setTransactionSuccessful();
            db.endTransaction();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
