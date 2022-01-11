package org.meicode.doan_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ApdapterChat extends ArrayAdapter {
    Context context;
    int layout;
    List<String> list;

    public ApdapterChat(Context context, int layout, List<String> list)
    {
        super(context,layout,list);
        this.context = context;
        this.layout = layout;
        this.list = list;
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
            holder.tv_content = (TextView) row.findViewById(R.id.name_group);
            holder.tv_date = (TextView) row.findViewById(R.id.event_message);
            holder.tv_name = (TextView) row.findViewById(R.id.name);
            row.setTag(holder);
        }else
        {
            holder = (AdapterHolder) row.getTag();
        }
        String[] data = list.get(position).toString().split("~~",3);
        holder.tv_content.setText(data[0]);
        holder.tv_date.setText(data[1]);
        holder.tv_name.setText(data[2]);
        return row;
    }
    class AdapterHolder
    {
        TextView tv_content;
        TextView tv_date;
        TextView tv_name;
    }
}
