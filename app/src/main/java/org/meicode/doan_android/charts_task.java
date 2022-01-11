package org.meicode.doan_android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class charts_task extends AppCompatActivity {
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntriesArrayList;

    SimpleDateFormat sdf;
    FirebaseDatabase database;
    DatabaseReference mData, rf;
    FirebaseAuth mAuth;
    String userid, d1, d2, tgDate, tgPs;
    Date date1, date2, tgD;
    private static int[] percent_Date;
    int count, k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_task);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://auth-4a763-default-rtdb.firebaseio.com/");
        mData = FirebaseDatabase.getInstance().getReference();
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID", null);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("date1") != null) {
            try {
                d1 = bundle.getString("date1");
                d2 = bundle.getString("date2");
                date1 = sdf.parse(d1);
                date2 = sdf.parse(d2);
                count = (int) ((date2.getTime() - date1.getTime()) / 86400000) + 1;
                percent_Date = new int[count];
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        loadData();
        getBarEntries();
    }
    private void init_Chart() {
        barChart = findViewById(R.id.idBarChart);
        barDataSet = new BarDataSet(barEntriesArrayList, "Thống kê từ ngày " + d1 + " đến " + d2);
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);

    }
    private void getBarEntries() {
        barEntriesArrayList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            barEntriesArrayList.add(new BarEntry((float) i+1, percent_Date[i]*1));
        }
    }

    private void loadData(){
        ValueEventListener valueEventListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.child("Users").child(userid).child("Tasks").child("Lịch sử công việc").getChildren()) {
                    for (DataSnapshot ds1 : ds.child("TasksChild").getChildren()) {
                        tgDate = ds1.child("Ngày hoàn thành").getValue().toString();
                        tgPs = ds1.child("Phần trăm hoàn thành").getValue().toString();
                        tgPs = removeLastChar(tgPs);
                        try {
                            tgD = sdf.parse(tgDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (tgD.compareTo(date1) >= 0 && tgD.compareTo(date2) <= 0) {
                            k = (int) (tgD.getTime() - date1.getTime()) / 86400000;
                            if (percent_Date[k] != 0) {
                                percent_Date[k] = (Integer.parseInt(tgPs) + percent_Date[k]) / 2;
                            } else {
                                percent_Date[k] = Integer.parseInt(tgPs);
                            }
                        }
                    }
                }
                getBarEntries();
                init_Chart();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mData.addValueEventListener(valueEventListener);
    }
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    //
}