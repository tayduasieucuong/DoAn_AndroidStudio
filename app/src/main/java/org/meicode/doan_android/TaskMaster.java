package org.meicode.doan_android;

import android.content.Context;
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
        adapter = new AdapterTaskItem(listGroup,listChild,headerTitle);
        expandableListView.setAdapter(adapter);
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
                            if(!"Detail".equals(dschild.getKey()))
                                childItem.add(dschild.getKey().toString());
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