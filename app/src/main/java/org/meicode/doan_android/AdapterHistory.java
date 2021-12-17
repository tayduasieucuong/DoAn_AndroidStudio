package org.meicode.doan_android;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterHistory extends BaseExpandableListAdapter {
    //Init
    ArrayList<String>  listGroup;
    ArrayList<String> listGroupDetail;
    HashMap<String,ArrayList<String>> listChild;
    public AdapterHistory(ArrayList<String> listGroup, HashMap<String,ArrayList<String>> listChild, ArrayList<String> listGroupDetail)
    {
        this.listChild = listChild;
        this.listGroup = listGroup;
        this.listGroupDetail = listGroupDetail;
    }
    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listChild.get(this.listGroup.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        //return group item
        return listGroup.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listChild.get(listGroup.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_group_history,viewGroup,false);
        TextView textView = view.findViewById(R.id.tv_title);
        TextView tv_time = view.findViewById(R.id.tv_time);
        TextView tv_status = view.findViewById(R.id.tv_status);
        ImageView img_status = view.findViewById(R.id.dot_status);
        String[] dataDetail = listGroupDetail.get(i).split("-",3);
        tv_time.setText(dataDetail[0]+" -> "+dataDetail[1]);
        tv_status.setText(dataDetail[2]);
        if(dataDetail[2].equals("Xong"))
            img_status.setImageResource(R.drawable.ic_dot);
        else
            img_status.setImageResource(R.drawable.ic_dot_red);
        String sGroup = String.valueOf(getGroup(i));
        textView.setText(sGroup);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_history,viewGroup,false);
        TextView textView = view.findViewById(R.id.tv_name);
        TextView tv_percent = view.findViewById(R.id.tv_percent);
        String sChild = String.valueOf(getChild(i,i1));
        String title,dateCompleted, percent;
        String[] temp = sChild.split("=",3);
        title = temp[0].split("/")[0].toString();
        dateCompleted = temp[1].split(",")[0].toString();
        percent = temp[2].split("%")[0].toString();
        textView.setText(title);
        if(Integer.parseInt(percent)<=50)
        {
            tv_percent.setTextColor(Color.parseColor("#fc0331"));
        }else{
            tv_percent.setTextColor(Color.parseColor("#76ff03"));
        }
        tv_percent.setText(percent+"%");
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
