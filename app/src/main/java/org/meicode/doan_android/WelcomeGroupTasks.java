package org.meicode.doan_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeGroupTasks extends AppCompatActivity {
    Button btn_go;
    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_group_tasks);
        btn_go = (Button)findViewById(R.id.btn_go);
        getSupportActionBar().hide();
        changeStatusBarColor("#6D85F6");
        onClick();
    }
    private void onClick(){
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeGroupTasks.this,ListGroup.class));
                finish();
            }
        });
    }
}