package org.meicode.doan_android;

import static java.util.Collections.swap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
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

public class Notification extends AppCompatActivity {
    ActionBar actionBar;
    ListView listView;
    ArrayList list;
    AdapterNotification adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userid;

    private void initView(){
        listView = (ListView) findViewById(R.id.listviewNotifi);
    }
    private void getData(){
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
    }

    private void setActionBar()
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        actionBar.setTitle("Thông báo");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(3);
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
    public interface onDeleteNotification{
        public void onDelete(String data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setActionBar();
        initView();
        getData();
        list = new ArrayList<String>();
        adapter = new AdapterNotification(Notification.this,R.layout.item_listviewnotifi,list, new onDeleteNotification(){
            @Override
            public void onDelete(String data){
                AlertDialog.Builder builder = new AlertDialog.Builder(Notification.this);
                builder.setMessage("Bạn có chắc muốn xóa")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String[] DataSplit = data.split(",");
                                onDeleteNotifi(DataSplit[1]);
                                Toast.makeText(Notification.this, "Đã xóa "+DataSplit[0], Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            }
        });
        onLoadData();
        listView.setAdapter(adapter);
    }
    private void onDeleteNotifi(String data){
        reference.child(userid).child("Notification").child(data).removeValue();
    }
    private String findNearsestTime(ArrayList<String> listTime){

        return "";
    }
    private void onLoadData(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        ArrayList<Long> listTime = new ArrayList<>();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ArrayList<String> listTemp = new ArrayList<String>();
                if(userid.equals(snapshot.getKey()))
                {
                    try {
                        for (DataSnapshot ds : snapshot.child("Notification").getChildren())
                        {
                            list.add(ds.child("Content").getValue().toString()+","+ds.child("Time").getValue().toString()+","+ds.getKey().toString());
                            Date datetg= dateFormat.parse(ds.child("Time").getValue().toString());
                            listTime.add(datetg.getTime());
                            for (int i=listTime.size()-1; i>0; i--) {
                                if(listTime.get(i)>listTime.get(i-1)){
                                    swap(listTime,i,i-1);
                                    swap(list,i,i-1);
                                }
                            }
                        }
                    }catch (Exception ex)
                    {
                        Toast.makeText(Notification.this, "Wait a second to load data !", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(this,TaskManagement.class));
            finish();
        }
        return true;
    }
}