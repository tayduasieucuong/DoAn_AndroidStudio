package org.meicode.doan_android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdapterListViewGroupHomePage extends ArrayAdapter {
    Context context;
    int layout;
    List<ListChildHome> list;

    public AdapterListViewGroupHomePage(Context context, int layout, List<ListChildHome> list) {
        super(context, layout, list);
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        AdapterHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
            holder = new AdapterHolder();
            holder.tv_name = row.findViewById(R.id.tv_name);
            holder.tv_admin = row.findViewById(R.id.tv_admin);
            holder.tv_time = row.findViewById(R.id.tv_time);
            row.setTag(holder);
        } else {
            holder = (AdapterHolder) row.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_admin.setText(list.get(position).getAdmin());
        holder.tv_time.setText(list.get(position).getCreateTime());

        return row;
    }

    class AdapterHolder {
        TextView tv_name;
        TextView tv_admin;
        TextView tv_time;
    }
}
