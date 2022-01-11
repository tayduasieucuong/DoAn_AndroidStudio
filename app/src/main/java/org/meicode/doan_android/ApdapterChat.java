package org.meicode.doan_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ApdapterChat extends ArrayAdapter {
    Context context;
    int layout;
    List<String> list;
    String UID;
    public ApdapterChat(Context context, int layout, List<String> list, String UID)
    {
        super(context,layout,list);
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.UID=UID;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        AdapterHolder holder = null;


        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layout,parent,false);
            holder = new AdapterHolder();
            holder.my_layout= row.findViewById(R.id.my_layout);
            holder.opp_layout= row.findViewById(R.id.opp_layout);
            holder.opp_nickname = (TextView) row.findViewById(R.id.opp_nickname);
            holder.opp_message = (TextView) row.findViewById(R.id.opp_message);
            holder.opp_time = (TextView) row.findViewById(R.id.opp_time);
            holder.my_message = (TextView) row.findViewById(R.id.my_message);
            holder.my_time = (TextView) row.findViewById(R.id.my_time);
            row.setTag(holder);
        }else
        {
            holder = (AdapterHolder) row.getTag();
        }
        String[] data = list.get(position).toString().split("~~",4);
        holder.opp_message.setText(data[0]);
        holder.my_message.setText(data[0]);
        holder.opp_time.setText(data[1]);
        holder.my_time.setText(data[1]);
        holder.opp_nickname.setText(data[2]);
        if(data[3].equals(UID)){
            holder.my_layout.setVisibility(View.VISIBLE);
            holder.opp_layout.setVisibility(View.GONE);
        }else {
            holder.my_layout.setVisibility(View.GONE);
            holder.opp_layout.setVisibility(View.VISIBLE);
        }
        return row;
    }
    class AdapterHolder
    {
        RelativeLayout my_layout,opp_layout;
        TextView opp_nickname;
        TextView opp_message;
        TextView opp_time;
        TextView my_message;
        TextView my_time;
    }
}
