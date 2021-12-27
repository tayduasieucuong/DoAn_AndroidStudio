package org.meicode.doan_android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListCreateGrAdapter extends ArrayAdapter {
    Context context;
    int layout;
    List<String> list;
    private Create_Group.onDeleteListener onDeleteListener;
    public ListCreateGrAdapter(Context context, int layout, List<String> list, Create_Group.onDeleteListener onDeleteListener)
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
            holder.btn_outGr = (ImageView) row.findViewById(R.id.btn_outgr);
            holder.rdg = (RadioGroup) row.findViewById(R.id.rdg);
            row.setTag(holder);
        }else
        {
            holder = (AdapterHolder) row.getTag();
        }
        String[] data = list.get(position).toString().split("/",2);
        holder.tv_name.setText(data[0]);
        holder.btn_outGr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteListener.onDelete(position);
            }
        });
        holder.rdg.check(R.id.radioButton2);
        return row;
    }
    class AdapterHolder
    {
        TextView tv_name;
        TextView tv_message;
        ImageView btn_outGr;
        RadioGroup rdg;
    }
}
