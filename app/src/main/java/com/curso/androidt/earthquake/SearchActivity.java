package com.curso.androidt.earthquake;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Url EarthQuakes: http://earthquake.usgs.gov/
 * Rss all_past_hour: http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom
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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, ListQuakeActivity.class);
                startActivity(intent);
            }
        });

        //Set filters
        setMagnitudeFilterListener();
        setDateFilterListener();
    }

    private void setDateFilterListener() {

        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Date button pressed");

                DialogFragment dialogFragment = new DatePickerDialogFragment();
                dialogFragment.show(getFragmentManager(), "datePicker");


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

    /*
    public static class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =  new DatePickerDialog(getActivity(), this, year,month,day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ((SearchActivity)getActivity()).setTxtDate(year, monthOfYear, dayOfMonth);
        }
    }
    */
}
