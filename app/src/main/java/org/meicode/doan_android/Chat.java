package org.meicode.doan_android;

import static java.util.Collections.swap;

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
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat extends AppCompatActivity {
    ArrayList<String> list;
    ArrayList<Integer> listTime;
    ApdapterChat adapter;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference reference,rf1,rf2;
    String userid;
    ActionBar actionBar;
    ImageView btn_goto;
    String title,idGroup;
    EditText ed_text;
    DateFormat dateFormat;
    private void getData(){
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Groups");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
    }
    private void initView(){
        btn_goto = (ImageView) findViewById(R.id.btn_add_goto);
        ed_text =(EditText) findViewById(R.id.messageText);
    }
    private void setActionBar()
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        Intent intent = getIntent();
        title = intent.getStringExtra("Name");
        idGroup = intent.getStringExtra("idGroup");
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle(title);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(3);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setActionBar();
        initView();
        getData();
        list = new ArrayList<String>();
        listTime = new ArrayList<Integer>();
        adapter = new ApdapterChat(this,R.layout.item_chat,list);
        listView = (ListView) findViewById(R.id.listFocus);
        listView.setAdapter(adapter);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        onReadTask();
        onClick();
        action();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClick(){
        btn_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ed_text.getText().toString();
                getTimeToNotify();
                String randomKey = randomString(15);
                reference.child(idGroup).child("Thành viên").child(userid).child("Chat").child(randomKey).child("Nội dung").setValue(text);
                reference.child(idGroup).child("Thành viên").child(userid).child("Chat").child(randomKey).child("Thời gian").setValue(DateNotify);
                ed_text.setText("");

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
        list.clear();
        listTime.clear();
        rf1 = database.getReference("Groups");
        rf1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(idGroup.equals(snapshot.getKey().toString()))
                {
                    try {
                        for (DataSnapshot ds : snapshot.child("Thành viên").getChildren())
                        {
                            String name = ds.child("Info").child("Họ và tên").getValue().toString();
                            for (DataSnapshot ds1 : ds.getChildren()) {
                                if (ds1.getKey().equals("Chat")){
                                    for (DataSnapshot ds2 : ds1.getChildren()) {
                                        String st1 = ds2.child("Nội dung").getValue().toString();
                                        String st2 = ds2.child("Thời gian").getValue().toString();
                                        list.add(st1+"~~"+st2+"~~"+name);
                                        Date datetg= dateFormat.parse(st2);
                                        listTime.add((int) datetg.getTime());
                                        for (int i=listTime.size()-1; i>0; i--) {
                                            if(listTime.get(i)<listTime.get(i-1)){
                                                swap(listTime,i,i-1);
                                                swap(list,i,i-1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }catch (Exception ex){
                        Toast.makeText(Chat.this, "Wait a second to load data !", Toast.LENGTH_SHORT).show();
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
    public void action(){
        rf2 = database.getReference("Groups").child(idGroup).child("Thành viên");
        rf2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                onReadTask();
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
            Intent intent = new Intent(Chat.this,HomePageGroup.class);
            intent.putExtra("IDTASK", idGroup);
            intent.putExtra("NameTaskGroup", title);
            startActivity(intent);
            finish();
        }
        return true;
    }
}