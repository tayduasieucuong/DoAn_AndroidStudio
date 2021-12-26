package org.meicode.doan_android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeCoutDown extends AppCompatActivity {
    private static  long START_TIME_IN_MILLIS = 60000;
    private boolean mIsBound;
    private TextView mTextViewCountDown, tv_name;
    private Button mButtonStartPause;
    private Button  mButtonFinish;
    private CountDownTimer mCountDownTimer;
    private int count=0, countSum=0;
    private boolean mTimerRunning,kt,ktt;
    private ProgressBar pb;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mTimeRelax;
    private NotificationManagerCompat managerCompat;
    private NotificationCompat.Builder builder, builder1,builder2;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    String uid,Name;
    ActionBar actionBar;
    private void setActionBar()
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("Thời gian tập trung");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(3);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_cout_down);
        setActionBar();
        Intent intent = getIntent();
        int time = intent.getIntExtra("time", 0);
        int time1 = intent.getIntExtra("time1", 0);
        Name = intent.getStringExtra("name");
        mTimeLeftInMillis = time*60000;
        mTimeRelax = time1*60;
        kt=false;
        START_TIME_IN_MILLIS=mTimeLeftInMillis;
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        tv_name=findViewById(R.id.textView2);
        tv_name.setText(Name);
        pb = findViewById(R.id.progressBar);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonFinish = findViewById(R.id.button_finish);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reference = database.getReference("Users");
        uid = mAuth.getUid();
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });



        updateCountDownText();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        kt=false;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel("My notification","My notification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager =  getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        String message = "Bạn có 5 phút để giải lao";
        builder = new NotificationCompat.Builder(TimeCoutDown.this,"My notification")
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Đã đến thời gian nghỉ")
                .setContentText(message)
                .setSound(alarmSound)
                .setAutoCancel(true);

        String message1 = "Chúc mừng bạn đã hoàn thành";
        builder1 = new NotificationCompat.Builder(TimeCoutDown.this,"My notification")
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Hết thời gian")
                .setContentText(message1)
                .setSound(alarmSound)
                .setAutoCancel(true);

        String message2 = "Quay lại làm việc thôi";
        builder2 = new NotificationCompat.Builder(TimeCoutDown.this,"My notification")
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Hết thời gian nghỉ")
                .setContentText(message2)
                .setSound(alarmSound)
                .setAutoCancel(true);
        managerCompat = NotificationManagerCompat.from(TimeCoutDown.this);
        mButtonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    pauseTimer();
                    mCountDownTimer.onFinish();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Không thể kết thúc khi chưa bắt đầu!",Toast.LENGTH_LONG).show();
                };
            }
        });
    }
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
    private void startTimer() {

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                count++;
                if( count%mTimeRelax==0 && kt==false){
                    managerCompat.notify(count,builder.build());
                    countSum+=count;
                    count=0;
                    kt=true;
                }else
                if(kt==true && count%(10)==0){
                    managerCompat.notify(count,builder2.build());
                    countSum+=count;
                    count=0;
                    kt=false;
                }
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFinish() {
                managerCompat.notify(count,builder1.build());
                mTimerRunning = false;
                count+=countSum;
                int c= (int) (count*100000 / START_TIME_IN_MILLIS);
                reference.child(uid).child("FocusTask").child(Name).child("Hoàn thành được").setValue(c+"%");
                getTimeToNotify();
                String randomKey = randomString(15);
                reference.child(uid).child("Notification").child(randomKey).child("Content").setValue("Hoàn thành thời gian tập trung \""+ Name+"\"");
                reference.child(uid).child("Notification").child(randomKey).child("Time").setValue(DateNotify);
                Intent intent = new Intent(TimeCoutDown.this,input_time.class);
                mButtonStartPause.setText("Start");
                mButtonStartPause.setVisibility(View.INVISIBLE);

                startActivity(intent);
                finish();
            }
        }.start();



        mTimerRunning = true;
        mButtonStartPause.setText("Tạm dừng");

    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Bắt đầu");
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();

        mButtonStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int hours = (int) ((mTimeLeftInMillis / 1000) / 60 )/60;
        int minutes = (int) ((mTimeLeftInMillis / 1000) / 60) %60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            mButtonFinish.performClick();
            startActivity(new Intent(TimeCoutDown.this, ListFocusTime.class));
            finish();
        }
        return true;
    }

}