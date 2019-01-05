package com.example.dogyun.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dogyun.myapplication.Bill.GetBill;
import com.example.dogyun.myapplication.Models.BillType;
import com.example.dogyun.myapplication.Models.Category;
import com.example.dogyun.myapplication.Models.itemList;
import com.example.dogyun.myapplication.Networks.NetworkTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        db.execSQL("CREATE TABLE MENU (idx INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER," +
                " category INTEGER REFERENCES CATEGORY (idx) on delete restrict on update cascade);");
        db.execSQL("CREATE TABLE BILL (idx INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, name TEXT, count TEXT, price TEXT, total INTEGER, type TEXT);");
        db.execSQL("CREATE TABLE CATEGORY (idx INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
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
                insertMenu(tt.getString("name"), tt.getInt("cost"), tt.getInt("rank"));
            }
        }
        catch (JSONException e){ ;}
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertMenu(String name, int price, int category) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO MENU(name, price, category) VALUES('" + name + "', " + price + ", " + category + ");");
        db.close();
    }

    public void insertBill(String date, String name, String count, String price, int total, String type) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO BILL(date, name, count, price, total, type) VALUES('" + date + "', '"+ name + "', '" + count + "', '" + price + "', " +
        total + ", '" + type + "');");
        db.close();
    }

    public void insertCategory(String category) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO CATEGORY(name) VALUES('" + category + "');");
        db.close();
    }

    public void updateMenu(String name, int price, int category) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE MENU SET price=" + price + ", categoty=" + category + " WHERE name='" + name + "';");
        db.close();
    }

    public void refundBill(String where, String type) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE BILL SET type='" + type + "_환불' WHERE date='" + where + "';");
        db.close();
    }

    public void deleteMenu(String name) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM MENU WHERE name='" + name + "';");
        db.close();
    }

    public void getMenu(ArrayList<itemList> menu) {
        menu.clear();

        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM MENU ORDER BY category, idx", null);
        while (cursor.moveToNext()) {
            itemList il = new itemList();
            il.name = cursor.getString(1);
            il.price = cursor.getInt(2);
            il.category = cursor.getInt(3);
            menu.add(il);
        }

        cursor.close();
        db.close();
    }

    public void getBill(ArrayList<BillType> bill) {
        bill.clear();

        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM BILL ORDER BY idx ASC", null);
        while (cursor.moveToNext()) {
            BillType bt = new BillType();
            bt.date = cursor.getString(1);
            bt.name = parserString(cursor.getString(2));
            bt.count = parserString(cursor.getString(3));
            bt.price = parserString(cursor.getString(4));
            bt.total = cursor.getInt(5);
            bt.type = cursor.getString(6);
            bill.add(bt);
        }

        cursor.close();
        db.close();
    }

    public ArrayList<String> parserString(String str){
        ArrayList<String> as = new ArrayList<String>();
        String[] array = str.substring(1, str.length()-1).split(", ");
        for(int i=0;i<array.length; i++) {
            as.add(array[i]);
        }
        return as;
    }

    public void getCategoy(ArrayList<Category> category) {
        category.clear();

        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORY ORDER BY idx", null);
        while (cursor.moveToNext()) {
            Category c = new Category();
            c.idx = cursor.getInt(0);
            c.name = cursor.getString(1);
            category.add(c);
        }

        cursor.close();
        db.close();
    }
}