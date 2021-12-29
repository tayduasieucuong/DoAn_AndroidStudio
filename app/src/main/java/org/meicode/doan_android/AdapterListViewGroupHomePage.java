package org.meicode.doan_android;

import android.app.Activity;
import android.content.Context;
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

public class AdapterListViewGroupHomePage extends ArrayAdapter {
    Context context;
    int layout;
    List<ListChildHome> list;
    AdapterHolder holder;
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
        holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
            holder = new AdapterHolder();
            holder.tv_name = row.findViewById(R.id.tv_name);
            holder.tv_admin = row.findViewById(R.id.tv_admin);
            holder.tv_time = row.findViewById(R.id.tv_time);
            //Drop down
            holder.btn_dropdown = row.findViewById(R.id.btn_down);
            holder.btn_info = row.findViewById(R.id.btn_info);
            holder.btn_delete = row.findViewById(R.id.btn_delete);
            row.setTag(holder);
        } else {
            holder = (AdapterHolder) row.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_admin.setText(list.get(position).getAdmin());
        holder.tv_time.setText(list.get(position).getCreateTime());
        AdapterHolder finalHolder = holder;
        holder.btn_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(list.get(position).getDropDown() == false)
                    {
                        holder.btn_delete.setVisibility(View.VISIBLE);
                        holder.btn_info.setVisibility(View.VISIBLE);
                        list.get(position).setDropDown(true);
                    }else{
                        holder.btn_delete.setVisibility(View.GONE);
                        holder.btn_info.setVisibility(View.GONE);
                        list.get(position).setDropDown(false);
                    }
            }
        });
        return row;
    }

    class AdapterHolder {
        TextView tv_name;
        TextView tv_admin;
        TextView tv_time;
        ImageView btn_dropdown;
        ImageView btn_info;
        ImageView btn_delete;
    }
}
