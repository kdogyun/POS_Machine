package com.example.dogyun.myapplication.ViewHolders;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.dogyun.myapplication.RequestHttpURLConnection;

/**
 * Created by dogyun on 2018-09-27.
 */

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private ContentValues values;
    private ProgressDialog dialog;
    private Context c;

    public NetworkTask(Context c, String url, ContentValues values) {
        this.c = c;
        this.url = url;
        this.values = values;
    }

    protected void onPreExecute() {
        dialog = new ProgressDialog(c);
        dialog.setTitle("DB 접근중");
        dialog.setMessage("잠시만 기다리세요...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {

        String result; // 요청 결과를 저장할 변수.
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}