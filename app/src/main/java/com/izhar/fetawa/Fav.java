package com.izhar.fetawa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class Fav extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db";
    public static final String TABLE_NAME = "favorite";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER = "answer";

    public Fav(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table favorite"
                        + "(id text primary key, question text, answer text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favorite");
        onCreate(db);
    }

    public long insert_new_fav(Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", answer.getId());
        contentValues.put("question", answer.getQuestion());
        contentValues.put("answer", answer.getAnswer());
        long l = db.insert("favorite", null, contentValues);
        return l;
    }

    public Cursor getFav(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select * from favorite where id=" + id + "", null);
        return result;
    }

    public Integer deleteFav(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("favorite", "id = ? ", new String[]{id});
    }

    public ArrayList<Answer> getAllFav()
    {
        ArrayList<Answer> array_list = new ArrayList();//hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from favorite", null );
        res.moveToFirst();
        while(!res.isAfterLast()){
            Answer answer = new Answer(res.getString(res.getColumnIndex(COLUMN_ID)), res.getString(res.getColumnIndex(COLUMN_QUESTION)),res.getString(res.getColumnIndex(COLUMN_ANSWER)));
            array_list.add(answer);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getIds(){
        ArrayList<String> ids = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from favorite", null );
        res.moveToFirst();
        while(!res.isAfterLast()){
            ids.add(res.getString(0));
            res.moveToNext();
        }
        return ids;
    }
}
