package org.meicode.doan_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SignIn extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btn_login;
    TextView btn_regis;
    EditText email;
    EditText password;
    Context context;
    boolean showpassword;
    ImageView btn_showpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        btn_login = (Button) findViewById(R.id.button2);
        email = (EditText) findViewById(R.id.edtEmail);
        password = (EditText) findViewById(R.id.edtPass);
        showpassword = false;
        btn_showpass = (ImageView)findViewById(R.id.view_eye);
        mAuth = FirebaseAuth.getInstance();
        btn_regis = (TextView) findViewById(R.id.textView3);
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
        if (TextUtils.isEmpty(emaill)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }else{
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
                        startActivity(intent);
                    }
                }
            });
        }
    }

}