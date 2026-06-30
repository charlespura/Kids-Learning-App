package com.example.kidslearningapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB";
    private static final String TABLE_NAME = "users";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                "score INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_NAME + ")", null);
            boolean columnExists = false;
            int nameIndex = cursor.getColumnIndex("name");

            if (nameIndex != -1) {
                while (cursor.moveToNext()) {
                    String columnName = cursor.getString(nameIndex);
                    if ("score".equals(columnName)) {
                        columnExists = true;
                        break;
                    }
                }
            }
            cursor.close();

            if (!columnExists) {
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN score INTEGER DEFAULT 0");
            }
        }

        // Add additional upgrade logic here if needed
    }


    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1; // Returns true if insert is successful
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?", new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Get score for a user
    public int getUserScore(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT score FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + "=?", new String[]{username});
        int score = 0;
        if (cursor.moveToFirst()) {
            score = cursor.getInt(0);
        }
        cursor.close();
        return score;
    }


    // Update score for a user
    public void updateUserScore(String username, int additionalPoints) {
        if (username == null || username.isEmpty()) {
            Log.e("DatabaseHelper", "Username is null or empty");
            return;
        }

        int currentScore = getUserScore(username);
        int newScore = currentScore + additionalPoints;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", newScore);
        db.update(TABLE_NAME, values, COL_USERNAME + "=?", new String[]{username});
    }
}
