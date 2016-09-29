package com.singh.daman.popularmovies2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.singh.daman.popularmovies2.Model.Movies;

import java.util.ArrayList;

/**
 * Created by daman on 27/9/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "movies";
    private static final String TABLE_MOVIES = "moviestable";
    private static final String TABLE_FAVS = "favstable";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_VOTE = "vote";
    private static final String KEY_DATE = "date";
    private static final String KEY_OVERVIEW = "overview";
    private static final String CREATE_MOVIES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MOVIES + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
            + KEY_IMAGE + " TEXT,"
            + KEY_VOTE + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_OVERVIEW + " TEXT" + ")";
    private static final String CREATE_FAV_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
            + KEY_IMAGE + " TEXT,"
            + KEY_VOTE + " TEXT,"
            + KEY_DATE + " TEXT,"
            + KEY_OVERVIEW + " TEXT" + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIES_TABLE);
        db.execSQL(CREATE_FAV_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVS);
        // Create tables again
        onCreate(db);
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public void addFavs(String id, String title, String image, String vote, String date, String overview){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_TITLE, title);
        values.put(KEY_IMAGE, image);
        values.put(KEY_VOTE, vote);
        values.put(KEY_DATE, date);
        values.put(KEY_OVERVIEW, overview);

        db.insert(TABLE_FAVS, null, values);
        db.close();
    }


    public void addMovies(Movies movies){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, movies.getId());
            values.put(KEY_TITLE, movies.getTitle());
            values.put(KEY_IMAGE, movies.getImage());
            values.put(KEY_VOTE, movies.getVote());
            values.put(KEY_DATE, movies.getDate());
            values.put(KEY_OVERVIEW, movies.getOverview());
            // Inserting Row
            db.insert(TABLE_MOVIES, null, values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Movies> getAllFavs() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Movies> movieslist = null;
        try{
            movieslist = new ArrayList<>();
            String QUERY = "SELECT * FROM "+TABLE_FAVS;
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    Movies movies = new Movies();
                    movies.setId(cursor.getString(0));
                    movies.setTitle(cursor.getString(1));
                    movies.setImage(cursor.getString(2));
                    movies.setVote(cursor.getString(3));
                    movies.setDate(cursor.getString(4));
                    movies.setOverview(cursor.getString(5));
                    movieslist.add(movies);
                }
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return movieslist;
    }

    public ArrayList<Movies> getAllMovies() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Movies> movieslist = null;
        try{
            movieslist = new ArrayList<>();
            String QUERY = "SELECT * FROM "+TABLE_MOVIES;
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    Movies movies = new Movies();
                    movies.setId(cursor.getString(0));
                    movies.setTitle(cursor.getString(1));
                    movies.setImage(cursor.getString(2));
                    movies.setVote(cursor.getString(3));
                    movies.setDate(cursor.getString(4));
                    movies.setOverview(cursor.getString(5));
                    movieslist.add(movies);
                }
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return movieslist;
    }

    public void deleteFav(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_FAVS+" where "+KEY_ID+"='"+id+"'");
    }


    public boolean CheckIsFAv(String fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_FAVS + " where " + KEY_ID + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
