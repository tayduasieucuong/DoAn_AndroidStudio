package org.meicode.doan_android;

import static android.text.InputType.TYPE_NULL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class DetailTask extends AppCompatActivity {
    int IDNoti;
    Intent intent;
    DatabaseReference mData;
    PendingIntent notifyPendingIntent;
    AlarmManager alarmManager;
    ImageView btn_add;
    ImageView btn_star;
    ImageView btn_important;
    ImageView btn_delete;
    int important;
    String tg_time;
    String date_string;
    Button tv_timestart;
    Button tv_timeend;
    EditText et_des;
    CheckBox checkBox;
    EditText et_title;
    TextView tv_repeat;
    TextView tv_nhacnho;
    String NameOfTask;
    FirebaseDatabase database;
    DatabaseReference reference,rf;
    NotificationManager notificationManager;
    Calendar date;
    String userid;
    String Name;
    int btn_ic_star = 0;
    Spinner spinnerRemind;
    ArrayList<String> itemSpinner = new ArrayList<String>();
    public void initView(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("Name")!= null)
        {
            Name=bundle.getString("Name");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_add = (ImageView) findViewById(R.id.ps_save);
        tv_timeend = (Button) findViewById(R.id.ps_btnend);
        tv_timestart = (Button) findViewById(R.id.ps_btnstart);
        tv_nhacnho=(TextView) findViewById(R.id.tv_nhacnho);
        et_des = (EditText) findViewById(R.id.ps_editTextTextMultiLine);
        et_title = (EditText)findViewById(R.id.et_title);
        et_title.setFocusable(false);
        et_title.setFocusableInTouchMode(false);
        checkBox=(CheckBox)findViewById(R.id.checkBox2);
        btn_star = (ImageView) findViewById(R.id.ps_favorite);
        btn_delete =(ImageView) findViewById(R.id.ps_delete);
        important = 0;
        btn_important = (ImageView)findViewById(R.id.ps_importain);
        spinnerRemind = (Spinner)findViewById(R.id.ps_nhacnho);
        //Get uid
        mData = FirebaseDatabase.getInstance().getReference();
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

    }
    private  void LoadData() {
        et_title.setText(Name);
        rf= mData.child("Users").child(userid).child("Tasks").child("Tất cả công việc").child(Name).child("Detail");
        rf.child("Mô tả").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                et_des.setText(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rf.child("ID notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                IDNoti=Integer.parseInt(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rf.child("Thời gian nhắc nhở").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_nhacnho.setText(snapshot.getValue().toString());
                tg_time=snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rf.child("Lặp lại").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch(snapshot.getValue().toString()) {
                    case "Không":
                        spinnerRemind.setSelection(0);
                        break;
                    case "Theo ngày":
                        spinnerRemind.setSelection(1);
                        break;
                    case "Theo tuần":
                        spinnerRemind.setSelection(2);
                        break;
                    case "Theo tháng":
                        spinnerRemind.setSelection(3);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rf.child("Ngày bắt đầu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_timestart.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rf.child("Ngày kết thúc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tv_timeend.setText(snapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rf.child("Nhắc nhở").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals("Không")){
                    checkBox.setChecked(false);
                }
                else{
                    checkBox.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rf.child("Quan trọng").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("Có")){
                    important =1;
                    btn_important.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                }
                else {
                    important=0;
                    btn_important.setImageResource(R.drawable.ic_baseline_radio_button_checked_24);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rf.child("Yêu thích").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("Có")){
                    btn_star.setImageResource(R.drawable.ic_baseline_star_24);
                    btn_ic_star=1;
                }
                else {
                    btn_star.setImageResource(R.drawable.ic_baseline_star_border_24);
                    btn_ic_star=0;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        initView();
        displaySpinner();
        LoadData();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                NameOfTask = null;
            } else {
                NameOfTask = extras.getString("Name");
            }
        }
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
                    if(checkBox.isChecked()){
                        repeat = "Có";
                    }else{
                        repeat = "Không";
                    }
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
                        Toast.makeText(DetailTask.this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {//Validate Date
                        int rs = compareDate();
                        if (rs == 0 || rs > 0)
                        {
                            Toast.makeText(DetailTask.this, "Chọn ngày sai !!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailTask.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
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
                    dr.child("Detail").child("Trạng thái").setValue("Chưa xong");
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
                        dr2.child("Detail").child("Trạng thái").setValue("Chưa xong");
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
                        dr3.child("Detail").child("Trạng thái").setValue("Chưa xong");
                        dr3.child("Detail").child("Thời gian nhắc nhở").setValue(tv_nhacnho.getText().toString());
                    }
                    Toast.makeText(DetailTask.this, "Add task Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailTask.this,TaskManagement.class));
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar currentDate = android.icu.util.Calendar.getInstance();
                new DatePickerDialog(DetailTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        tv_timestart.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                    }
                }, currentDate.get(android.icu.util.Calendar.YEAR), currentDate.get(android.icu.util.Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });
        tv_timeend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar currentDate = android.icu.util.Calendar.getInstance();
                new DatePickerDialog(DetailTask.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        tv_timeend.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                    }
                }, currentDate.get(android.icu.util.Calendar.YEAR), currentDate.get(android.icu.util.Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailTask.this);
                builder.setMessage("Bạn có chắc chắn muốn xóa không?").setPositiveButton("Có", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                onPushTask();
                if (checkBox.isChecked()) {
                    if (!tv_nhacnho.getText().equals(tg_time))
                    scheduleNotification();
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        cancelAlarm();
                    }
                }
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
                new TimePickerDialog(DetailTask.this, new TimePickerDialog.OnTimeSetListener() {
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
                startActivity(new Intent(DetailTask.this,TaskManagement.class));
                finish();
                return true;
            }
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            AudioAttributes audioAttributes=new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel("Notification", name, importance);
//            channel.setDescription(description);
//            channel.setSound(uri,audioAttributes);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
              NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void scheduleNotification() {
        intent=new Intent(getApplicationContext(),Receiver.class);
        notifyPendingIntent=PendingIntent.getBroadcast(getApplicationContext(), 1,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,getTime(),notifyPendingIntent);
        }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private long getTime() {
        if(Calendar.getInstance().after(date)){
            date.add(Calendar.DAY_OF_MONTH,1);
        }
        return  date.getTimeInMillis();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void cancelAlarm() {
//        intent=new Intent(this, Receiver.class);
//        notifyPendingIntent=PendingIntent.getBroadcast(this,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
//        if(alarmManager==null){
//            alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        }
//        alarmManager.cancel(notifyPendingIntent);
//        notificationManager.cancel(IDNoti);
        notificationManager.deleteNotificationChannel("Notification");
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Intent int1 = new Intent(DetailTask.this, TaskManagement.class);
                    startActivity(int1);
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
                        Toast.makeText(DetailTask.this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {//Validate Date
                        int rs = compareDate();
                        if (rs == 0 || rs > 0)
                        {
                            Toast.makeText(DetailTask.this, "Chọn ngày sai !!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(DetailTask.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DatabaseReference dr3 = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Tasks").child("Công việc quan trọng").child(et_title.getText().toString());
                    DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Tasks").child("Công việc yêu thích").child(et_title.getText().toString());
                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Tasks").child("Tất cả công việc").child(et_title.getText().toString());
                    dr.removeValue();

                    if(btn_ic_star==1)
                    {
                        dr2.removeValue();
                    }
                    if (important==1)
                    {
                        dr3.removeValue();
                    }
                    Toast.makeText(DetailTask.this, "Delete task Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailTask.this,TaskManagement.class));
                    finish();

                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
}