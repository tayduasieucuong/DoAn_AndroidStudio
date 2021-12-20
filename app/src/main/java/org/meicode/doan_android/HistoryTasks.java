package org.meicode.doan_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryTasks extends AppCompatActivity {
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    ExpandableListView expandableListView;
    ArrayList<String> listGroup = new ArrayList<>();
    Context context;
    HashMap<String,ArrayList<String>> listChild = new HashMap<>();
    String userid;
    FirebaseDatabase database;
    DatabaseReference reference,rf;
    AdapterHistory adapterTaskItem;
    ArrayList<String> childItem;
    ArrayList<String> listGroupDetail = new ArrayList<>();
    private void getData(){
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");

        expandableListView = (ExpandableListView) findViewById(R.id.exp_list_view);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_tasks);
        setActionBar();
        getData();
        onReadTasks();
        adapterTaskItem = new AdapterHistory(listGroup,listChild,listGroupDetail);
        expandableListView.setAdapter(adapterTaskItem);
    }
    private void onReadTasks(){
        reference.addChildEventListener(new ChildEventListener() {
            int counter = 0;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(userid.equals(snapshot.getKey()))
                {
                    for(DataSnapshot ds : snapshot.child("Tasks").child("Lịch sử công việc").getChildren())
                    {
                        String groupname =(String) ds.getKey();
                        listGroup.add(groupname);
                        listGroupDetail.add(ds.child("Detail").child("Ngày hoàn thành").getValue().toString()+"-"+ds.child("Detail").child("Trạng thái").getValue().toString());
                        childItem = new ArrayList<>();
                        for (DataSnapshot dschild : ds.child("TasksChild").getChildren())
                        {
                                childItem.add(dschild.getKey().toString()+"/"+dschild.getValue());
                        }
                        listChild.put(listGroup.get(counter),childItem);
                        counter++;
                    }
                    adapterTaskItem.notifyDataSetChanged();
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("Lịch sử công việc");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
    }
    private  void setBottomNavigation()
    {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        FloatingActionButton add_btn = (FloatingActionButton) findViewById(R.id.addbottom);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            Intent intent = new Intent(HistoryTasks.this,TaskManagement.class);
            startActivity(intent);
            finish();

        }
        return true;
    }
}