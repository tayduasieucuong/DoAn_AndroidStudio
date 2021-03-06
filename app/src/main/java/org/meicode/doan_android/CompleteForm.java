package org.meicode.doan_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CompleteForm extends AppCompatActivity {
    String taskChild;
    String taskMaster;
    String userid;
    SeekBar seekBar;
    TextView tv_percent;
    ImageView icon;
    TextView title;
    FirebaseDatabase database;
    DatabaseReference reference;
    String descript;
    TextView tv_desc;
    ImageView btn_complete;
    ImageView btn_delete;
    String headerTitle;
    String tasktemp;
    int indexGroup;
    int indexItem;
    ActionBar actionBar;

    private void getDatafromAnotherActivity(){
        Intent intent = getIntent();
        taskMaster = intent.getStringExtra("NameOfTask");
        headerTitle = intent.getStringExtra("HeaderTitle");
        taskChild = intent.getStringExtra("NameOfChildTask");
        indexGroup = intent.getIntExtra("Index Group",0);
        indexItem = intent.getIntExtra("Index Item",0);
        final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
        userid = sharedPreferences.getString("UID",null);
        String[] task = taskChild.split("/");
        tasktemp = task[0];
        title.setText(tasktemp);
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
    }
    private void initView(){
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tv_percent = (TextView) findViewById(R.id.tv_percent);
        icon = (ImageView) findViewById(R.id.icon);
        seekBar.setProgress(50);
        tv_percent.setTextColor(Color.parseColor("#ff6d00"));
        tv_percent.setText("50%");
        icon.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_24);
        title = (TextView)findViewById(R.id.tv_title);
        tv_desc = (TextView) findViewById(R.id.tv_des);
        btn_complete = (ImageView) findViewById(R.id.btn_complete);
        btn_delete = (ImageView) findViewById(R.id.btn_delete);
        actionBar = getSupportActionBar();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_form);
        initView();
        setActionBar();
        getDatafromAnotherActivity();
        onClick();
        onReadTask();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            onBackPressed();
        }
        return true;
    }
    String DateNotify;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getTimeToNotify(){
        Date date = android.icu.util.Calendar.getInstance(TimeZone.getTimeZone("UTC+7")).getTime();
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
    private void setActionBar()
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("Ho??n th??nh c??ng vi???c");

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(3);
    }
    private void onReadTask(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(userid.equals(snapshot.getKey()))
                {
                    try {
                        descript = snapshot.child("Tasks").child("T???t c??? c??ng vi???c").child(taskMaster).child("TasksChild").child(taskChild).child("Detail").child("M?? t???").getValue().toString();
                    }catch (Exception ex) {}
                    tv_desc.setText(descript);
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
    private void onDeleteTask(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (userid.equals(snapshot.getKey()))
                {

                    DatabaseReference dr = reference.child(snapshot.getKey()).child("Tasks").child("T???t c??? c??ng vi???c").child(taskMaster).child("TasksChild").child(tasktemp);
                    getTimeToNotify();
                    String randomKey = randomString(15);
                    reference.child(userid).child("Notification").child(randomKey).child("Content").setValue("???? x??a "+ title.getText().toString());
                    reference.child(userid).child("Notification").child(randomKey).child("Time").setValue(DateNotify);
                    dr.removeValue();
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
    private void onCompleteTask(){
        reference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (userid.equals(snapshot.getKey()))
                {

                    Calendar calendar = Calendar.getInstance();
                    String[] temp = taskChild.split("/");
                    taskChild = temp[0];
                    DatabaseReference drMasterDetail = reference.child(snapshot.getKey()).child("Tasks").child("L???ch s??? c??ng vi???c").child(taskMaster);
                    drMasterDetail.child("Detail").child("Ng??y ho??n th??nh").setValue("Kh??ng x??c ?????nh");
                    drMasterDetail.child("Detail").child("Tr???ng th??i").setValue("Ch??a ho??n th??nh");
                    DatabaseReference dr2 = reference.child(snapshot.getKey()).child("Tasks").child("L???ch s??? c??ng vi???c").child(taskMaster).child("TasksChild").child(taskChild).child("Ph???n tr??m ho??n th??nh");
                    dr2.setValue(tv_percent.getText().toString());
                    DatabaseReference dr = reference.child(snapshot.getKey()).child("Tasks").child("T???t c??? c??ng vi???c").child(taskMaster).child("TasksChild").child(taskChild).child("Detail").child("Tr???ng th??i");
                    dr.setValue("Xong");
                    DatabaseReference dr3 = reference.child(snapshot.getKey()).child("Tasks").child("L???ch s??? c??ng vi???c").child(taskMaster).child("TasksChild").child(taskChild).child("Ng??y ho??n th??nh");

                    final android.icu.util.Calendar currentDate = android.icu.util.Calendar.getInstance();
                    dr3.setValue(currentDate.get(Calendar.DATE)+"/"+(currentDate.get(Calendar.MONTH)+1)+"/"+currentDate.get(Calendar.YEAR));

                    getTimeToNotify();
                    String randomKey = randomString(15);
                    reference.child(userid).child("Notification").child(randomKey).child("Content").setValue("Ho??n th??nh "+ title.getText().toString());
                    reference.child(userid).child("Notification").child(randomKey).child("Time").setValue(DateNotify);
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
    private void onClick(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<=30) {
                    tv_percent.setTextColor(Color.parseColor("#d50000"));
                    icon.setImageResource(R.drawable.ic_sad);
                }
                else if(i<=50) {
                    tv_percent.setTextColor(Color.parseColor("#ff6d00"));
                    icon.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_24);
                }
                else if(i<=80) {
                    tv_percent.setTextColor(Color.parseColor("#76ff03"));
                    icon.setImageResource(R.drawable.ic_smile);
                }
                else {
                    tv_percent.setTextColor(Color.parseColor("#2962ff"));
                    icon.setImageResource(R.drawable.ic_baseline_sentiment_very_satisfied_24);
                }
                tv_percent.setText(i+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteTask();
                Toast.makeText(CompleteForm.this, "Task is deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CompleteForm.this,TaskMaster.class);
                intent.putExtra("HeaderTitle",headerTitle);
                startActivity(intent);
                finish();
            }
        });
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCompleteTask();
                Toast.makeText(CompleteForm.this, "Task is completed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CompleteForm.this,TaskMaster.class);
                intent.putExtra("HeaderTitle",headerTitle);
                startActivity(intent);
                finish();
            }
        });
    }
}