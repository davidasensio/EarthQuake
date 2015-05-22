package com.curso.androidt.earthquake;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.curso.androidt.earthquake.util.xml.XmlReader;

import java.io.InputStream;
import java.util.List;

/**
 * Created by davasens on 5/22/2015.
 */
public class QuakeSearchAsyncTask extends AsyncTask<QuakeDto, Void, List<Quake>> {

    private final static String LOG_TAG = QuakeSearchAsyncTask.class.getSimpleName();

    private Context context;
    private ListView listViewQuakes;
    private ProgressDialog progressDialog;

    public QuakeSearchAsyncTask(Context context, ListView listViewQuakes, ProgressDialog progressDialog) {
        this.context = context;
        this.listViewQuakes = listViewQuakes;
        this.progressDialog = progressDialog;
    }

    @Override
    protected List<Quake> doInBackground(QuakeDto... params) {
        //FIXME: Search in database. Temporaly search by url
        String urlRss = context.getResources().getString(R.string.month_4_5);
        InputStream is = XmlReader.getFeedInputStreamFromUrl(urlRss);
        List<Quake> listQuakes = new XmlQuakeParser("feed", "entry").parse(is);
        return listQuakes;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading earthquakes...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(List<Quake> quakes) {
        super.onPostExecute(quakes);

        ListAdapter quakeAdapter = new QuakeAdapter(quakes, R.layout.quake_list_item, context);
        listViewQuakes.setAdapter(quakeAdapter);

        progressDialog.hide();
        Log.w(LOG_TAG, quakes.toString());

    }
}
