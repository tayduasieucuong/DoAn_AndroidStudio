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

public class ListViewAdapter extends ArrayAdapter {
    Context context;
    int layout;
    List<String> list;
    public ListViewAdapter(Context context, int layout, List<String> list)
    {
        super(context,layout,list);
        this.layout = layout;
        this.list = list;
        this.context = context;
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
            holder.img = (ImageView) row.findViewById(R.id.btn_checkbox);
            holder.imgright = (ImageView) row.findViewById(R.id.btn_star);
            holder.tv= (TextView) row.findViewById(R.id.tv_content);
            holder.btn_add = (ImageView) row.findViewById(R.id.btn_add_child_task);
            row.setTag(holder);
        }else
        {
            holder = (AdapterHolder) row.getTag();
        }
        if(position!=list.size()-1)
            holder.btn_add.setVisibility(View.GONE);
        holder.tv.setText(list.get(position).toString());
        return row;
    }
    class AdapterHolder
    {
        ImageView img;
        TextView tv;
        ImageView imgright;
        ImageView btn_add;
    }
}
