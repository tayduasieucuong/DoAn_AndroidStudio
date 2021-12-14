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
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
    FirebaseAuth AuthUI;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    String userid;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    String username;
    String email;
    TextView userName;
    TextView userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);
        setActionBar();
        setBottomNavigation();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        expandableListView = findViewById(R.id.exp_list_view);
        //Database
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
        readTasks();
        //
        adapter = new MainAdapter(listGroup,listChild);
        expandableListView.setAdapter(adapter);
        setOnClickAddTask();
        setOnClickExpandListView();
        onClick();
    }
    private void onClick(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.profile)
                {
                    startActivity(new Intent(TaskManagement.this,UserInfo.class));
                    finish();
                    return true;
                }else if(id == R.id.history)
                {
                    startActivity(new Intent(TaskManagement.this,HistoryTasks.class));
                    finish();
                    return true;
                }else if(id == R.id.analyst)
                {
                    return true;
                }else if(id == R.id.share)
                {
                    return true;
                }else if(id == R.id.setting)
                {
                    return true;
                }else if(id == R.id.help)
                {
                    return true;
                }else if(id == R.id.logout)
                {
                    AuthUI.getInstance().signOut();
                    Intent intent2 = new Intent(TaskManagement.this,SignIn.class);
                    startActivity(intent2);
                    return true;
                }
                return false;
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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setTitle("");
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(1);
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
                        Toast.makeText(TaskManagement.this, "Person", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.group:
                        Toast.makeText(TaskManagement.this, "Group", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(TaskManagement.this,calendar_task.class));
                        finish();
                        return true;
                    case R.id.alarm:
                        startActivity(new Intent(TaskManagement.this,input_time.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
    private void setOnClickAddTask(){
        FloatingActionButton btn_add_task = findViewById(R.id.addbottom);
        btn_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskManagement.this,CreateTask.class));
                finish();
            }
        });
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
                    Intent intent = new Intent(TaskManagement.this,TaskMaster.class);
                    intent.putExtra("HeaderTitle",expandableListView.getItemAtPosition(position).toString());
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Notify", Toast.LENGTH_SHORT).show();
                    finish();
                    //intent.putExtra("HeaderTitle",);
                    //onGroupLongClick(groupPosition);
                }

                /*  if child item clicked */
                else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    //  ...
                   //onChildLongClick(groupPosition, childPosition);
                    Toast.makeText(getApplicationContext(), "Notify", Toast.LENGTH_SHORT).show();
                    finish();

                }
                return false;
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
                    email = snapshot.child("UserInfo").child("Email").getValue().toString();
                    username = snapshot.child("UserInfo").child("Name").getValue().toString();
                    userName = (TextView) findViewById(R.id.usr_name);
                    userEmail = (TextView) findViewById(R.id.usr_email);
                    try {
                        userEmail.setText(email);
                        userName.setText(username);
                    }catch (Exception ex){}
                    for(DataSnapshot ds : snapshot.child("Tasks").getChildren())
                    {
                        if(!ds.getKey().equals("Lịch sử công việc")) {
                            String groupname = (String) ds.getKey();
                            listGroup.add(groupname);
                            childItem = new ArrayList<>();
                            for (DataSnapshot dschild : ds.getChildren()) {
                                if (!"Detail".equals(dschild.getKey()))
                                    childItem.add(dschild.getKey().toString());
                            }
                            listChild.put(listGroup.get(counter), childItem);
                            counter++;
                        }
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
            drawerLayout.openDrawer(GravityCompat.START);
        }else if( id == R.id.notify)
        {
            Toast.makeText(getApplicationContext(), "Notify", Toast.LENGTH_SHORT).show();
        }else if( id == R.id.search){

            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return true;
    }
    //Menu Bottom Event



}