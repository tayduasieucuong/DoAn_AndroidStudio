package org.meicode.doan_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class TaskMasterChild extends AppCompatActivity {
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    ArrayList<String> list;
    ArrayList<String> listTime;
    ListView lv;
    TextView tv;
    ImageView btn_add_child_task;
    String headername;
    String userid;
    String headerMaster;
    FirebaseDatabase database;
    DatabaseReference reference,rf;
    ImageView btn_donealltasks;
    ListViewAdapter adapter;
    SimpleDateFormat sdf;
    String CurrentDate;
    private void getCurrentDate(){
        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        CurrentDate = date.toString();
        String[] DateCurrentTemp = CurrentDate.split("-",3);
        CurrentDate = DateCurrentTemp[2] + "/" + DateCurrentTemp[1] + "/" + DateCurrentTemp[0];
    }
    private void InitView(){
        tv = (TextView) findViewById(R.id.tv_title);
        btn_add_child_task = (ImageView) findViewById(R.id.btn_add_task);
        btn_donealltasks = (ImageView) findViewById(R.id.completeAlltasks);
    }
    private void getData(){
        Intent intent = getIntent();
        headername = intent.getStringExtra("HeaderName"); // OK
        headerMaster = intent.getStringExtra("HeaderMaster");//tat ca cong viec
        tv.setText(headername);
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
        sdf = new SimpleDateFormat("dd/MM/yyyy");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_master_child);
        setActionBar();
        setBottomNavigation();
        InitView();
        getData();
        lv = (ListView) findViewById(R.id.lv);
        list = new ArrayList<String>();
        listTime = new ArrayList<String>();
        adapter = new ListViewAdapter(this, R.layout.layout_item_child_taskmaster,list,headerMaster,headername,listTime);
        lv.setAdapter(adapter);
        readTasks();
        onClick();
    }
    private void onClick(){
        btn_add_child_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskMasterChild.this,TaskChildPerson.class);
                intent.putExtra("NameOfTask",headername);
                intent.putExtra("HeaderTitle",headerMaster);
                intent.putExtra("forwardTo","TaskMasterChild");
                startActivity(intent);
                finish();
            }
        });
        FloatingActionButton btn_add_task = findViewById(R.id.addbottom);
        btn_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskMasterChild.this,CreateTask.class));
                finish();
            }
        });
        btn_donealltasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskMasterChild.this);
                builder.setMessage("Bạn đã hoàn thành tất cả rồi chứ ^^")
                        .setPositiveButton("Hoàn thành", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference drRemove = reference.child(userid).child("Tasks").child("Tất cả công việc").child(headername);
                                completeTask();
                                drRemove.removeValue();
                                Toast.makeText(TaskMasterChild.this, "Chúc mừng bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TaskMasterChild.this,TaskManagement.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Chưa hoàn thành", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(TaskMasterChild.this, "Làm việc đi nào !!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
            }
        });

    }
    public void completeTask() {
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (userid.equals(snapshot.getKey())) {
                    DatabaseReference drRemove = reference.child(userid).child("Tất cả công việc").child(headername);
                    DatabaseReference dr = reference.child(userid)
                            .child("Tasks/Lịch sử công việc")
                            .child(headername);
                    DatabaseReference drDetail = dr.child("Detail");
                    DatabaseReference drTasksChild = dr.child("TasksChild");
                    getCurrentDate();
                    drDetail.child("Ngày hoàn thành").setValue(CurrentDate);
                    drDetail.child("Trạng thái").setValue("Hoàn thành");
                    for (DataSnapshot dsTasksChild : snapshot.child("Tasks")
                            .child("Tất cả công việc")
                            .child(headername)
                            .child("TasksChild").getChildren()) {
                        //ds.getKey = Thiết kế chức năng
                        if (!dsTasksChild.child("Detail/Trạng thái").getValue().toString().equals("Xong")) {
                            drTasksChild.child(dsTasksChild.getKey().toString()).child("Ngày hoàn thành").setValue(CurrentDate);
                            drTasksChild.child(dsTasksChild.getKey().toString()).child("Phần trăm hoàn thành").setValue("100%");
                        }
                    }
                    getTimeToNotify();
                    String randomKey = randomString(15);
                    reference.child(userid).child("Notification").child(randomKey).child("Content").setValue("Hoàn thành công việc lớn "+ headername);
                    reference.child(userid).child("Notification").child(randomKey).child("Time").setValue(DateNotify);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setActionBar()
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //actionBar.setLogo(R.drawable.ic_menu);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        actionBar.setTitle("");
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(3);
    }
    String DateNotify;
    private void getTimeToNotify(){
        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        DateNotify = date.toString();
        String[] DateCurrentTemp = DateNotify.split("-",3);;

        String[] time = java.text.DateFormat.getDateTimeInstance().format(android.icu.util.Calendar.getInstance().getTime()).toString().split(" ",3);
        String[] timeLocate = time[2].split(" ",3);
        DateNotify = DateCurrentTemp[2] + "/" + DateCurrentTemp[1] + "/" + DateCurrentTemp[0] + " - "+timeLocate[2];
    }
    String randomString(int n)
    {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }
    private  void setBottomNavigation()
    {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        FloatingActionButton add_btn = (FloatingActionButton) findViewById(R.id.addbottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.person:
                        startActivity(new Intent(TaskMasterChild.this,TaskManagement.class));
                        finish();
                        return true;
                    case R.id.group:
                        startActivity(new Intent(TaskMasterChild.this,Comingsoon.class) );
                        finish();
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(TaskMasterChild.this,calendar_task.class));
                        finish();
                        return true;
                    case R.id.alarm:
                        startActivity(new Intent(TaskMasterChild.this,ListFocusTime.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
    private void readTasks(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey();
                if(userid.equals(key))
                {
                    for (DataSnapshot ds : snapshot.child("Tasks").child(headerMaster).child(headername).child("TasksChild").getChildren())
                    {
                        list.add(ds.getKey().toString()+"/"+ds.child("Detail").child("Trạng thái").getValue().toString());
                        listTime.add(ds.child("Detail").child("Ngày bắt đầu").getValue().toString() + "-" +ds.child("Detail").child("Ngày kết thúc").getValue().toString());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            startActivity(new Intent(TaskMasterChild.this,TaskManagement.class));
            finish();
        }
        return true;
    }
}