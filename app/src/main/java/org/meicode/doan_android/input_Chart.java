package org.meicode.doan_android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class input_Chart extends AppCompatActivity {
    EditText edt1,edt2,edt3;
    Button btn;
    Intent intent;
    String t1,t2;
    Calendar cal1,cal2;
    Date date1,date2;
    SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_chart);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        edt2=(EditText) findViewById(R.id.edt2);
        edt3=(EditText) findViewById(R.id.edt3);
        btn=(Button) findViewById(R.id.btn);
        intent = new Intent(input_Chart.this, charts_task.class);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        edt2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar currentDate = android.icu.util.Calendar.getInstance();
                new DatePickerDialog(input_Chart.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        edt2.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                        try {
                            date1 = sdf.parse(edt2.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, currentDate.get(android.icu.util.Calendar.YEAR), currentDate.get(android.icu.util.Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });

        edt3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar currentDate = android.icu.util.Calendar.getInstance();
                new DatePickerDialog(input_Chart.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        edt3.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                        try {
                            date2 = sdf.parse(edt3.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, currentDate.get(android.icu.util.Calendar.YEAR), currentDate.get(android.icu.util.Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(date1.compareTo(date2) > 0) {
                    Toast.makeText(input_Chart.this, "Chọn ngày sai !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    intent.putExtra("date1",edt2.getText().toString());
                    intent.putExtra("date2",edt3.getText().toString());
                    startActivity(intent);
                }
            }
        });

    }
    private int compareDate() throws ParseException {
        int result = date1.compareTo(date2);
        return result;
    };
}