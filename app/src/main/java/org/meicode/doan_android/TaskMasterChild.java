package org.meicode.doan_android;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
    DatabaseReference reference;
    ListViewAdapter adapter;
    private void InitView(){
        tv = (TextView) findViewById(R.id.tv_title);
        btn_add_child_task = (ImageView) findViewById(R.id.btn_add_task);
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
                        Toast.makeText(TaskMasterChild.this, "Person", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.group:
                        Toast.makeText(TaskMasterChild.this, "Group", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(TaskMasterChild.this,calendar_task.class));
                        finish();
                        return true;
                    case R.id.alarm:
                        startActivity(new Intent(TaskMasterChild.this,input_time.class));
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