package org.meicode.doan_android;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btn_regis;
    EditText email;
    EditText password;
    FirebaseDatabase database;
    DatabaseReference reference;
    TextView tv_signin;
    boolean showpassword;
    ImageView btn_showpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        tv_signin = (TextView)findViewById(R.id.textView3);
        btn_regis = (Button) findViewById(R.id.button2);
        email = (EditText)findViewById(R.id.edtEmail);
        password = (EditText)findViewById(R.id.edtPass);
        btn_showpass = (ImageView)findViewById(R.id.view_eye);
        showpassword = false;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");


        onClick();
    }
    private void onClick(){
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });
        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
        btn_showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showpassword) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showpassword=false;
                }
                else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showpassword = true;
                }
            }
        });
    }
    private void createUser(){
        String emaill = email.getText().toString();
        String passwordd = password.getText().toString();
        if (TextUtils.isEmpty(emaill)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                        reference = database.getReference("Users");
                        String uid = mAuth.getUid();
                        reference.child(uid);
                        reference.child(uid).child("UserInfo");
                        reference.child(uid).child("UserInfo").child("Email").setValue(email.getText().toString());
                        startActivity(new Intent(SignUp.this,SignIn.class));
                    }
                }
            });
        }
    }
}