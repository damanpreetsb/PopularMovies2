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

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "movies";
    private static final String TABLE_MOVIES = "moviestable";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_VOTE = "vote";
    private static final String KEY_DATE = "date";
    private static final String KEY_OVERVIEW = "overview";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MOVIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_VOTE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_OVERVIEW + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        // Create tables again
        onCreate(db);
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
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
}
