package com.curso.androidt.earthquake.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.curso.androidt.earthquake.Quake;
import com.curso.androidt.earthquake.QuakeDto;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by davasens on 5/22/2015.
 */
public class QuakeDaoImpl implements QuakeDao {

    public static final String TABLE = "QUAKE";
    public static final String FILED_ID = "ID";
    public static final String FILED_TITLE = "TITLE";
    public static final String FILED_LINK = "LINK";
    public static final String FILED_DATE = "DATE";
    public static final String FILED_MAGNITUDE = "MAGNITUDE";
    public static final String FILED_LATITUDE = "LATITUDE";
    public static final String FILED_LONGITUDE = "LONGITUDE";
    public static final String FILED_ELEVATION = "ELEVATION";

    private SQLiteDatabase db;
    private SimpleDateFormat sdf;

    public QuakeDaoImpl(SQLiteDatabase db) {
        this.db = db;
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public List<Quake> find(QuakeDto dto) {

        return null;
    }

    @Override
    public Quake get(String id) {

        String where = FILED_ID.concat(" = ?");
        String[] whereArgs = new String[]{id};

        Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null, null);
        return cursorToQuake(cursor);
    }



    @Override
    public List<Quake> findAll() {
        return null;
    }

    @Override
    public Quake insert(Quake entity) {
        db.insert(TABLE, null, quakeToContentValues(entity));
        return null;
    }


    @Override
    public Quake update(Quake entity) {

        return null;
    }

    @Override
    public void delete() {

    }

    private ContentValues quakeToContentValues(Quake entity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FILED_ID, entity.getId());
        contentValues.put(FILED_TITLE, entity.getTitle());
        contentValues.put(FILED_LINK, entity.getLink());
        contentValues.put(FILED_DATE, sdf.format(entity.getDate()));
        contentValues.put(FILED_MAGNITUDE, entity.getMagnitude());
        contentValues.put(FILED_LATITUDE, entity.getLatitude());
        contentValues.put(FILED_LONGITUDE, entity.getLongitude());
        contentValues.put(FILED_ELEVATION, entity.getElevation());

        return contentValues;
    }

    private List<Quake> cursorToListQuake(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                Quake quake = new Quake(

                );

            }while(cursor.moveToNext());
        }
    }
}
