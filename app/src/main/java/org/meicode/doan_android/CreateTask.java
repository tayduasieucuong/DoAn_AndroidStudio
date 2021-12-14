package org.meicode.doan_android;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

@RequiresApi(api = Build.VERSION_CODES.N)
public class CreateTask extends AppCompatActivity {
    public static String edt_tittle, edt_message;
    public static final int NOTIFICICATION_ID = getNotificationID();
    Intent intent;
    PendingIntent notifyPendingIntent;
    AlarmManager alarmManager;
    ImageView btn_add;
    ImageView btn_star;
    ImageView btn_important;
    int important;
    String date_string;
    Button tv_timestart;
    Button tv_timeend;
    EditText et_des;
    CheckBox checkBox;
    EditText et_title;
    TextView tv_nhacnho;
    NotificationManager notificationManager;
    ImageView btn_cross;
    FirebaseDatabase database;
    DatabaseReference reference;
    Calendar date;
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
        tv_nhacnho=(TextView) findViewById(R.id.tv_nhacnho);
        et_des = (EditText) findViewById(R.id.ps_editTextTextMultiLine);
        btn_cross = (ImageView)findViewById(R.id.btn_close_calender);
        et_title = (EditText)findViewById(R.id.et_title);
        checkBox=(CheckBox)findViewById(R.id.checkBox2);
        btn_star = (ImageView) findViewById(R.id.ps_favorite);
        important = 0;
        btn_important = (ImageView)findViewById(R.id.ps_importain);
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
        itemSpinner.add("Theo ngày");
        itemSpinner.add("Theo tuần");
        itemSpinner.add("Theo tháng");
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
        createNotification();
    }

    private void onPushTask(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(userid.equals(snapshot.getKey()))
                {
                    String repeat="";
                    String favorite = "";
                    String Important = "";
                    if(checkBox.isChecked()){
                        repeat = "Có";
                    }else{
                        repeat = "Không";
                    }
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
                    dr.child("Detail").child("Lặp lại").setValue(spinnerRemind.getSelectedItem().toString());
                    dr.child("Detail").child("Yêu thích").setValue(favorite);
                    dr.child("Detail").child("Quan trọng").setValue(Important);
                    dr.child("Detail").child("Ngày bắt đầu").setValue(tv_timestart.getText().toString());
                    dr.child("Detail").child("Ngày kết thúc").setValue(tv_timeend.getText().toString());
                    dr.child("Detail").child("Nhắc nhở").setValue(repeat);
                    dr.child("Detail").child("Danh sách").setValue(spinnerList.getSelectedItem().toString());
                    dr.child("Detail").child("Trạng thái").setValue("Chưa xong");
                    dr.child("Detail").child("ID notification").setValue(NOTIFICICATION_ID);
                    dr.child("Detail").child("Thời gian nhắc nhở").setValue(tv_nhacnho.getText().toString());
                    if(btn_ic_star==1)
                    {
                        dr2.child("Detail").child("Mô tả").setValue(et_des.getText().toString());
                        dr2.child("Detail").child("Lặp lại").setValue(spinnerRemind.getSelectedItem().toString());
                        dr2.child("Detail").child("Yêu thích").setValue(favorite);
                        dr2.child("Detail").child("Quan trọng").setValue(Important);
                        dr2.child("Detail").child("Ngày bắt đầu").setValue(tv_timestart.getText().toString());
                        dr2.child("Detail").child("Ngày kết thúc").setValue(tv_timeend.getText().toString());
                        dr2.child("Detail").child("Nhắc nhở").setValue(repeat);
                        dr2.child("Detail").child("Danh sách").setValue(spinnerList.getSelectedItem().toString());
                        dr2.child("Detail").child("Trạng thái").setValue("Chưa xong");
                        dr2.child("Detail").child("ID notification").setValue(NOTIFICICATION_ID);
                        dr2.child("Detail").child("Thời gian nhắc nhở").setValue(tv_nhacnho.getText().toString());
                    }
                    if (important==1)
                    {
                        dr3.child("Detail").child("Mô tả").setValue(et_des.getText().toString());
                        dr3.child("Detail").child("Lặp lại").setValue(spinnerRemind.getSelectedItem().toString());
                        dr3.child("Detail").child("Yêu thích").setValue(favorite);
                        dr3.child("Detail").child("Quan trọng").setValue(Important);
                        dr3.child("Detail").child("Ngày bắt đầu").setValue(tv_timestart.getText().toString());
                        dr3.child("Detail").child("Ngày kết thúc").setValue(tv_timeend.getText().toString());
                        dr3.child("Detail").child("Nhắc nhở").setValue(repeat);
                        dr3.child("Detail").child("Danh sách").setValue(spinnerList.getSelectedItem().toString());
                        dr3.child("Detail").child("Trạng thái").setValue("Chưa xong");
                        dr3.child("Detail").child("ID notification").setValue(NOTIFICICATION_ID);
                        dr3.child("Detail").child("Thời gian nhắc nhở").setValue(tv_nhacnho.getText().toString());
                    }
                    Toast.makeText(CreateTask.this, "Add task Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateTask.this,TaskManagement.class));
                    finish();
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
                final Calendar currentDate = android.icu.util.Calendar.getInstance();
                new DatePickerDialog(CreateTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        tv_timestart.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                    }
                }, currentDate.get(android.icu.util.Calendar.YEAR), currentDate.get(android.icu.util.Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });
        tv_timeend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar currentDate = android.icu.util.Calendar.getInstance();
                new DatePickerDialog(CreateTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        tv_timeend.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                    }
                }, currentDate.get(android.icu.util.Calendar.YEAR), currentDate.get(android.icu.util.Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPushTask();
                if (checkBox.isChecked()) {
                    scheduleNotification();
                }else {
                    cancelAlarm();
                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tv_nhacnho.setEnabled(true);
                }
                else tv_nhacnho.setEnabled(false);
            }
        });
        tv_nhacnho.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    showDateTimePicker();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                date_string=dayOfMonth+"/"+monthOfYear+"/"+year;
                new TimePickerDialog(CreateTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        date_string+="  |  "+hourOfDay+":"+minute;
                        tv_nhacnho.setText(date_string);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
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
            case android.R.id.home: {
                startActivity(new Intent(CreateTask.this,TaskManagement.class));
                finish();
                return true;
            }
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes audioAttributes=new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Receiver.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(uri,audioAttributes);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void scheduleNotification() {
        intent=new Intent(getApplicationContext(),Receiver.class);
        notifyPendingIntent=PendingIntent.getBroadcast(getApplicationContext(),NOTIFICICATION_ID,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        edt_tittle=et_title.getText().toString();
        edt_message=et_des.getText().toString();
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,getTime(),notifyPendingIntent);
        switch (spinnerRemind.getSelectedItem().toString()){
            case "Không":
                break;
            case "Theo ngày":
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,getTime(),AlarmManager.INTERVAL_DAY,notifyPendingIntent);
                break;
            case "Theo tuần":
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,getTime(),AlarmManager.INTERVAL_DAY*7,notifyPendingIntent);
                break;
            case "Theo tháng":
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,getTime(),AlarmManager.INTERVAL_DAY*30,notifyPendingIntent);
                break;
        }
    }

    private long getTime() {
        if(Calendar.getInstance().after(date)){
            date.add(Calendar.DAY_OF_MONTH,1);
        }
        return  date.getTimeInMillis();
    }
    public static int getNotificationID(){
        return (int) new Date().getTime();
    }
    private void cancelAlarm() {
        intent=new Intent(this, Receiver.class);
        notifyPendingIntent=PendingIntent.getBroadcast(this,NOTIFICICATION_ID,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        if(alarmManager==null){
            alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(notifyPendingIntent);
        notificationManager.cancel(NOTIFICICATION_ID);
    }
}
