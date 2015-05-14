package com.curso.androidt.earthquake;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Date;
import java.util.LinkedList;


public class ListQuakeActivity extends Activity {
    ListView listViewQuakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_quake);

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
        if (id == R.id.action_settings) {
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

                break;
            case R.id.action_map:

                break;
        }
        return super.onContextItemSelected(item);
    }

    private void init() {


        LinkedList<Quake> listQuakes = new LinkedList<>();
        //dummy data
        listQuakes.add(new Quake("Quake 1", "http://earthquake.usgs.gov/earthquakes/eventpage/nn00494300#general_map", 3.5f, new Date(), 0f,0f));
        listQuakes.add(new Quake("Quake 2", "http://earthquake.usgs.gov/earthquakes/eventpage/nn00494300#general_map", 3.5f, new Date(), 0f,0f));
        listQuakes.add(new Quake("Quake 3", "http://earthquake.usgs.gov/earthquakes/eventpage/nn00494300#general_map", 3.5f, new Date(), 0f,0f));
        listQuakes.add(new Quake("Quake 4", "http://earthquake.usgs.gov/earthquakes/eventpage/nn00494300#general_map", 3.5f, new Date(), 0f,0f));

        listViewQuakes = (ListView) findViewById(R.id.listViewQuakes);
        ListAdapter quakeAdapter = new QuakeAdapter(listQuakes, R.layout.quake_list_item, this);
        listViewQuakes.setAdapter(quakeAdapter);

        //Register context menu
        registerForContextMenu(listViewQuakes);

    }


}
