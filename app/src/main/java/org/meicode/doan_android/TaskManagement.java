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
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManagement extends AppCompatActivity {
    //View for event
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    ExpandableListView expandableListView;
    //List
    ArrayList<String> listGroup = new ArrayList<>();
    Context context;
    HashMap<String,ArrayList<String>> listChild = new HashMap<>();
    //Adapter for job
    MainAdapter adapter;
    //Database Firebase
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);
        setActionBar();
        setBottomNavigation();
        expandableListView = findViewById(R.id.exp_list_view);
        //Database
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        //readTasks();
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
        readTasks();
        //
        adapter = new MainAdapter(listGroup,listChild);
        expandableListView.setAdapter(adapter);
        setOnClickAddTask();

    }
    private void setActionBar()
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //actionBar.setLogo(R.drawable.ic_menu);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
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
    private void setOnClickAddTask(){
        FloatingActionButton btn_add_task = findViewById(R.id.addbottom);
        btn_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskManagement.this,CreateTask.class));
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
                    Toast.makeText(TaskManagement.this, snapshot.child("UserInfo").child("Email").getValue().toString(), Toast.LENGTH_SHORT).show();
                    //listGroup.add(snapshot.child("UserInfo").child("Tasks").getKey());

                    for(DataSnapshot ds : snapshot.child("Tasks").getChildren())
                    {
                        String groupname =(String) ds.getKey();
                        listGroup.add(groupname);
                        childItem = new ArrayList<>();
                        for (DataSnapshot dschild : ds.getChildren())
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
    //Menu Top Event
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
            Toast.makeText(TaskManagement.this, "Ok", Toast.LENGTH_SHORT).show();
        }else if( id == R.id.notify)
        {
            Toast.makeText(getApplicationContext(), "Notify", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    //Menu Bottom Event



}