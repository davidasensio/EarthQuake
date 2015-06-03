package com.curso.androidt.earthquake;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Url EarthQuakes: http://earthquake.usgs.gov/
 * Rss all_past_hour: http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom
 *
 * TODO:
 * 1. Notifify last significant earthquakes
 * 2. Notifify near earthquakes (defined by user)
 * 3. Clear stored data
 * 4. Change url (day, week, month)
 * 5. Only download with wifi
 * 6. Order by: magnitude, date, proximity
 */
public class SearchActivity extends Activity {

    private static final String LOG_TAG = SearchActivity.class.getName();

    //Components
    Spinner spinnerMagnitude = null;
    EditText txtDate = null;
    DatePicker datePicker = null;
    Button btnPickDate = null;
    Button btnSearch = null;

    //Vars
    Date selectedDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();

        /*
        QuakeSQLiteOpenHelper helper = new QuakeSQLiteOpenHelper(this, "EarthQuake.s3db", null, getResources().getInteger(R.integer.database_version));
        SQLiteDatabase db = helper.getWritableDatabase();
        QuakeDao dao = new QuakeDaoImpl(db);

        db.beginTransaction();

        try {
            dao.insert(new Quake("1", "Uno","fef", new Date(), 3.5f,0.1f,12.1f));
            dao.insert(new Quake("2", "Dos","fdf", new Date(), 5.5f,0.1f,10.1f));
            dao.insert(new Quake("3", "Tres","ffd", new Date(), 2.5f,0.1f,0.1f));
            dao.insert(new Quake("4", "Cuatro","ffa", new Date(), 3.5f,0.1f,11.1f));
            dao.insert(new Quadke("5", "Cinco","ffa", new Date(), 7.5f,10.1f,101.1f));

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        List<Quake> all = dao.findAll();
        Log.d(LOG_TAG, "List of quakes: " + all.toString());
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        //Initialize components
        spinnerMagnitude = (Spinner) findViewById(R.id.spinnerMagnitudFilter);
        txtDate = (EditText) findViewById(R.id.txtDateFilter);
        btnPickDate = ((Button)findViewById(R.id.btnPickDate));
        btnSearch = ((Button)findViewById(R.id.btnSearch));

        //Button Search
        setBtnSearchListener();

        //Set filters
        setMagnitudeFilterListener();
        setDateFilterListener();

        //SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //defaultSharedPreferences.edit().putString(getString(R.string.key_url_rss), "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom").commit();
    }

    private void setBtnSearchListener() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String magnitudeSelected = spinnerMagnitude.getSelectedItem().toString();
                String dateSelected = String.valueOf(txtDate.getText());
                if (dateSelected != null) {
                    try {
                        Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(dateSelected);
                        dateSelected = new SimpleDateFormat("yyyy-MM-dd").format(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


                Intent intent = new Intent(SearchActivity.this, ListQuakeActivity.class);
                intent.putExtra("magnitudeSelected", magnitudeSelected);
                intent.putExtra("dateSelected", dateSelected);

                startActivity(intent);
            }
        });
    }

    private void setDateFilterListener() {

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Date button pressed");

                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setTxtDate(year, monthOfYear, dayOfMonth);
                    }
                });
                datePickerDialogFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    public void setTxtDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        this.selectedDate = calendar.getTime();

        String strSelectedDate =  new SimpleDateFormat("dd-MM-yyyy").format(this.selectedDate);
        txtDate.setText(strSelectedDate);
    }


    private void setMagnitudeFilterListener() {
        spinnerMagnitude.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Object item = ((TextView) view).getAdapter().getItem(position);
                Log.d(LOG_TAG, "Item selected: " + position + "Row id: " + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
