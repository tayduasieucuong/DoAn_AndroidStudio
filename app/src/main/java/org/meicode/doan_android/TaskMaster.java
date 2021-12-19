package org.meicode.doan_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
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

import java.util.ArrayList;
import java.util.HashMap;

public class TaskMaster extends AppCompatActivity {
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton btn_add_task;
    String headerTitle;
    TextView tv_group;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userid;
    ArrayList<String> listGroup = new ArrayList<>();
    Context context;
    HashMap<String,ArrayList<String>> listChild = new HashMap<>();
    AdapterTaskItem adapter;
    ExpandableListView expandableListView;
    String CurrentDate;
    String interfaceNameTaskMaster;
    public interface OnCompleteTaskMasterListener{
        public void onCompleteTaskMaster(String data);
    }
    private void initView(){

        expandableListView = findViewById(R.id.exp_list_view);
        Intent intent = getIntent();
        headerTitle = intent.getStringExtra("HeaderTitle");
        tv_group = (TextView) findViewById(R.id.tv_group);
        tv_group.setText(headerTitle);
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
        adapter = new AdapterTaskItem(listGroup, listChild, headerTitle, new OnCompleteTaskMasterListener() {
            @Override
            public void onCompleteTaskMaster(String data) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskMaster.this);
                interfaceNameTaskMaster = data;
                builder.setMessage("Bạn đã hoàn thành tất cả rồi chứ ^^")
                        .setPositiveButton("Hoàn thành", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                completeTask();
                                DatabaseReference drRemove = reference.child(userid).child("Tasks").child("Tất cả công việc").child(interfaceNameTaskMaster);
                                drRemove.removeValue();
                                Toast.makeText(TaskMaster.this, "Chúc mừng bạn đã hoàn thành", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        })
                        .setNegativeButton("Chưa hoàn thành", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(TaskMaster.this, "Làm việc đi nào !!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();

            }
        });
        expandableListView.setAdapter(adapter);
    }
    private void getCurrentDate(){
        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        CurrentDate = date.toString();
        String[] DateCurrentTemp = CurrentDate.split("-",3);
        CurrentDate = DateCurrentTemp[2] + "/" + DateCurrentTemp[1] + "/" + DateCurrentTemp[0];
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_master);
        initView();
        setActionBar();
        setBottomNavigation();
        readTasks();
        onClick();
    }
    public void completeTask(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (userid.equals(snapshot.getKey())){
                    DatabaseReference drRemove = reference.child(userid).child("Tất cả công việc").child(interfaceNameTaskMaster);
                    DatabaseReference dr = reference.child(userid)
                            .child("Tasks/Lịch sử công việc")
                             .child(interfaceNameTaskMaster);
                    DatabaseReference drDetail = dr.child("Detail");
                    DatabaseReference drTasksChild = dr.child("TasksChild");
                    getCurrentDate();
                    drDetail.child("Ngày hoàn thành").setValue(CurrentDate);
                    drDetail.child("Trạng thái").setValue("Hoàn thành");
                    for(DataSnapshot dsTasksChild : snapshot.child("Tasks")
                            .child("Tất cả công việc")
                            .child(interfaceNameTaskMaster)
                            .child("TasksChild").getChildren()){
                        //ds.getKey = Thiết kế chức năng
                        if(!dsTasksChild.child("Detail/Trạng thái").getValue().toString().equals("Xong"))
                        {
                            drTasksChild.child(dsTasksChild.getKey().toString()).child("Ngày hoàn thành").setValue(CurrentDate);
                            drTasksChild.child(dsTasksChild.getKey().toString()).child("Phần trăm hoàn thành").setValue("100%");
                        }
                    }

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
    public void readTasks()
    {

        reference.addChildEventListener(new ChildEventListener() {
            int counter = 0;
            ArrayList<String> childItem;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey().toString();
                if(userid.equals(key)) {
                    //listGroup.add(snapshot.child("UserInfo").child("Tasks").getKey());

                    for(DataSnapshot ds : snapshot.child("Tasks").child(headerTitle).getChildren())
                    {
                        String groupname =(String) ds.getKey();
                        listGroup.add(groupname);
                        childItem = new ArrayList<>();
                        for (DataSnapshot dschild : ds.child("TasksChild").getChildren())
                        {
                            if(!"Detail".equals(dschild.getKey())) {
                                String temp = dschild.child("Detail").child("Trạng thái").getValue().toString();
                                try {
                                    String time = dschild.child("Detail").child("Ngày bắt đầu").getValue().toString() + " -> " + dschild.child("Detail").child("Ngày kết thúc").getValue().toString();
                                    childItem.add(dschild.getKey().toString() + "-" + temp + "-" + time);
                                }catch (Exception e){}
                            }
                        }
                        listChild.put(listGroup.get(counter),childItem);
                        counter++;
                    }
                    adapter.notifyDataSetChanged();
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
    private void onClick(){
        FloatingActionButton btn_add_task = findViewById(R.id.addbottom);
        btn_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskMaster.this,CreateTask.class));
                finish();
            }
        });
    }
    private void setActionBar()
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("");

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(3);
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
                        startActivity(new Intent(TaskMaster.this,TaskManagement.class));
                        finish();
                        return true;
                    case R.id.group:
                        startActivity(new Intent(TaskMaster.this,Comingsoon.class) );
                        finish();
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(TaskMaster.this,calendar_task.class));
                        finish();
                        return true;
                    case R.id.alarm:
                        startActivity(new Intent(TaskMaster.this,input_time.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            startActivity(new Intent(this,TaskManagement.class));
            finish();
        }else if( id == R.id.notify)
        {
            Toast.makeText(getApplicationContext(), "Notify", Toast.LENGTH_SHORT).show();

        }
        return true;
    }
    private  void setOnClickExpandListView()
    {
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                long packedPosition = expandableListView.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);


                /*  if group item clicked */
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    //  ...

                    //onGroupLongClick(groupPosition);
                }

                /*  if child item clicked */
                else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    //  ...
                    //onChildLongClick(groupPosition, childPosition);

                }

                return false;
            }
        });
    }
}