package org.meicode.doan_android;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomePageGroup extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    ArrayList<TaskGroup> taskGroups;
    Context mContext;
    FirebaseAuth AuthUI;
    HorizontalAdapter horizontalAdapter;
    AdapterListViewGroupHomePage adapterListViewGroupHomePage;
    String content[];
    ArrayList<ListChildHome> listChildren;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference reference;
    ActionBar actionBar;
    String userid;
    String idTask;
    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
    private void setActionBar(){

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_interface);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
//        changeStatusBarColor("#6D85F6");
    }
    private void initView(){
        recyclerView = findViewById(R.id.recycler_view);
        listView = findViewById(R.id.listViewHome);
    }
    private void setHorizontalAdapter(ArrayList<TaskGroup> taskGroupss){
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                HomePageGroup.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //Init Adapter
        horizontalAdapter = new HorizontalAdapter(HomePageGroup.this, taskGroupss, new itemClickRecycler() {
            @Override
            public void onSelect(String id, String path) {
                adapterListViewGroupHomePage.clear();
                getListChildren(id, path);
            }
        });

        recyclerView.setAdapter(horizontalAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_right_add,menu);
        return true;
    }


    private void getListChildren(String id, String path){
        String Path = path;
        String Id = id;
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(Id.equals(snapshot.getKey()))
                {
                    for(DataSnapshot child : snapshot.child("Tasks").child(path).child("TasksChild").getChildren())
                    {
                        String Name = child.getKey();
                        String TimeC = child.child("Detail").child("Ngày tạo").getValue().toString();
                        String Admin = child.child("Detail").child("Người đảm nhận").getValue().toString();

                        ListChildHome children = new ListChildHome(Id,Name,Name,TimeC,Admin,false);
                        listChildren.add(children);
                        adapterListViewGroupHomePage.notifyDataSetChanged();
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
    private void initFirebase(){
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Groups");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
        Intent intent = getIntent();
        idTask = intent.getStringExtra("IDTASK");
    }
    private void getDataGroups(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(idTask.equals(snapshot.getKey()))
                {
                    for(DataSnapshot task : snapshot.child("Tasks").getChildren())
                    {
                        TaskGroup taskGroup = new TaskGroup(task.getKey(),task.child("Detail/Ngày tạo").getValue().toString(),snapshot.getKey());
                        taskGroups.add(taskGroup);
                    }
                }
                horizontalAdapter.notifyDataSetChanged();
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
    public interface itemClickRecycler{
        public void onSelect(String id,String path);
    }
    private void onClick(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.profile) {
                    startActivity(new Intent(HomePageGroup.this, UserInfo.class));
                    finish();
                    return true;
                } else if (id == R.id.history) {
                    startActivity(new Intent(HomePageGroup.this, HistoryTasks.class));
                    finish();
                    return true;
                } else if (id == R.id.analyst) {
                    startActivity(new Intent(HomePageGroup.this, input_Chart.class));
                    finish();
                    return true;
                } else if (id == R.id.share) {
                    startActivity(new Intent(HomePageGroup.this, Comingsoon.class));
                    finish();
                    return true;
                } else if (id == R.id.setting) {
                    startActivity(new Intent(HomePageGroup.this, Comingsoon.class));
                    finish();
                    return true;
                } else if (id == R.id.help) {
                    startActivity(new Intent(HomePageGroup.this, Comingsoon.class));
                    finish();
                    return true;
                }else if(id == R.id.logout) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePageGroup.this);
                    builder.setTitle("Bạn muốn đăng xuất");
                    builder.setIcon(R.drawable.logout);
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AuthUI.getInstance().signOut();
                            Intent intent2 = new Intent(HomePageGroup.this, SignIn.class);
                            startActivity(intent2);
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    return true;
                }
            return false;
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_group);
        setActionBar();
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        mContext = this;
        initView();
        //Integer[] img = {R.drawable.task_cylinder,R.drawable.task_barchart,R.drawable.task_pen,R.drawable.task_area};
        taskGroups = new ArrayList<>();
        listChildren = new ArrayList<ListChildHome>();
        adapterListViewGroupHomePage = new AdapterListViewGroupHomePage(this, R.layout.row_item_home_lv,listChildren);
        listView.setAdapter(adapterListViewGroupHomePage);
        initFirebase();
        setHorizontalAdapter(taskGroups);
        getDataGroups();
        onClick();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        if(id == R.id.btn_add)
        {
            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}