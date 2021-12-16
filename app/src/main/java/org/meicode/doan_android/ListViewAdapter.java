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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter {
    Context context;
    int layout;
    List<String> list;
    String nameofTask;
    String headerTitle;
    List<String> listTime;
    public ListViewAdapter(Context context, int layout, List<String> list, String nameofTask, String headerTitle, List<String> listTime)
    {
        super(context,layout,list);
        this.layout = layout;
        this.list = list;
        this.context = context;
        this.nameofTask = nameofTask;
        this.headerTitle = headerTitle;
        this.listTime = listTime;
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
            holder.btn_complete = (ImageView) row.findViewById(R.id.btn_checkbox);
            holder.imgright = (ImageView) row.findViewById(R.id.btn_star);
            holder.tv= (TextView) row.findViewById(R.id.tv_content);
            holder.btn_add = (ImageView) row.findViewById(R.id.btn_add_child_task);
            holder.tv_time = (TextView) row.findViewById(R.id.tv_time);
            row.setTag(holder);
        }else
        {
            holder = (AdapterHolder) row.getTag();
        }
        holder.btn_add.setVisibility(View.GONE);
        String data  = list.get(position).toString();
        String[] dataSplited = data.split("/");
        String content = dataSplited[0];
        String status = dataSplited[1];
        holder.tv.setText(content);
        String[] dateData = listTime.get(position).toString().split("-");
        String timeStart = dateData[0];
        String timeEnd = dateData[1];
        holder.tv_time.setText(timeStart + " -> " + timeEnd);
        if (status.equals("Xong")){
            holder.btn_complete.setImageResource(R.drawable.ic_baseline_radio_button_checked_24);
            holder.btn_complete.setEnabled(false);
        }else{
            holder.btn_complete.setEnabled(true);
            holder.btn_complete.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
        }
        holder.btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CompleteForm.class);
                intent.putExtra("NameOfChildTask",data);
                intent.putExtra("NameOfTask",headerTitle);
                intent.putExtra("HeaderTitle",nameofTask);
                intent.putExtra("forwardTo","TaskMasterChild");
                view.getContext().startActivity(intent);
            }
        });
        return row;
    }
    class AdapterHolder
    {
        ImageView btn_complete;
        TextView tv;
        ImageView imgright;
        ImageView btn_add;
        TextView tv_time;
    }
}
