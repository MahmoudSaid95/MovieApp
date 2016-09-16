package com.example.android.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.android.movieapp.onlineData.Result;

import java.util.ArrayList;

/**
 * Created by M.Sa3ed on 9/15/2016.
 */
public class DatabaseAdapter {

    FavoriteHelper favoriteHelper;
    Context context;
    static boolean _id = false;
    public DatabaseAdapter(Context context) {
        favoriteHelper = new FavoriteHelper(context);
        this.context = context;
    }

    public long insertData(String poster_id, String POSTER, String TITEL, String DATE, String VOTE, String OVERVIEW) {
        SQLiteDatabase sqLiteDatabase = favoriteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteHelper.POSTER, POSTER);
        contentValues.put(FavoriteHelper.TITEL, TITEL);
        contentValues.put(FavoriteHelper.DATE, DATE);
        contentValues.put(FavoriteHelper.VOTE, VOTE);
        contentValues.put(FavoriteHelper.OVERVIEW, OVERVIEW);
        contentValues.put(FavoriteHelper.POSTER_ID, poster_id);

        long id = sqLiteDatabase.insert(FavoriteHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public ArrayList<Result> getAllData() {
        SQLiteDatabase db = favoriteHelper.getWritableDatabase();
        String[] columns = {FavoriteHelper.POSTER_ID, FavoriteHelper.POSTER, FavoriteHelper.TITEL, FavoriteHelper.DATE, FavoriteHelper.VOTE, FavoriteHelper.OVERVIEW};
        Cursor cursor = db.query(FavoriteHelper.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<Result> movieData = new ArrayList<Result>();
        while (cursor.moveToNext()) {
            String POSTER = cursor.getString(1);
            String TITEL = cursor.getString(2);
            String DATE = cursor.getString(3);
            String VOTE = cursor.getString(4);
            String OVERVIE = cursor.getString(5);
            String poster_id = cursor.getString(0);
            Result Data = new Result();
            Data.setPosterPath(POSTER);
            Data.setReleaseDate(DATE);
            Data.setVoteAverage(Double.parseDouble(VOTE));
            Data.setTitle(TITEL);
            Data.setOverview(OVERVIE);
            Data.setId(Integer.parseInt(poster_id));
            movieData.add(Data);
        }
        return movieData;
    }

    public void getData(String p_Id) {
        SQLiteDatabase db = favoriteHelper.getWritableDatabase();
        String[] columns = {FavoriteHelper.POSTER_ID, FavoriteHelper.POSTER, FavoriteHelper.TITEL, FavoriteHelper.DATE, FavoriteHelper.VOTE, FavoriteHelper.OVERVIEW};
        Cursor cursor = db.query(FavoriteHelper.TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {

            if(p_Id.equals(cursor.getString(0))){
                _id = true;
                return ;
            }
            _id = false;
        }

    }

    static class FavoriteHelper extends SQLiteOpenHelper {
        private final static String DATABASE_NAME = "favoritetabase";
        private final static int DATABASE_VERSION = 3;
        private final static String TABLE_NAME = "movietable";
        private final static String UID = "_id";
        private final static String POSTER = "poster";
        private final static String TITEL = "titel";
        private final static String DATE = "date";
        private final static String VOTE = "vote";
        private final static String OVERVIEW = "overview";
        private final static String POSTER_ID = "POSTER_ID";
        private final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + UID + " VARCHAR(255) PRIMARY KEY ," + POSTER + " VARCHAR(255) ," + TITEL + " VARCHAR(255) ," + DATE + " VARCHAR(255) ," + VOTE + " VARCHAR(255) ," + OVERVIEW + " VARCHAR(255) ," + POSTER_ID + " INTEGER);";
        private final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public FavoriteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
                Toast.makeText(context, "database CREATED", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(context, "Sorry there is Error in create your database \n" + e, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(DROP_TABLE);
                onCreate(sqLiteDatabase);
            } catch (SQLException e) {
                Toast.makeText(context, "Sorry there is Error in upgrade your database \n" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}