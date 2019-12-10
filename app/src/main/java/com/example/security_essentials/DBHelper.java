package com.example.security_essentials;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;


public class DBHelper extends SQLiteOpenHelper  {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "databaseName.db";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_UUID = "uuid";
    public static final String TABLE_NAME = "user";
    public static final DBHelper.Companion Companion = new DBHelper.Companion(null);

     public DBHelper(Context context) {
          super(context, "databaseName.db", null,1);
     }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE security (" +
                      "ID INTEGER PRIMARY KEY," +
                      "NAME VARCHAR, " +
                      "EMAIL VARCHAR, " +
                      "UUID VARCHAR);"
        );
   }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS security");
        onCreate(db);
    }

    public void insertRow(String name, String email, String uuid) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("uuid", uuid);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("security", null, values);
        db.close();
    }

    public void updateRow(String name, String email, String uuid){
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("uuid", uuid);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("security", null, values);
        db.close();
    }

    public void deleteRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("security", "ID = ?", new String[]{row_id});
        db.close();
    }

    public final Cursor getAllRow() {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("Select * From security", null);
    }

    public static final class Companion {
        private Companion(Object o) {

        }
    }


    public int describeContents() {
        return 0;
    }
/*
    public static final class CREATOR implements Parcelable.Creator {

        public DBHelper createFromParcel(Parcel parcel) {
            checkParameterIsNotNull(parcel, "parcel");
            return new DBHelper(parcel);
       }

        private void checkParameterIsNotNull(Parcel parcel, String parcel1) {
        }


        public DBHelper[] newArray(int size) {
            return new DBHelper[size];
        }
    }*/


}
