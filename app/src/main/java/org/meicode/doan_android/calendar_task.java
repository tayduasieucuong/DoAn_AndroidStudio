package org.meicode.doan_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class calendar_task extends AppCompatActivity{

    ImageView nextMonth,backMonth;
    TextView textView;
    ListView listView;
    CompactCalendarView compactCalendar;
    FirebaseDatabase database;
    DatabaseReference mData, rf;
    FirebaseAuth mAuth;
    SimpleDateFormat sdf;
    String userid;
    String date,message;
    Date date1;
    Event ev;
    ArrayList<Event_Calendar> arr=new ArrayList<>();
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Calendar");
        textView=(TextView) findViewById(R.id.monthYearTV);
        listView=(ListView) findViewById(R.id.lv_event);
        nextMonth=findViewById(R.id.n_event);
        backMonth=findViewById(R.id.b_event);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mData = database.getReference("Users");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID", null);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        readTasks();
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                ArrayList<Event_Calendar> list=new ArrayList<>();
                for (int i=0;i<arr.size();i++){
                    if(arr.get(i).getDate().compareTo(dateClicked)==0){
                        list.add(arr.get(i));
                    }
                }
                EventAdapter customAdapter=new EventAdapter(calendar_task.this,list);
                listView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                textView.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
        backMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendar.scrollLeft();
            }
        });
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendar.scrollRight();
            }
        });
    }

    private void readTasks() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.child(userid).child("Tasks").child("Tất cả công việc").getChildren()){
                    date=dataSnapshot.child("Detail").child("Ngày kết thúc").getValue().toString();
                    message=dataSnapshot.child("Detail").child("Mô tả").getValue().toString();
                    try {
                        date1=sdf.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ev = new Event(Color.RED,date1.getTime(),dataSnapshot.getKey());
                    compactCalendar.addEvent(ev);
                    arr.add(new Event_Calendar(date1,dataSnapshot.getKey(),message));
                    for (DataSnapshot dataChild:dataSnapshot.child("TasksChild").getChildren()){
                        date=dataChild.child("Detail").child("Ngày kết thúc").getValue().toString();
                        message=dataChild.child("Detail").child("Mô tả").getValue().toString();
                        try {
                            date1=sdf.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        ev = new Event(Color.RED,date1.getTime(),dataChild.getKey());
                        compactCalendar.addEvent(ev);
                        arr.add(new Event_Calendar(date1,dataChild.getKey(),message));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mData.addValueEventListener(valueEventListener);
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