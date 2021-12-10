package org.meicode.doan_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskChildPerson extends AppCompatActivity {
    String NameOfTask;
    String userid;
    FirebaseDatabase database;
    DatabaseReference reference;
    ActionBar actionBar;
    ImageButton btn_back;
    ImageButton btn_save;
    EditText et_title;
    EditText et_descript;
    String headerTitle;
    Button btn_timestart;
    Button btn_timend;
    ImageView btn_close_calendar;
    CalendarView calendarView;
    CalendarView calendarViewEnd;
    private void initView(){
        Intent intent = getIntent();
        headerTitle = intent.getStringExtra("HeaderTitle");
        btn_back = (ImageButton) findViewById(R.id.ps_back);
        btn_save = (ImageButton) findViewById(R.id.ps_save);
        et_title = (EditText) findViewById(R.id.et_title);
        et_descript = (EditText) findViewById(R.id.ps_editTextTextMultiLine);
        btn_timend = (Button) findViewById(R.id.ps_btnend);
        btn_timestart = (Button) findViewById(R.id.ps_btnstart);
        calendarView = (CalendarView) findViewById(R.id.calendarView2);
        calendarViewEnd = (CalendarView) findViewById(R.id.calendarViewEnd);
        calendarView.setVisibility(View.GONE);
        calendarViewEnd.setVisibility(View.GONE);
        btn_close_calendar = (ImageView) findViewById(R.id.btn_close_calender);
        btn_close_calendar.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_child_person);
        initView();
        Intent intent = getIntent();
        actionBar = getSupportActionBar();
        actionBar.hide();
        NameOfTask = intent.getStringExtra("NameOfTask");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
        onPushChildTask();
        onClick();
    }
    private int compareDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf.parse(btn_timestart.getText().toString());
        Date date2 = sdf.parse(btn_timend.getText().toString());
        int result = date1.compareTo(date2);
        return result;
    };
    private int validateDate() {
        try {
            int rs = compareDate();
            if (rs == 0 || rs > 0) {
                Toast.makeText(TaskChildPerson.this, "Chọn ngày sai !!!", Toast.LENGTH_SHORT).show();
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(TaskChildPerson.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
            return 2;
        }
        return 0;
    }
    private void onClick(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_title.getText().toString().equals(""))
                {
                    Toast.makeText(TaskChildPerson.this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                    return;
                }else if(validateDate()==1||validateDate()==2){
                    return;
                }
                onPushChildTask();
                Intent intent = new Intent(TaskChildPerson.this,TaskMaster.class);
                intent.putExtra("HeaderTitle",headerTitle);
                startActivity(intent);
                finish();
            }
        });
        btn_timestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setVisibility(View.VISIBLE);
                btn_close_calendar.setVisibility(View.VISIBLE);
            }
        });
        btn_timend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarViewEnd.setVisibility(View.VISIBLE);
                btn_close_calendar.setVisibility(View.VISIBLE);
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                btn_timestart.setText((day+"/"+month+'/'+year));
            }
        });
        calendarViewEnd.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                btn_timend.setText((day+"/"+month+'/'+year));
            }
        });
        btn_close_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setVisibility(View.GONE);
                calendarViewEnd.setVisibility(View.GONE);
                btn_close_calendar.setVisibility(View.GONE);
            }
        });
    }
    private void onPushChildTask()
    {
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(userid.equals(snapshot.getKey()))
                {
                    DatabaseReference dr = reference.child(snapshot.getKey()).child("Tasks").child("Tất cả công việc").child(NameOfTask).child("TasksChild");
                    if(!et_descript.getText().toString().equals("")) {
                        dr.child(et_title.getText().toString()).child("Detail").child("Mô tả").setValue(et_descript.getText().toString());
                        dr.child(et_title.getText().toString()).child("Detail").child("Trạng thái").setValue("Chưa xong");
                        Toast.makeText(TaskChildPerson.this,"Create Task Success",Toast.LENGTH_SHORT).show();
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
}