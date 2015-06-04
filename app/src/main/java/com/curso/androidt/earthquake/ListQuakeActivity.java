package com.curso.androidt.earthquake;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.curso.androidt.earthquake.dao.QuakeDao;
import com.curso.androidt.earthquake.dao.QuakeDaoImpl;
import com.curso.androidt.earthquake.util.CommonUtils;
import com.curso.androidt.earthquake.util.xml.XmlReader;

import java.io.InputStream;
import java.util.Date;
import java.util.List;


public class ListQuakeActivity extends Activity {

    private static final String LOG_TAG = ListQuakeActivity.class.getName();

    private static final String URL_EARTH_QUAKES_ALL_HOUR = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom";
    private static final String URL_EARTH_QUAKES_ALL_DAY = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.atom";
    private static final String URL_EARTH_QUAKES_M4_HOUR = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/m4_day.atom";

    private ListView listViewQuakes;
    private boolean useUrl = true;


    //Filters
    Float magnitude = 0f;
    Date date;

    //BBDD
    QuakeSQLiteOpenHelper helper;
    SQLiteDatabase db;
    QuakeDao dao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_quake);



        date = CommonUtils.getStringToDate(getIntent().getExtras().getString("dateSelected"));
        String magnitudeStr = getIntent().getExtras().getString("magnitudeSelected");
        if ("Significant".equals(magnitudeStr)) {
            magnitude = 5.5f;
        } else if ("Magnitude 4.5+".equals(magnitudeStr)) {
            magnitude = 4.5f;
        } else if ("Magnitude 2.5+".equals(magnitudeStr)) {
            magnitude = 2.5f;
        } else if ("Magnitude 1.0+".equals(magnitudeStr)) {
            magnitude = 1f;
        }  else if ("All Earthquakes".equals(magnitudeStr)) {
            magnitude = 0f;
        }


        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_quake, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_order) {
            selectOrder();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //Context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listViewQuakes) {
                getMenuInflater().inflate(R.menu.menu_context_quake, menu);

            int position = ((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            Quake quake = (Quake) listViewQuakes.getAdapter().getItem(position);
            menu.setHeaderTitle(quake.getTitle());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case (R.id.action_detail):
                Intent intentDetail = new Intent(this, QuakeDetailActivity.class);
                startActivity(intentDetail);

                break;
            case R.id.action_map:
                int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                Quake quake = (Quake) listViewQuakes.getAdapter().getItem(position);

                double latitude = quake.getLatitude();
                double longitude = quake.getLongitude();
                String label = "Earthquake";
                String uriBegin = "geo:" + longitude + "," + latitude;
                String query =  longitude+ "," + latitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=15";
                Uri uri = Uri.parse(uriString);
                Intent intentMap = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intentMap);


                break;
        }
        return super.onContextItemSelected(item);
    }

    private void init() {

        listViewQuakes = (ListView) findViewById(R.id.listViewQuakes);

        helper = new QuakeSQLiteOpenHelper(this, getString(R.string.database_name), null, getResources().getInteger(R.integer.database_version));
        db = helper.getWritableDatabase();
        dao = new QuakeDaoImpl(db);

        doSearch(null); //Default search

        //Register context menu
        registerForContextMenu(listViewQuakes);

    }

    private void doSearch(QuakeDto quakeDto) {
        //Filter DTO
        if (quakeDto == null) {
            quakeDto = new QuakeDto();
            //new QuakeSearchAsyncTask(this, listViewQuakes, new ProgressDialog(this)).execute(quakeDto); //Comment
            quakeDto.setMagnitude(Float.valueOf(magnitude));
            quakeDto.setDate(date);
            quakeDto.setSort(QuakeDaoImpl.FIELD_DATE);
            quakeDto.setDir("DESC");
        }

        //read from BBDD
        List<Quake> listQuakes = dao.find(quakeDto);

        setQuakeAdapter(listQuakes);
    }

    /*
    private void old_init() {
    listViewQuakes = (ListView) findViewById(R.id.listViewQuakes);

        if (!useUrl) {
            //dummy data
            //listQuakes.add(new Quake("Quake 1", "http://earthquake.usgs.gov/earthquakes/eventpage/nn00494300#general_map", 3.5f, new Date(), 0f,0f));
            //listQuakes.add(new Quake("Quake 2", "http://earthquake.usgs.gov/earthquakes/eventpage/nn00494300#general_map", 3.5f, new Date(), 0f,0f));
            //listQuakes.add(new Quake("Quake 3", "http://earthquake.usgs.gov/earthquakes/eventpage/nn00494300#general_map", 3.5f, new Date(), 0f,0f));
            //listQuakes.add(new Quake("Quake 4", "http://earthquake.usgs.gov/earthquakes/eventpage/nn00494300#general_map", 3.5f, new Date(), 0f,0f));

            List<Quake> listQuakes = getListQuakesFromAssets();
            setQuakeAdapter(listQuakes);
        } else {
            //Do it async
            QuakeDto quakeDto = new QuakeDto();
            new QuakeSearchAsyncTask(this, listViewQuakes, new ProgressDialog(this)).execute(quakeDto);
        }

        //Register context menu
        registerForContextMenu(listViewQuakes);
    }
    */

    private void setQuakeAdapter(List<Quake> listQuakes) {
        ListAdapter quakeAdapter = new QuakeAdapter(listQuakes, R.layout.quake_list_item, this);
        listViewQuakes.setAdapter(quakeAdapter);
    }

    public List<Quake> getListQuakesFromAssets() {
        XmlQuakeParser xmlQuakeParser = new XmlQuakeParser("fedd", "entry");
        InputStream is = XmlReader.getFeedInputStreamFromAssets(this, "sample.xml");
        List<Quake> listQuakes = xmlQuakeParser.parse(is);
        Log.w(LOG_TAG, listQuakes.toString());
        return listQuakes;
    }

    private void selectOrder() {
        final ArrayAdapter<String> orderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.order_entries));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.arrow_down_float)
                .setTitle("Select order")
                .setAdapter(orderAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = orderAdapter.getItem(which);
                        reorder(item);
                    }
                });

        builder.show();

    }

    private void reorder(String item) {
        Log.d(LOG_TAG, "Item selected: " + item);

        QuakeDto quakeDto = new QuakeDto();
        quakeDto.setMagnitude(Float.valueOf(magnitude));
        quakeDto.setDate(date);

        if (item.contains("DESC")) {
            quakeDto.setDir("DESC");
        }else {
            quakeDto.setDir("ASC");
        }

        if (item.toUpperCase().contains("DATE")) {
            quakeDto.setSort(QuakeDaoImpl.FIELD_DATE);
        } else if (item.toUpperCase().contains("MAGNITUDE")) {
            quakeDto.setSort(QuakeDaoImpl.FIELD_MAGNITUDE);
        }else if (item.toUpperCase().contains("PROXIMITY")) {
            quakeDto.setSort(QuakeDaoImpl.FIELD_PROXIMITY);
        }

        doSearch(quakeDto);
    }

}
