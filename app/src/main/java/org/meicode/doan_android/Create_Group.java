package org.meicode.doan_android;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class Create_Group extends AppCompatActivity {
    ArrayList<String> list;
    ListCreateGrAdapter adapter;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference reference,rf;
    String userid;
    ActionBar actionBar;
    ImageView btn_goto;
    ArrayList<SearchModel> item = new ArrayList<>();
    private void getData(){
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
    }
    private void initView(){
        btn_goto = (ImageView) findViewById(R.id.btn_add_goto);
    }
    private void setActionBar()
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("Quản lý thời gian");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(3);
    }
    public interface onDeleteListener{
        public void onDelete(int data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setActionBar();
        initView();
        getData();
        list = new ArrayList<String>();
        adapter = new ListCreateGrAdapter(this,R.layout.item_list_person,list,new onDeleteListener(){
            @Override
            public void onDelete(int data) {
                list.remove(data);
                adapter.notifyDataSetChanged();
            }
        });
        listView = (ListView) findViewById(R.id.listFocus);
        listView.setAdapter(adapter);
        onReadTask();
        onClick();
    }
    private void onClick(){
        btn_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SimpleSearchDialogCompat(Create_Group.this,"Search","Gmail of person",null,initData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        SearchTask(searchable.getTitle());
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });
    }
    String DateNotify;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getTimeToNotify(){
        Date date = Calendar.getInstance(TimeZone.getTimeZone("UTC+7")).getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateNotify = dateFormat.format(date);
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
    private void onReadTask(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    item.add(new SearchModel(ds.child("UserInfo").child("Email").getValue().toString()));
                }
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
            startActivity(new Intent(Create_Group.this,ListGroup.class));
            finish();
        }
        return true;
    }
    private ArrayList<SearchModel> initData() {
        return item;
    }
    //Menu Bottom Event
    private void SearchTask(String query){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {

                        if (query.equals(snapshot.child("UserInfo").child("Email").getValue().toString())){
                            for (int i =0;i<list.size();i++){
                                String[] data = list.get(i).toString().split("/",2);
                                if (query.equals(data[0])){
                                    Toast.makeText(Create_Group.this, "Tên đã có trong danh sách !", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            String name = query;
                            String id_person = snapshot.getKey().toString();
                            list.add(name+"/"+id_person);
                        }

                }catch (Exception ex){
                    Toast.makeText(Create_Group.this, "Wait a second to load data !", Toast.LENGTH_SHORT).show();
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
}