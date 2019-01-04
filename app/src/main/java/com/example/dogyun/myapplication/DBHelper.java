package com.example.dogyun.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v4.content.IntentCompat;
import android.util.Log;

import com.example.dogyun.myapplication.ViewHolders.NetworkTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by dogyun on 2018-09-06.
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context context;

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        //db.execSQL("CREATE TABLE MENU (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, cost INTEGER);");
        db.execSQL("CREATE TABLE MENU (name TEXT, cost INTEGER, rank INTEGER);");
    }

    public void doFirst(){
        String url = "http://andomira.com/POS_menug.php";
        ContentValues cv = new ContentValues();
        cv.put("cago","PLAN_menu");

        // AsyncTask를 통해 HttpURLConnection 수행.

        NetworkTask networkTask = new NetworkTask(context,url, cv);
        try {
            doJSONParser(networkTask.execute().get());
            //doJSONParser(networkTask.execute().get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        NetworkTask networkTask = new NetworkTask(url, cv);
        networkTask.execute();
        */
    }

    public void doJSONParser(String str){
        String result = "";
        try{
            JSONObject order = new JSONObject(str);
            JSONArray index = order.getJSONArray("menu");
            for (int i = 0; i < index.length(); i++) {
                JSONObject tt = index.getJSONObject(i);
                insert(tt.getString("name"), tt.getInt("cost"), tt.getInt("rank"));
            }
        }
        catch (JSONException e){ ;}
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String name, int cost, int rank) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO MENU VALUES('" + name + "', " + cost + ", " + rank + ");");
        db.close();
    }

    public void update(String name, int cost) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE MENU SET cost=" + cost + " WHERE name='" + name + "';");
        db.close();
    }

    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM MENU WHERE name='" + name + "';");
        db.close();
    }
    public HashMap getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String, String> items = new HashMap<String, String>();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT name, cost FROM MENU ORDER BY rank", null);
        while (cursor.moveToNext()) {
            items.put(cursor.getString(0),String.valueOf(cursor.getInt(1)));
            MainActivity.names.add(cursor.getString(0));
        }

        cursor.close();
        db.close();

        return items;
    }
}