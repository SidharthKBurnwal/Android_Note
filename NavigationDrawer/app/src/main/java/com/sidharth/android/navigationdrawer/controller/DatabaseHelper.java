package com.sidharth.android.navigationdrawer.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sidharth.android.navigationdrawer.model.Note;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper{

    //database version
    private static final int Database_Version = 2;

    //database name
    private static final String database_name = "note1.db";

    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, database_name, null, Database_Version);
    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
        onCreate(db);
    }

    //insert a note
    public long insertNote(String title, String content){
        Log.d("inside insert method ", " hiiii ");
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Note.COLUMN_TITLE,title);
        values.put(Note.COLUMN_CONTENT,content);

        long count = db.insert(Note.TABLE_NAME,null,values);
        db.close();
        return count;
    }

    //retrive the note list
    public List<Note> getAllNotes(){
         List<Note> notes = new ArrayList<>();

         //select all query
        String selectQuery = "SELECT * FROM " + Note.TABLE_NAME ;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(cursor.getColumnIndex(Note.COLUMN_ID));
                Log.d("hiiiiiiiiiiiii count", note.getId()+"");

                note.setTitle(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(Note.COLUMN_CONTENT)));
                notes.add(note);
            }while (cursor.moveToNext());
        }
        db.close();
        return notes;
    }

    public int update(String title,String content){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Note.COLUMN_TITLE,n.getTitle());
        values.put(Note.COLUMN_CONTENT,content);
        int i = db.update(Note.TABLE_NAME,values,Note.COLUMN_TITLE + " = " + title,null);
        return i;
    }

    public void delete(Note note){
        db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_TITLE + " = " + note.getTitle(),null);

    }




}
