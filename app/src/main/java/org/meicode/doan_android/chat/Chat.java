package org.meicode.doan_android.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.meicode.doan_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chat extends AppCompatActivity {
    ImageView backProject, sendMessage;
    RecyclerView listMessage;
    TextView nameProject;
    EditText chatMessage;
    FirebaseDatabase database;
    DatabaseReference mData;
    FirebaseAuth mAuth;
    String groupID,myID;
    SimpleDateFormat sdf;
    ChatAdapter chatAdapter;
    String userID,content,nickname,date;
    Date messageDate;
    private final List<ChatList> chatLists=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        backProject=findViewById(R.id.backProject);
        sendMessage=findViewById(R.id.sendMessage);
        listMessage=findViewById(R.id.listMessage);
        nameProject=findViewById(R.id.nameProject);
        chatMessage=findViewById(R.id.chatMessage);
        mAuth = FirebaseAuth.getInstance();
        myID=getIntent().getStringExtra("UID");
        database = FirebaseDatabase.getInstance("https://doan-3672e-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mData = database.getReference("Groups");
        final SharedPreferences sharedPreferences = getSharedPreferences("GROUPID", MODE_PRIVATE);
        groupID = sharedPreferences.getString("GID", null);
        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        showMessage();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void showMessage() {
        ValueEventListener valueEventListener = new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.child(groupID).child("Thành viên").getChildren()){
                    userID=dataSnapshot.getValue().toString();
                    nickname=dataSnapshot.child("Info").child("Biệt danh").getValue().toString();
                    content=dataSnapshot.child("Chat").child("Nội dung").getValue().toString();
                    date=dataSnapshot.child("Chat").child("Ngày tạo").getValue().toString();
                    try {
                        messageDate=sdf.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    chatLists.add(new ChatList(messageDate,content,nickname,userID));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mData.addValueEventListener(valueEventListener);
    }


}