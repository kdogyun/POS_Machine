package com.example.dogyun.myapplication.ViewHolders;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dogyun.myapplication.MainActivity;
import com.example.dogyun.myapplication.Adapter.MyPagerAdapter;
import com.example.dogyun.myapplication.Networks.NetworkTask;
import com.example.dogyun.myapplication.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.Date;

/**
 * Created by dogyun on 2018-09-26.
 */

public class MyChildViewHolder extends ChildViewHolder {

    public TextView listChild;
    public Button buttonChild;

    public MyChildViewHolder(View itemView) {
        super(itemView);
        listChild = (TextView) itemView.findViewById(R.id.option1);
        buttonChild = (Button) itemView.findViewById(R.id.buttonChild);
    }


    public void onBind(String Sousdoc, String parentTitle) {
        final String str = parentTitle.substring(0,19);
        final String type = parentTitle.substring(parentTitle.length()-3,parentTitle.length()-1);
        listChild.setText(Sousdoc);
        buttonChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(itemView.getContext());

                ad.setTitle("환불");       // 제목 설정
                ad.setMessage("환불 하시겠습니까?");   // 내용 설정

                // 확인 버튼 설정
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = new Date(System.currentTimeMillis());
                        MainActivity.dbHelper.refundBill(str, type);
//                        String url = MainActivity.siteURL + "POS_refund.php";
//                        ContentValues cv = new ContentValues();
//                        cv.put("cago","PLAN_bill");
//                        cv.put("fix", str);
//                        cv.put("type", type);
//                        // AsyncTask를 통해 HttpURLConnection 수행.
//                        NetworkTask networkTask = new NetworkTask(itemView.getContext(), url, cv);
//
//                        try {
//                            networkTask.execute();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        dialog.dismiss();     //닫기
                        // Event

                        MainActivity.getBill();
                        MyPagerAdapter.historyFragment.historyFirstDraw();
                        MyPagerAdapter.chartFragment.chartFirstDraw();
                    }
                });

                // 취소 버튼 설정
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                // 창 띄우기
                ad.show();
            }
        });
    }


}