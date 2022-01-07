package org.meicode.doan_android.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.meicode.doan_android.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatList> chatLists;
    private final Context context;
    private final String userID;
    public ChatAdapter(List<ChatList> chatLists, Context context,String userID) {
        this.chatLists = chatLists;
        this.context = context;
        this.userID=userID;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        ChatList list2=chatLists.get(position);
        if(list2.getIdUser().equals(userID)){
            holder.my_layout.setVisibility(View.VISIBLE);
            holder.opp_layout.setVisibility(View.GONE);
        }else {
            holder.my_layout.setVisibility(View.GONE);
            holder.opp_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout opp_layout,my_layout;
        private ImageView user_message;
        private TextView opp_nickname,opp_message,opp_time,my_message,my_time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            opp_layout=itemView.findViewById(R.id.opp_layout);
            my_layout=itemView.findViewById(R.id.my_layout);
            user_message=itemView.findViewById(R.id.user_message);
            opp_nickname=itemView.findViewById(R.id.opp_nickname);
            opp_message=itemView.findViewById(R.id.opp_message);
            opp_time=itemView.findViewById(R.id.opp_time);
            my_message=itemView.findViewById(R.id.my_message);
            my_time=itemView.findViewById(R.id.my_time);
        }
    }
}
