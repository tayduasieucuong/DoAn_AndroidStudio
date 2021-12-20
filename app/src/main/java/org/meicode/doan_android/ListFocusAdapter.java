package org.meicode.doan_android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListFocusAdapter extends ArrayAdapter {
    Context context;
    int layout;
    List<String> list;
    private ListFocusTime.onDeleteListener onDeleteListener;
    public ListFocusAdapter(Context context, int layout, List<String> list, ListFocusTime.onDeleteListener onDeleteListener)
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
            holder.tv_content = (TextView) row.findViewById(R.id.tv_content);
            holder.tv_time = (TextView) row.findViewById(R.id.tv_time);
            holder.tv_percent = (TextView) row.findViewById(R.id.tv_percent);
            holder.btn_delete = (ImageView) row.findViewById(R.id.btn_delete);
            row.setTag(holder);
        }else
        {
            holder = (AdapterHolder) row.getTag();
        }
        String[] data = list.get(position).toString().split("/",3);
        holder.tv_content.setText(data[0]);
        holder.tv_time.setText(data[1]);
        holder.tv_percent.setText(data[2]);
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteListener.onDelete(data[0]);
            }
        });
        return row;
    }
    class AdapterHolder
    {
        TextView tv_content;
        TextView tv_time;
        TextView tv_percent;
        ImageView btn_delete;
    }
}
