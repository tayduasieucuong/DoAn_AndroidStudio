package org.meicode.doan_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class Detail_Child_Task extends AppCompatActivity {
    int IDNoti ;
    String NameOfTask;
    Calendar date;
    String userid, edt_tittle, edt_message, date_string;
    FirebaseDatabase database;
    DatabaseReference reference, rf,mData;
    ActionBar actionBar;
    ImageButton btn_back;
    ImageButton btn_save;
    EditText et_title;
    EditText et_descript;
    String headerTitle;
    Button btn_timestart;
    Button btn_timend;
    CheckBox checkBox;
    Spinner spinnerRemind;
    String forwardTo;
    TextView tv_nhacnho;
    Intent intent;
    NotificationManager notificationManager;
    PendingIntent notifyPendingIntent;
    AlarmManager alarmManager;
    Date dateParent;
    String dt;
    SimpleDateFormat sdf;
    ArrayList<String> itemSpinner = new ArrayList<String>();
    ImageView btn_delete;
    String Name;
    private void initView() {
        Intent intent = getIntent();
        headerTitle = intent.getStringExtra("HeaderTitle");
        btn_back = (ImageButton) findViewById(R.id.ps_back);
        btn_save = (ImageButton) findViewById(R.id.ps_save);
        et_title = (EditText) findViewById(R.id.et_title);
        et_descript = (EditText) findViewById(R.id.ps_editTextTextMultiLine);
        btn_timestart = (Button) findViewById(R.id.ps_btnstart);
        btn_timend = (Button) findViewById(R.id.ps_btnend);
        spinnerRemind = (Spinner) findViewById(R.id.ps_nhacnho);
        checkBox = (CheckBox) findViewById(R.id.checkBox2);
        tv_nhacnho = (TextView) findViewById(R.id.tv_nhacnho);
        btn_delete = (ImageButton) findViewById(R.id.ps_delete);
    }

    private void displaySpinner() {
        itemSpinner.add("Không");
        itemSpinner.add("Theo ngày");
        itemSpinner.add("Theo tuần");
        itemSpinner.add("Theo tháng");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRemind.setAdapter(adapter);
    }
    private  void LoadData() {
        et_title.setText(Name);
        rf= mData.child("Users").child(userid).child("Tasks").child("Tất cả công việc").child(NameOfTask).child("TasksChild").child(Name).child("Detail");
        rf.child("Mô tả").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                et_descript.setText(snapshot.getValue().toString());
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
                //tg_time=snapshot.getValue().toString();
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
                btn_timestart.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        rf.child("Ngày kết thúc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                btn_timend.setText(snapshot.getValue().toString());
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_child_task);
        initView();
        Intent intent = getIntent();
        actionBar = getSupportActionBar();
        actionBar.hide();
        forwardTo = intent.getStringExtra("forwardTo");
        NameOfTask = intent.getStringExtra("NameOfTask");
        Name = intent.getStringExtra("NameOfChildTask");
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        mData = FirebaseDatabase.getInstance().getReference();
        userid = sharedPreferences.getString("UID", null);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        getDatePattern();
        displaySpinner();
        LoadData();
        //onPushChildTask();
        onClick();
        createNotification();
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Notification", name, importance);
            channel.setDescription(description);
            channel.setSound(uri, audioAttributes);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void scheduleNotification() {
        intent = new Intent(getApplicationContext(), Receiver.class);
        notifyPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), IDNoti, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        edt_tittle = et_title.getText().toString();
        edt_message = et_descript.getText().toString();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, getTime(), notifyPendingIntent);
        switch (spinnerRemind.getSelectedItem().toString()) {
            case "Không":
                break;
            case "Theo ngày":
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTime(), AlarmManager.INTERVAL_DAY, notifyPendingIntent);
                break;
            case "Theo tuần":
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTime(), AlarmManager.INTERVAL_DAY * 7, notifyPendingIntent);
                break;
            case "Theo tháng":
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTime(), AlarmManager.INTERVAL_DAY * 30, notifyPendingIntent);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private long getTime() {
        if (Calendar.getInstance().after(date)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
        return date.getTimeInMillis();
    }

    public static int getNotificationID() {
        return (int) new Date().getTime();
    }

    private void cancelAlarm() {
        intent = new Intent(this, Receiver.class);
        notifyPendingIntent = PendingIntent.getBroadcast(this, IDNoti, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager == null) {
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(notifyPendingIntent);
        notificationManager.cancel(IDNoti);
    }

    private int compareDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf.parse(btn_timestart.getText().toString());
        Date date2 = sdf.parse(btn_timend.getText().toString());
        int result = date1.compareTo(date2);
        return result;
    }

    ;

    private int compareDate1() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date2 = sdf.parse(btn_timend.getText().toString());
        int result = date2.compareTo(dateParent);
        if (result > 0) {
            Toast.makeText(Detail_Child_Task.this, "Ngày kết thúc đã lớn hơn ngày kết thúc của task cha.Vui lòng điều chỉnh lại!", Toast.LENGTH_SHORT).show();
            return 3;
        }
        return 0;
    }

    ;

    private int validateDate() {
        try {
            int rs = compareDate();
            if (rs == 0 || rs > 0) {
                Toast.makeText(Detail_Child_Task.this, "Chọn ngày sai !!!", Toast.LENGTH_SHORT).show();
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(Detail_Child_Task.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
            return 2;
        }
        return 0;
    }

    private void onClick() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (et_title.getText().toString().equals("")) {
                    Toast.makeText(Detail_Child_Task.this, "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show();
                    return;
                } else if (validateDate() == 1 || validateDate() == 2) {
                    return;
                } else {
                    try {
                        if (compareDate1() == 3) return;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                onPushChildTask();
                if (checkBox.isChecked()) {
                    scheduleNotification();
                } else {
                    cancelAlarm();
                }
                try {
                    if (forwardTo.equals("TaskMasterChild")) {
                        Intent intent = new Intent(Detail_Child_Task.this, TaskMasterChild.class);
                        intent.putExtra("HeaderMaster", headerTitle);
                        intent.putExtra("HeaderName", NameOfTask);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Detail_Child_Task.this, TaskMaster.class);
                        intent.putExtra("HeaderTitle", headerTitle);
                        startActivity(intent);
                    }
                } catch (Exception exception) {
                    Intent intent = new Intent(Detail_Child_Task.this, TaskMaster.class);
                    intent.putExtra("HeaderTitle", headerTitle);
                    startActivity(intent);
                }
                finish();
            }
        });
        btn_timestart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar currentDate = Calendar.getInstance();
                new DatePickerDialog(Detail_Child_Task.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        btn_timestart.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });
        btn_timend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar currentDate = Calendar.getInstance();
                new DatePickerDialog(Detail_Child_Task.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        btn_timend.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_nhacnho.setEnabled(true);
                    tv_nhacnho.performClick();
                } else tv_nhacnho.setEnabled(false);
            }
        });
        tv_nhacnho.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });
        spinnerRemind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Imple
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Detail_Child_Task.this);
                builder.setMessage("Bạn có chắc chắn muốn xóa không?").setPositiveButton("Có", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
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
                date_string = dayOfMonth + "/" + monthOfYear + "/" + year;
                new TimePickerDialog(Detail_Child_Task.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        date_string += "  |  " + hourOfDay + ":" + minute;
                        tv_nhacnho.setText(date_string);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void onPushChildTask() {
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (userid.equals(snapshot.getKey())) {
                    String repeat = "";
                    if (checkBox.isChecked()) {
                        repeat = "Có";
                    } else {
                        repeat = "Không";
                    }
                    DatabaseReference dr = reference.child(snapshot.getKey()).child("Tasks").child("Tất cả công việc").child(NameOfTask).child("TasksChild");
                    if (!et_descript.getText().toString().equals("")) {
                        dr.child(et_title.getText().toString()).child("Detail").child("Mô tả").setValue(et_descript.getText().toString());
                        dr.child(et_title.getText().toString()).child("Detail").child("Nhắc nhở").setValue(repeat);
                        dr.child(et_title.getText().toString()).child("Detail").child("Ngày bắt đầu").setValue(btn_timestart.getText().toString());
                        dr.child(et_title.getText().toString()).child("Detail").child("Ngày kết thúc").setValue(btn_timend.getText().toString());
                        dr.child(et_title.getText().toString()).child("Detail").child("Trạng thái").setValue("Chưa xong");
                        dr.child(et_title.getText().toString()).child("Detail").child("ID notification").setValue(IDNoti);
                        dr.child(et_title.getText().toString()).child("Detail").child("Lặp lại").setValue(spinnerRemind.getSelectedItem().toString());
                        dr.child(et_title.getText().toString()).child("Detail").child("Thời gian nhắc nhở").setValue(tv_nhacnho.getText().toString());
                        Toast.makeText(Detail_Child_Task.this, "Change Task Success", Toast.LENGTH_SHORT).show();
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

    private void getDatePattern() {
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        rf = reference.child(userid).child("Tasks").child("Tất cả công việc").child(NameOfTask).child("Detail").child("Ngày kết thúc");
        rf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dt = snapshot.getValue().toString();
                try {
                    dateParent = sdf.parse(dt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    mData.child("Users").child(userid).child("Tasks").child("Tất cả công việc").child(NameOfTask).child("TasksChild").child(Name).removeValue();
                    Toast.makeText(Detail_Child_Task.this, "Delete task Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Detail_Child_Task.this,TaskManagement.class));
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
}