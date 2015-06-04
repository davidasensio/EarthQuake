package com.curso.androidt.earthquake.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.curso.androidt.earthquake.Quake;
import com.curso.androidt.earthquake.QuakeDto;
import com.curso.androidt.earthquake.util.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by davasens on 5/22/2015.
 */
public class QuakeDaoImpl implements QuakeDao {

    public static final String TABLE = "QUAKE";
    public static final String FIELD_ID = "ID";
    public static final String FIELD_TITLE = "TITLE";
    public static final String FIELD_LINK = "LINK";
    public static final String FIELD_DATE = "DATE_QUAKE";
    public static final String FIELD_MAGNITUDE = "MAGNITUDE";
    public static final String FIELD_LATITUDE = "LATITUDE";
    public static final String FIELD_LONGITUDE = "LONGITUDE";
    public static final String FIELD_ELEVATION = "ELEVATION";
    public static final String FIELD_PROXIMITY = "PROXIMITY";

    private SQLiteDatabase db;
    private SimpleDateFormat sdf;

    public QuakeDaoImpl(SQLiteDatabase db) {
        this.db = db;
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public List<Quake> find(QuakeDto dto) {
        String where = "";
        ArrayList whereArgs = new ArrayList();
        String[] whereArgsArray = null;
        String orderBy = null;

        if (dto.getMagnitude() != null ) {
            where = where.concat(FIELD_MAGNITUDE.concat(" >=? "));
            whereArgs.add(String.valueOf(dto.getMagnitude()));
        }

        if (dto.getDate() != null) {
            where = where.concat(" AND ").concat(FIELD_DATE.concat(" >= Datetime(?) "));
            whereArgs.add(new SimpleDateFormat("yyyy-MM-dd").format(dto.getDate()));
        }

        if (whereArgs.size() > 0) {
            whereArgsArray = new String[whereArgs.size()];
            whereArgs.toArray(whereArgsArray);
        }

        if (dto.getSort() != null && !dto.getSort().equals(FIELD_PROXIMITY)) {
            orderBy = dto.getSort() + " " + dto.getDir();
        }

        Cursor cursor = db.query(TABLE, null, where, whereArgsArray, null, null, orderBy);

        List<Quake> result = cursorToListQuake(cursor);

        if (dto.getSort() != null && dto.getSort().equals(FIELD_PROXIMITY)) {
            result = orderByProxymity(result, dto.getDir());
        }

        return result;
    }

    private List<Quake> orderByProxymity(List<Quake> listQuakes, final String direction) {
        double lat1 = 39.4623656f;
        double long1 = -0.3659583f;

        for (Quake quake : listQuakes) {

            double lat2 = quake.getLatitude();
            double long2 = quake.getLongitude();
            Double distanceInMeters = CommonUtils.getDistanceInMeters(lat1, long1, lat2, long2);
            quake.setProximity(distanceInMeters.floatValue());
        }

        Collections.sort(listQuakes, new Comparator<Quake>() {
            @Override
            public int compare(Quake lhs, Quake rhs) {
                int result = 0;
                if (lhs.getProximity() > rhs.getProximity()) {
                    result = 1;
                }else if (lhs.getProximity() < rhs.getProximity()) {
                    result = -1;
                } else {
                    result = 0;
                }
                if (direction.equals("DESC")) {
                    result = result * -1;
                }
                return result;
            }
        });

        return listQuakes;
    }

    @Override
    public Quake get(String id) {
        Quake result = null;

        String where = FIELD_ID.concat("=?");
        String[] whereArgs = new String[]{id};

        Cursor cursor = db.query(TABLE, null, where, whereArgs, null, null, null);
        List<Quake> quakes = cursorToListQuake(cursor);
        if (quakes != null && quakes.size() > 0) {
            result = quakes.get(0);
        }
        return result;
    }


    @Override
    public List<Quake> findAll() {
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        return cursorToListQuake(cursor);
    }

    @Override
    public void insert(Quake entity) {
        Quake result = get(entity.getId());
        if (result == null) {
            db.insert(TABLE, null, quakeToContentValues(entity));
        }
    }


    @Override
    public Quake update(Quake entity) {
        String where = FIELD_ID.concat("=?");
        String[] whereArgs = new String[]{entity.getId()};
        db.update(TABLE, quakeToContentValues(entity),where, whereArgs);
        return get(entity.getId());
    }

    @Override
    public void delete(String id) {
        String where = FIELD_ID.concat("=?");
        String[] whereArgs = new String[] {id};
        db.delete(TABLE, where, whereArgs);

    }

    private ContentValues quakeToContentValues(Quake entity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FIELD_ID, entity.getId());
        contentValues.put(FIELD_TITLE, entity.getTitle());
        contentValues.put(FIELD_LINK, entity.getLink());
        if (entity.getDate() != null) {
            contentValues.put(FIELD_DATE, sdf.format(entity.getDate()));
        }else {
            contentValues.putNull(FIELD_DATE);
        }
        contentValues.put(FIELD_MAGNITUDE, entity.getMagnitude());
        contentValues.put(FIELD_LATITUDE, entity.getLatitude());
        contentValues.put(FIELD_LONGITUDE, entity.getLongitude());
        contentValues.put(FIELD_ELEVATION, entity.getElevation());

        return contentValues;
    }

    private List<Quake> cursorToListQuake(Cursor cursor) {
        LinkedList<Quake> result = new LinkedList<>();
        if (cursor.moveToFirst()) {
            do {
                Date quakeDate = null;
                try {
                    quakeDate = sdf.parse(cursor.getString(cursor.getColumnIndex(FIELD_DATE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Quake quake = new Quake(
                        cursor.getString(cursor.getColumnIndex(FIELD_ID))
                        , cursor.getString(cursor.getColumnIndex(FIELD_TITLE))
                        , cursor.getString(cursor.getColumnIndex(FIELD_LINK))
                        , quakeDate
                        , cursor.getFloat(cursor.getColumnIndex(FIELD_MAGNITUDE))
                        , cursor.getFloat(cursor.getColumnIndex(FIELD_LATITUDE))
                        , cursor.getFloat(cursor.getColumnIndex(FIELD_LONGITUDE))
                );
                result.add(quake);

            } while (cursor.moveToNext());
        }

        return result;
    }
}
