package org.meicode.doan_android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
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
    private Create_Group.onReadDutyListener onReadDutyListener;
    public ListCreateGrAdapter(Context context, int layout, List<String> list, Create_Group.onDeleteListener onDeleteListener, Create_Group.onReadDutyListener onReadDutyListener)
    {
        super(context,layout,list);
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.onDeleteListener = onDeleteListener;
        this.onReadDutyListener = onReadDutyListener;
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
        String[] data = list.get(position).toString().split("/",3);
        holder.tv_name.setText(data[0]);
        holder.btn_outGr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteListener.onDelete(position);
            }
        });
        holder.rdg.check(R.id.radioButton2);
        holder.rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton) {
                    onReadDutyListener.onRead(position,true);
                } else {
                    onReadDutyListener.onRead(position,false);
                }
            }
        });
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
