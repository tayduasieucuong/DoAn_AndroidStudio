package org.meicode.doan_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ForgetPass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);
        TextView tvSinIn = (TextView) findViewById(R.id.textView3);
        tvSinIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPass.this, SignIn.class);
                startActivity(intent);
            }
        });
    }
}