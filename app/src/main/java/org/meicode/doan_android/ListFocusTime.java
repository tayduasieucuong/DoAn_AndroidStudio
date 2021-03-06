package org.meicode.doan_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListFocusTime extends AppCompatActivity {
    ArrayList<String> list;
    ListFocusAdapter adapter;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userid;
    ActionBar actionBar;
    ImageView btn_goto;
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
        actionBar.setTitle("Qu???n l?? th???i gian");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(3);
    }
    public interface onDeleteListener{
        public void onDelete(String data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_focus_time);
        setActionBar();
        initView();
        getData();
        list = new ArrayList<String>();
        adapter = new ListFocusAdapter(this,R.layout.item_list_focus,list,new onDeleteListener(){
            @Override
            public void onDelete(String data){
                AlertDialog.Builder builder = new AlertDialog.Builder(ListFocusTime.this);
                builder.setMessage("B???n c?? ch???c mu???n x??a")
                        .setPositiveButton("X??a", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onDeleteTask(data);
                                Toast.makeText(ListFocusTime.this, "???? x??a "+data, Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        })
                        .setNegativeButton("H???y", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
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
                startActivity(new Intent(ListFocusTime.this,input_time.class));
                finish();
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
    private void onDeleteTask(String name){
        reference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                reference.child(userid).child("FocusTask").child(name).removeValue();
                getTimeToNotify();
                String randomKey = randomString(15);
                reference.child(userid).child("Notification").child(randomKey).child("Content").setValue("???? x??a \""+ name +"\" kh???i qu???n l?? c??ng vi???c");
                reference.child(userid).child("Notification").child(randomKey).child("Time").setValue(DateNotify);
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
    private void onReadTask(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(userid.equals(snapshot.getKey().toString()))
                {
                    try {
                        for (DataSnapshot ds : snapshot.child("FocusTask").getChildren())
                        {
                            String content = ds.getKey().toString();
                            String time = ds.child("T???ng th???i gian").getValue().toString();
                            String percent = ds.child("Ho??n th??nh ???????c").getValue().toString();
                            list.add(content+"/"+time+"/"+percent);
                        }
                    }catch (Exception ex){
                        Toast.makeText(ListFocusTime.this, "Wait a second to load data !", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            startActivity(new Intent(ListFocusTime.this,TaskManagement.class));
            finish();
        }
        return true;
    }
}