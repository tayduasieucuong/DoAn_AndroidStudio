package org.meicode.doan_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class CreateTask extends AppCompatActivity {
    ImageView btn_add;
    ImageView btn_star;
    TextView tv_timestart;
    TextView tv_timeend;
    EditText et_des;
    EditText et_title;
    TextView tv_repeat;
    CalendarView calendarView;
    CalendarView calendarViewEnd;
    ImageView btn_cross;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userid;
    int btn_ic_star = 0;
    public void initView(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_add = (ImageView) findViewById(R.id.btn_add);
        tv_timeend = (TextView) findViewById(R.id.tv_timeend);
        tv_timestart = (TextView) findViewById(R.id.tv_timestart);
        et_des = (EditText) findViewById(R.id.et_description);
        tv_repeat = (TextView) findViewById(R.id.tv_repeat);
        btn_cross = (ImageView)findViewById(R.id.btn_cross);
        et_title = (EditText)findViewById(R.id.et_title);
        btn_star = (ImageView) findViewById(R.id.ic_star);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarViewEnd = (CalendarView) findViewById(R.id.calendarViewEnd);
        calendarViewEnd.setVisibility(View.INVISIBLE);
        calendarView.setVisibility(View.INVISIBLE);
        btn_cross.setVisibility(View.INVISIBLE);
        //Get uid
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        initView();
        onClick();
    }
    private void onPushTask(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(userid.equals(snapshot.getKey()))
                {
                    String favorite = "";
                    if(btn_ic_star==0)
                        favorite = "Không";
                    else
                        favorite = "Có";
                    if(et_title==null || TextUtils.isEmpty(et_title.getText()))
                    {
                        Toast.makeText(CreateTask.this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        int rs = compareDate();
                        if (rs == 0 || rs > 0)
                        {
                            Toast.makeText(CreateTask.this, "Chọn ngày sai !!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(CreateTask.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DatabaseReference dr2 = reference.child(snapshot.getKey()).child("Tasks").child("Công việc yêu thích").child(et_title.getText().toString());
                    DatabaseReference dr = reference.child(snapshot.getKey()).child("Tasks").child("Tất cả công việc").child(et_title.getText().toString());
                    dr.child("Detail").child("Mô tả").setValue(et_des.getText().toString());
                    dr.child("Detail").child("Lặp lại").setValue(tv_repeat.getText());
                    dr.child("Detail").child("Yêu thích").setValue(favorite);
                    if(btn_ic_star==1)
                    {
                        dr2.child("Detail").child("Mô tả").setValue(et_des.getText().toString());
                        dr2.child("Detail").child("Lặp lại").setValue(tv_repeat.getText());
                        dr2.child("Detail").child("Yêu thích").setValue(favorite);
                    }
                    Toast.makeText(CreateTask.this, "Add task Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateTask.this,TaskManagement.class));
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
    private void onClick()
    {
        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_ic_star==0) {
                    btn_star.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_ic_star=1;
                }
                else {
                    btn_star.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_ic_star=0;
                }
            }
        });
        tv_timestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setVisibility(View.VISIBLE);
                btn_cross.setVisibility(View.VISIBLE);
            }
        });
        tv_timeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarViewEnd.setVisibility(View.VISIBLE);
                btn_cross.setVisibility(View.VISIBLE);
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                tv_timestart.setText((day+"/"+month+1+'/'+year));
            }
        });
        calendarViewEnd.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                tv_timeend.setText((day+"/"+month+1+'/'+year));
            }
        });
        btn_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setVisibility(View.INVISIBLE);
                calendarViewEnd.setVisibility(View.INVISIBLE);
                btn_cross.setVisibility(View.INVISIBLE);
            }
        });
        tv_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_repeat.getText().toString().equals("Không"))
                {
                    tv_repeat.setText("Có");
                }
                else
                {
                    tv_repeat.setText("Không");
                }
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPushTask();
            }
        });

    }
    private int compareDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf.parse(tv_timestart.getText().toString());
        Date date2 = sdf.parse(tv_timeend.getText().toString());
        int result = date1.compareTo(date2);
        return result;
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( (item.getItemId()))
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }
}