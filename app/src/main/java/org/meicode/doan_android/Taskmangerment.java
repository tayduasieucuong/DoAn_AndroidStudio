package org.meicode.doan_android;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Taskmangerment extends AppCompatActivity {
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmangerment);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_menu);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        //bottomNavigationView.setBackground(new ColorDrawable(Color.parseColor("#ECB7F0")));
        FloatingActionButton add_btn = (FloatingActionButton) findViewById(R.id.addbottom);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.search)
        {
            Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
        }else if( id == R.id.notify)
        {
            Toast.makeText(getApplicationContext(), "Notify", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}