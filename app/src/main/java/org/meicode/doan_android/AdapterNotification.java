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

public class AdapterNotification extends ArrayAdapter {
    Context context;
    int layout;
    List<String> list;
    private Notification.onDeleteNotification monDeleteNotification;
    public AdapterNotification(Context context, int layout, List<String> list, Notification.onDeleteNotification monDeleteNotification)
    {
        super(context,layout,list);
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.monDeleteNotification = monDeleteNotification;
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
            holder.imgleft = row.findViewById(R.id.img);
            holder.tv_content = row.findViewById(R.id.tv_content);
            holder.tv_time = row.findViewById(R.id.tv_time);
            holder.btn_delete = row.findViewById(R.id.btn_delete);
            row.setTag(holder);
        }else
        {
            holder = (AdapterHolder) row.getTag();
        }
        String[] data = list.get(position).split(",",3);
        holder.tv_content.setText(data[0].toString());
        holder.tv_time.setText(data[1].toString());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monDeleteNotification.onDelete(data[0]+","+data[2]);
            }
        });
        return row;
    }
    public class AdapterHolder{
        ImageView imgleft;
        TextView tv_content;
        TextView tv_time;
        ImageView btn_delete;
    }
}
