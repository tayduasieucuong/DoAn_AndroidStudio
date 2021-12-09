package org.meicode.doan_android;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterHistory extends BaseExpandableListAdapter {
    //Init
    ArrayList<String>  listGroup;
    HashMap<String,ArrayList<String>> listChild;
    public AdapterHistory(ArrayList<String> listGroup, HashMap<String,ArrayList<String>> listChild)
    {
        this.listChild = listChild;
        this.listGroup = listGroup;

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
        String text1,text2;
        String[] temp = sChild.split("/");
        text1 = temp[0];
        text2 = temp[1];
        textView.setText(text1);
        temp = text2.split("%");
        if(Integer.parseInt(temp[0].toString())<=50)
        {
            tv_percent.setTextColor(Color.parseColor("#fc0331"));
        }else{
            tv_percent.setTextColor(Color.parseColor("#76ff03"));
        }
        tv_percent.setText(text2);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
