package com.curso.androidt.earthquake;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by davasens on 5/22/2015.
 */
public class QuakeSQLiteOpenHelper extends SQLiteOpenHelper {

    private Context context;

    public QuakeSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        execScript(db, R.array.script_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i=oldVersion + 1;i<=newVersion;i++) {
            switch (i) {
                case 1:
                    break;
                case 2:
                    execScript(db, R.array.script_upgrade_1_2);
                    break;
            }
        }
    }

    public void execScript(SQLiteDatabase db, int resourceScript) {
        String[] queries = context.getResources().getStringArray(resourceScript);

        db.beginTransaction();
        try {
            for (String query : queries) {
                db.execSQL(query);
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }
}
