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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
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

import java.sql.DataTruncation;
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
    DatabaseReference reference,rf1;
    DatabaseReference referenceUser;
    ActionBar actionBar;
    String userid;
    String idTask;
    ImageView btn_add_child;
    String Admin_child;
    String NameTask;
    ActionMenuItemView btn_top_add;
    TextView tv;
    String isManager = "YES";
    Spinner spinnerPersonal;
    String TaskNameP;
    ArrayList<String> itemSpinner = new ArrayList<String>();
    TextView tv_chucvu;
    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
    private void getUser(){
        DatabaseReference rf = database.getReference("Groups/"+idTask+"/Thành viên/"+userid+"/Info");
        rf.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getKey().equals("Chức vụ"))
                {
                    tv_chucvu.setText("Chức vụ: "+snapshot.getValue().toString());
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
    private void setActionBar(){

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        Intent intent = getIntent();
        String title = intent.getStringExtra("NameTaskGroup");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setElevation(0);
//        changeStatusBarColor("#6D85F6");
    }
    private void initView(){
        recyclerView = findViewById(R.id.recycler_view);
        listView = findViewById(R.id.listViewHome);
        btn_add_child = findViewById(R.id.btn_add_group_child);
        btn_top_add = findViewById(R.id.btn_add);
        tv_chucvu  = findViewById(R.id.tv_chucvu);
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
                TaskNameP = path;
                getListChildren(id, path);
            }
        }, new itemBtnComplete() {
            @Override
            public void onComplete(String id, String name) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePageGroup.this);
                builder.setMessage("Bạn đã hoàn thành tất cả rồi chứ ^^")
                        .setPositiveButton("Hoàn thành", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                completeTaskParent(id, name);
                            }
                        })
                        .setNegativeButton("Chưa hoàn thành", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(HomePageGroup.this, "Đẩy mạnh công việc lên nào !!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
            }
        }, new itemBtnDelete() {
            @Override
            public void onDelete(String id, String name) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomePageGroup.this);
                builder.setMessage("Bạn muốn xóa chứ ^^")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference rf = database.getReference("Groups/"+idTask+"/Tasks/"+name);
                                rf.removeValue();
                                recreate();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder.create().show();
            }
        });

        recyclerView.setAdapter(horizontalAdapter);
    }

    private void completeTaskParent(String idP, String NameTask)
    {
        DatabaseReference rf = database.getReference("Groups/"+idTask+"/Thành viên/"+userid+"/Info");
        rf.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getKey().equals("Chức vụ"))
                {
                    if(snapshot.getValue().toString().equals("Quản lý"))
                    {
                        DatabaseReference rfRmove = database.getReference("Groups/"+idTask+"/Tasks/"+NameTask+"/Detail");
                        rfRmove.child("Status").setValue("Xong");
                        recreate();
                    }else{
                        Toast.makeText(HomePageGroup.this, "Chỉ quản trị viên mới có thể hoàn thành", Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_right_add,menu);
        DatabaseReference rf = database.getReference("Groups/"+idTask+"/Thành viên/"+userid+"/Info");
        rf.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getKey().equals("Chức vụ"))
                {
                    if(snapshot.getValue().toString().equals("Nhân viên"))
                    {
                        btn_add_child.setVisibility(View.GONE);
                        isManager = "NO";
                        MenuItem item = menu.findItem(R.id.btn_add);
                        item.setVisible(false);
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
        if(isManager.equals("NO"))
        {
            MenuItem item = menu.findItem(R.id.btn_add);
            item.setVisible(false);
        }
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
                        String Status = child.child("Detail").child("Status").getValue().toString();
                        ListChildHome children = new ListChildHome(TaskNameP,Name,Name,TimeC,Admin,Status);
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
                        TaskGroup taskGroup = new TaskGroup(task.getKey(),task.child("Detail/Ngày tạo").getValue().toString(),snapshot.getKey(),task.child("Detail/Status").getValue().toString());
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
    public interface itemBtnComplete{
        public void onComplete(String id,String name);
    }
    public interface itemBtnDelete{
        public void onDelete(String id, String name);
    }
    public interface itemBtnCompleteChild{
        public void onComplete(String idP, String id, String Name);
    }
    public interface itemBtnDeleteChild{
        public void onDelete(String idP, String id, String Name);
    }
    private void onClick(){
        btn_add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChild();
            }
        });
    }
    private void displaySpinner(){
        rf1 = database.getReference("Groups");
        itemSpinner.clear();
        rf1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(idTask.equals(snapshot.getKey()))
                {
                    for(DataSnapshot task : snapshot.child("Thành viên").getChildren())
                    {
                        itemSpinner.add(task.child("Info").child("Biệt danh").getValue().toString());
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,itemSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPersonal.setAdapter(adapter);
    }
    private void showDialogChild(){
        final Dialog dialog = new Dialog(HomePageGroup.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_child);
        final EditText et_name_child = dialog.findViewById(R.id.et_name_child);
        final TextView tv_time_child = dialog.findViewById(R.id.time_child);
        spinnerPersonal = dialog.findViewById(R.id.spinner2);
        final Button btn_create_child = dialog.findViewById(R.id.btn_create_child);
        displaySpinner();
        getCurrentDate();
        tv_time_child.setText("Ngày tạo " + CurrentDate);
        btn_create_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String NameChild = et_name_child.getText().toString();
                String TimeChild = CurrentDate;
                String EmailChild = spinnerPersonal.getSelectedItem().toString();
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
                                reference.child(idTask).child("Tasks").child(NameTask).child("TasksChild").child(NameChild).child("Detail/Status").setValue("Chưa xong");
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
        adapterListViewGroupHomePage = new AdapterListViewGroupHomePage(this, R.layout.row_item_home_lv, listChildren, new itemBtnCompleteChild() {
            @Override
            public void onComplete(String idP, String id, String Name) {
                onExcuteComplete(idP,id,Name);
            }
        }, new itemBtnDeleteChild() {
            @Override
            public void onDelete(String idP, String id, String Name) {
                Toast.makeText(HomePageGroup.this, "Delete", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(adapterListViewGroupHomePage);
        initFirebase();
        setHorizontalAdapter(taskGroups);
        getDataGroups();
        getUser();
        onClick();
    }
    private void onExcuteComplete(String idPa, String Id, String FullName)
    {

        DatabaseReference rfInfoUser = database.getReference("Groups/"+idTask+"/Thành viên/"+userid+"/Info");
        rfInfoUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getKey().equals("Họ và tên"))
                {
                    if(FullName.equals(snapshot.getValue()))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageGroup.this);
                        builder.setMessage("Bạn đã hoàn thành tất cả rồi chứ ^^")
                                .setPositiveButton("Hoàn thành", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseReference rf = database.getReference("Groups/"+idTask+"/Tasks/"+idPa+"/TasksChild/"+Id+"/Detail");
                                        rf.child("Status").setValue("Xong");
                                        recreate();
                                    }
                                })
                                .setNegativeButton("Chưa hoàn thành", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(HomePageGroup.this, "Đẩy mạnh công việc lên nào !!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        builder.create().show();
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
                tv_time.setText("Ngày tạo: " + CurrentDate);
                if(etName.equals("")) {
                    Toast.makeText(HomePageGroup.this, "Tên Task đang bị bỏ trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                reference.child(idTask).child("Tasks").child(etName).child("Detail").child("Ngày tạo").setValue(time_create);
                reference.child(idTask).child("Tasks").child(etName).child("Detail").child("Status").setValue("Chưa xong");
                dialog.dismiss();
                recreate();
            }
        });
        dialog.show();
    }
}