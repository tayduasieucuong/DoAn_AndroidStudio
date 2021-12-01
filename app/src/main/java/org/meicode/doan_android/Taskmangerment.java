package org.meicode.doan_android;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class Taskmangerment extends AppCompatActivity {
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;

    ExpandableListView expandableListView;
    //List
    ArrayList<String> listGroup = new ArrayList<>();
    HashMap<String,ArrayList<String>> listChild = new HashMap<>();
    //Adapter for job
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

        MainAdapter adapter;
        //listviewexpan
        expandableListView = findViewById(R.id.exp_list_view);
        for(int i = 0 ;i<=10;i++)
        {
            listGroup.add("Group"+i);
            ArrayList<String> arrayList = new ArrayList<>();

            for (int j = 0;j<=5;j++)
            {
                arrayList.add("Item"+j);
            }
            listChild.put(listGroup.get(i),arrayList);
        }
        adapter = new MainAdapter(listGroup,listChild);
        expandableListView.setAdapter(adapter);
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