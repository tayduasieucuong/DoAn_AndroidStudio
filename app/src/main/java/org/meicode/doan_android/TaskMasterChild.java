package org.meicode.doan_android;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TaskMasterChild extends AppCompatActivity {
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    ArrayList<String> list;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_master_child);
        setActionBar();
        setBottomNavigation();
        lv = (ListView) findViewById(R.id.lv);
        list = new ArrayList<String>();
        list.add("fasfasdf");
        list.add("fasfdasfasdfas");
        list.add("fasfasdf");
        list.add("fasfdasfasdfas");
        list.add("fasfasdf");
        list.add("fasfdasfasdfas");
        list.add("fasfasdf");
        list.add("fasfdasfasdfas");
        list.add("fasfasdf");
        list.add("fasfdasfasdfas");
        list.add("fasfasdf");
        list.add("fasfdasfasdfas");
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.layout_item_child_taskmaster,list);
        lv.setAdapter(adapter);
    }
    private void setActionBar()
    {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //actionBar.setLogo(R.drawable.ic_menu);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setTitle("");
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECB7F0")));
        actionBar.setElevation(1);
    }
    private  void setBottomNavigation()
    {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
        FloatingActionButton add_btn = (FloatingActionButton) findViewById(R.id.addbottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.person:
                        Toast.makeText(TaskMasterChild.this, "Person", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.group:
                        Toast.makeText(TaskMasterChild.this, "Group", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.calendar:
                        Toast.makeText(TaskMasterChild.this, "Calendar", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.alarm:
                        startActivity(new Intent(TaskMasterChild.this,input_time.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}