package org.meicode.doan_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListGroupAdapter extends ArrayAdapter {
    Context context;
    int layout;
    List<String> list;
    String idtask;
    private ListGroup.onDeleteListener onDeleteListener;
    public ListGroupAdapter(Context context, int layout, List<String> list, ListGroup.onDeleteListener onDeleteListener)
    {
        super(context,layout,list);
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.onDeleteListener = onDeleteListener;
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
            holder.tv_name = (TextView) row.findViewById(R.id.name_group);
            holder.tv_message = (TextView) row.findViewById(R.id.event_message);
            holder.btn_outGr = (ImageView) row.findViewById(R.id.btn_outgr);
            holder.btn_info = (ImageView) row.findViewById(R.id.btn_info);
            row.setTag(holder);
        }else
        {
            holder = (AdapterHolder) row.getTag();
        }
        String[] data = list.get(position).toString().split("/",2);
        holder.tv_name.setText(data[0]);
        holder.tv_message.setText("Id: "+data[1]);
        idtask = data[1];
        holder.btn_outGr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteListener.onDelete(data[0]);
            }
        });
        holder.btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),HomePageGroup.class);
                intent.putExtra("IDTASK", idtask);
                view.getContext().startActivity(intent);
            }
        });
        return row;
    }
    class AdapterHolder
    {
        TextView tv_name;
        TextView tv_message;
        ImageView btn_outGr;
        ImageView btn_info;
    }
}
