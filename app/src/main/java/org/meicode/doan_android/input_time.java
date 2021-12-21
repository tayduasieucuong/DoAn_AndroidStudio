package org.meicode.doan_android;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class input_time extends AppCompatActivity {
    EditText edt1,edt2,edt3;
    Button btn;
    Intent intent;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    String t1,t2;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_time);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Thời gian tập trung");
        edt1=(EditText) findViewById(R.id.edt1);
        edt2=(EditText) findViewById(R.id.edt2);
        edt3=(EditText) findViewById(R.id.edt3);
        btn=(Button) findViewById(R.id.btn);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        intent = new Intent(input_time.this, TimeCoutDown.class);

        edt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(input_time.this,2 ,new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        edt2.setText(i + ":" + i1);
                        intent.putExtra("time",i*60+i1);
                        t1=i+" giờ "+i1+" phút";
                    }
                }, 0, 0, true);
                timePickerDialog.show();
            }
        });

        edt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(input_time.this,2, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        edt3.setText(i + ":" + i1);
                        intent.putExtra("time1",i*60+i1);
                        t2=i+" giờ "+i1+" phút";
                    }
                }, 0, 0, true);
                timePickerDialog.show();

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String nametask =edt1.getText().toString();
                String time1task =edt2.getText().toString();
                String time2task =edt3.getText().toString();
                if(nametask.isEmpty())
                {
                    edt1.setError("Name is required!");
                    edt1.requestFocus();
                    return;
                }
                if(time1task.isEmpty())
                {
                    edt2.setError("Time is required!");
                    edt2.requestFocus();
                    return;
                }
                if(time2task.isEmpty())
                {
                    edt3.setError("Time is required!");
                    edt3.requestFocus();
                    return;
                }
                reference = database.getReference("Users");
                String uid = mAuth.getUid();
                reference.child(uid).child("FocusTask").child(nametask).child("Tổng thời gian").setValue(t1);
                reference.child(uid).child("FocusTask").child(nametask).child("Nghỉ sau").setValue(t2);
                getTimeToNotify();
                String randomKey = randomString(15);
                reference.child(uid).child("Notification").child(randomKey).child("Content").setValue("Đã thêm "+ nametask + " vào quản lý công việc");
                reference.child(uid).child("Notification").child(randomKey).child("Time").setValue(DateNotify);
                intent.putExtra("name",edt1.getText().toString());
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            startActivity(new Intent(this,ListFocusTime.class));
            finish();
        }
        return true;
    }
}
