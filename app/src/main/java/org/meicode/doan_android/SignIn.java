package org.meicode.doan_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btn_login;
    TextView btn_regis;
    EditText email;
    EditText password;
    TextView tv_forgotpass;
    Context context;
    boolean showpassword;
    ImageView btn_showpass;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        btn_login = (Button) findViewById(R.id.button2);
        email = (EditText) findViewById(R.id.edtEmail);
        password = (EditText) findViewById(R.id.edtPass);
        tv_forgotpass = (TextView)findViewById(R.id.textView4);
        showpassword = false;
        btn_showpass = (ImageView)findViewById(R.id.view_eye);
        mAuth = FirebaseAuth.getInstance();
        btn_regis = (TextView) findViewById(R.id.textView3);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        onClick();

    }
    private void onClick(){
        tv_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,ForgetPass.class));
            }
        });
        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,SignUp.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
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
    private void loginUser(){
        String emaill = email.getText().toString();
        String passwordd = password.getText().toString();
        if(!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        }

        if(passwordd.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(emaill)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(emaill,passwordd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                final SharedPreferences sharedPreferences = getSharedPreferences("USERID", MODE_PRIVATE);
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignIn.this,TaskManagement.class);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UID",mAuth.getUid());
                        editor.commit();
                        progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignIn.this, "Log in Error", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

}