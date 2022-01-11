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
    private HomePageGroup.itemBtnCompleteChild itemBtnCompleteChild;
    private HomePageGroup.itemBtnDeleteChild itemBtnDeleteChild;
    public AdapterListViewGroupHomePage(Context context, int layout, List<ListChildHome> list,HomePageGroup.itemBtnCompleteChild itemBtnCompleteChild,HomePageGroup.itemBtnDeleteChild itemBtnDeleteChild) {
        super(context, layout, list);
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.itemBtnCompleteChild = itemBtnCompleteChild;
        this.itemBtnDeleteChild = itemBtnDeleteChild;
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
            holder.btn_complete = row.findViewById(R.id.btn_complete);
            holder.btn_info = row.findViewById(R.id.btn_info);
            holder.btn_delete = row.findViewById(R.id.btn_delete);
            row.setTag(holder);
        } else {
            holder = (AdapterHolder) row.getTag();
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_admin.setText(list.get(position).getAdmin());
        holder.tv_time.setText(list.get(position).getCreateTime());
        if(list.get(position).getIsDone().equals("Xong"))
            holder.btn_complete.setImageResource(R.drawable.ic_baseline_radio_button_checked_24);
        if(list.get(position).getIsDone().equals("Chưa xong"))
            holder.btn_complete.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
        holder.btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(position).getIsDone().equals("Chưa xong")){
                    itemBtnCompleteChild.onComplete(list.get(position).getIdParent(),list.get(position).getId(),list.get(position).getAdmin());
                }
            }
        });
        AdapterHolder finalHolder = holder;
        return row;
    }

    class AdapterHolder {
        TextView tv_name;
        TextView tv_admin;
        TextView tv_time;
        ImageView btn_dropdown;
        ImageView btn_info;
        ImageView btn_delete;
        ImageView btn_complete;
    }
}
