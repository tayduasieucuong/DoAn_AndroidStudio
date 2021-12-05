package org.meicode.doan_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Date;

public class CreateTask extends AppCompatActivity {
    ImageView btn_add;
    ImageView btn_star;
    ImageView btn_important;
    int important;
    Button tv_timestart;
    Button tv_timeend;
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
    Spinner spinnerRemind;
    Spinner spinnerList;
    ArrayList<String> itemListSpinner = new ArrayList<String>();
    ArrayList<String> itemSpinner = new ArrayList<String>();
    public void initView(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_add = (ImageView) findViewById(R.id.ps_save);
        tv_timeend = (Button) findViewById(R.id.ps_btnend);
        tv_timestart = (Button) findViewById(R.id.ps_btnstart);
        et_des = (EditText) findViewById(R.id.ps_editTextTextMultiLine);
        tv_repeat = (TextView) findViewById(R.id.ps_laplai);
        btn_cross = (ImageView)findViewById(R.id.btn_close_calender);
        et_title = (EditText)findViewById(R.id.et_title);
        btn_star = (ImageView) findViewById(R.id.ps_favorite);
        important = 0;
        btn_important = (ImageView)findViewById(R.id.ps_importain);
        calendarView = (CalendarView) findViewById(R.id.calendarView2);
        calendarViewEnd = (CalendarView) findViewById(R.id.calendarViewEnd);
        calendarViewEnd.setVisibility(View.INVISIBLE);
        calendarView.setVisibility(View.INVISIBLE);
        btn_cross.setVisibility(View.INVISIBLE);
        spinnerRemind = (Spinner)findViewById(R.id.ps_nhacnho);
        spinnerList= (Spinner) findViewById(R.id.ps_list);
        //Get uid
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
    }
    private void displaySpinner(){
        itemSpinner.add("Không");
        itemSpinner.add("Nhắc nhở mỗi ngày");
        itemSpinner.add("Nhắc nhở mỗi tuần");
        itemSpinner.add("Nhắc nhở mỗi tháng");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,itemSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRemind.setAdapter(adapter);
        itemListSpinner.add("Không");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,itemListSpinner);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerList.setAdapter(adapter1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        initView();
        displaySpinner();
        onClick();
    }
    private void onPushTask(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(userid.equals(snapshot.getKey()))
                {
                    String favorite = "";
                    String Important = "";
                    if(important==0)
                        Important = "Không";
                    else
                        Important = "Có";
                    if(btn_ic_star==0)
                        favorite = "Không";
                    else
                        favorite = "Có";
                    //Check information
                    if(et_title==null || TextUtils.isEmpty(et_title.getText()))//Validate Title
                    {
                        Toast.makeText(CreateTask.this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {//Validate Date
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
                    DatabaseReference dr3 = reference.child(snapshot.getKey()).child("Tasks").child("Công việc quan trọng").child(et_title.getText().toString());
                    DatabaseReference dr2 = reference.child(snapshot.getKey()).child("Tasks").child("Công việc yêu thích").child(et_title.getText().toString());
                    DatabaseReference dr = reference.child(snapshot.getKey()).child("Tasks").child("Tất cả công việc").child(et_title.getText().toString());
                    dr.child("Detail").child("Mô tả").setValue(et_des.getText().toString());
                    dr.child("Detail").child("Lặp lại").setValue(tv_repeat.getText());
                    dr.child("Detail").child("Yêu thích").setValue(favorite);
                    dr.child("Detail").child("Quan trọng").setValue(Important);
                    dr.child("Detail").child("Ngày bắt đầu").setValue(tv_timestart.getText().toString());
                    dr.child("Detail").child("Ngày kết thúc").setValue(tv_timeend.getText().toString());
                    dr.child("Detail").child("Nhắc nhở").setValue(spinnerRemind.getSelectedItem()).toString();
                    dr.child("Detail").child("Danh sách").setValue(spinnerList.getSelectedItem().toString());
                    if(btn_ic_star==1)
                    {
                        dr2.child("Detail").child("Mô tả").setValue(et_des.getText().toString());
                        dr2.child("Detail").child("Lặp lại").setValue(tv_repeat.getText());
                        dr2.child("Detail").child("Yêu thích").setValue(favorite);
                        dr2.child("Detail").child("Quan trọng").setValue(Important);
                        dr2.child("Detail").child("Ngày bắt đầu").setValue(tv_timestart.getText().toString());
                        dr2.child("Detail").child("Ngày kết thúc").setValue(tv_timeend.getText().toString());
                        dr2.child("Detail").child("Nhắc nhở").setValue(spinnerRemind.getSelectedItem()).toString();
                        dr2.child("Detail").child("Danh sách").setValue(spinnerList.getSelectedItem().toString());
                    }
                    if (important==1)
                    {
                        dr3.child("Detail").child("Mô tả").setValue(et_des.getText().toString());
                        dr3.child("Detail").child("Lặp lại").setValue(tv_repeat.getText());
                        dr3.child("Detail").child("Yêu thích").setValue(favorite);
                        dr3.child("Detail").child("Quan trọng").setValue(Important);
                        dr3.child("Detail").child("Ngày bắt đầu").setValue(tv_timestart.getText().toString());
                        dr3.child("Detail").child("Ngày kết thúc").setValue(tv_timeend.getText().toString());
                        dr3.child("Detail").child("Nhắc nhở").setValue(spinnerRemind.getSelectedItem()).toString();
                        dr3.child("Detail").child("Danh sách").setValue(spinnerList.getSelectedItem().toString());
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
        btn_important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(important==0)
                {
                    btn_important.setImageResource(R.drawable.ic_baseline_radio_button_checked_24);
                    important=1;
                }
                else
                {
                    btn_important.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                    important=0;
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
                month++;
                tv_timestart.setText((day+"/"+month+'/'+year));
            }
        });
        calendarViewEnd.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                tv_timeend.setText((day+"/"+month+'/'+year));
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
                    tv_repeat.setTextColor(Color.parseColor("#64dd17"));
                }
                else
                {
                    tv_repeat.setText("Không");
                    tv_repeat.setTextColor(Color.parseColor("#ff0000"));
                }
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPushTask();
            }
        });
        //onClick Spinner item
        spinnerRemind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Imple
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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