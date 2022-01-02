package org.meicode.doan_android;

import android.app.ActionBar;
import android.app.Dialog;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
    DatabaseReference referenceUser;
    ActionBar actionBar;
    String userid;
    String idTask;
    ImageView btn_add_child;
    String Admin_child;
    String NameTask;
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
//        changeStatusBarColor("#6D85F6");
    }
    private void initView(){
        recyclerView = findViewById(R.id.recycler_view);
        listView = findViewById(R.id.listViewHome);
        btn_add_child = findViewById(R.id.btn_add_group_child);
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
                NameTask = path;
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
        referenceUser = database.getReference("Users");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
        Intent intent = getIntent();
        idTask = intent.getStringExtra("IDTASK");
    }
    String CurrentDate;
    private void getCurrentDate(){
        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        CurrentDate = date.toString();
        String[] DateCurrentTemp = CurrentDate.split("-",3);
        CurrentDate = DateCurrentTemp[2] + "/" + DateCurrentTemp[1] + "/" + DateCurrentTemp[0];
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
        btn_add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChild();
            }
        });
    }
    private void showDialogChild(){
        final Dialog dialog = new Dialog(HomePageGroup.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_child);
        final EditText et_name_child = dialog.findViewById(R.id.et_name_child);
        final TextView tv_time_child = dialog.findViewById(R.id.time_child);
        final EditText et_Email = dialog.findViewById(R.id.et_email);
        final Button btn_create_child = dialog.findViewById(R.id.btn_create_child);
        getCurrentDate();
        tv_time_child.setText(CurrentDate);
        btn_create_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NameChild = et_name_child.getText().toString();
                String TimeChild = CurrentDate;
                String EmailChild = et_Email.getText().toString();
                if(NameChild.equals(""))
                {
                    Toast.makeText(HomePageGroup.this, "Tên bị bỏ trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(EmailChild.equals(""))
                {
                    Toast.makeText(HomePageGroup.this, "Email người đảm nhận trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                referenceUser.addChildEventListener(new ChildEventListener() {
                    int flag = 0;
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(EmailChild.equals(snapshot.child("UserInfo/Email").getValue())) {
                            flag = 1;
                            try {
                                Admin_child = snapshot.child("UserInfo/Name").getValue().toString();
                                reference.child(idTask).child("Tasks").child(NameTask).child("TasksChild").child(NameChild).child("Detail/Ngày tạo").setValue(CurrentDate);
                                reference.child(idTask).child("Tasks").child(NameTask).child("TasksChild").child(NameChild).child("Detail/Người đảm nhận").setValue(Admin_child);
                                dialog.dismiss();
                                recreate();

                            }
                            catch (Exception e){
                                return;
                            }
                        }
                        if(flag==0)
                            Toast.makeText(HomePageGroup.this, "Không tồn tại "+EmailChild, Toast.LENGTH_SHORT).show();
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
        });
        dialog.show();
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
            onBackPressed();
        }
        if(id == R.id.btn_add)
        {
            showDialog();
        }
        return true;
    }
    public void showDialog(){
        final Dialog dialog = new Dialog(HomePageGroup.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_parent_task);

        final EditText et_name = dialog.findViewById(R.id.et_name);
        final TextView tv_time = dialog.findViewById(R.id.time);
        final Button btn_create = dialog.findViewById(R.id.btn_create);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String etName = et_name.getText().toString();
                getCurrentDate();
                String time_create = CurrentDate;
                tv_time.setText(CurrentDate);
                if(etName.equals("")) {
                    Toast.makeText(HomePageGroup.this, "Tên Task đang bị bỏ trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                reference.child(idTask).child("Tasks").child(etName).child("Detail").child("Ngày tạo").setValue(time_create);
                dialog.dismiss();
                recreate();
            }
        });
        dialog.show();
    }
}