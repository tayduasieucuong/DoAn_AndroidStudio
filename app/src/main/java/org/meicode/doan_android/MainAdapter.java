package org.meicode.doan_android;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainAdapter extends BaseExpandableListAdapter {
    //Init
    ArrayList<String>  listGroup;
    HashMap<String,ArrayList<String>> listChild;
    public MainAdapter(ArrayList<String> listGroup, HashMap<String,ArrayList<String>> listChild)
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
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_group,viewGroup,false);
        TextView textView = view.findViewById(R.id.tv_group);
        String sGroup = String.valueOf(getGroup(i));
        textView.setText(sGroup);
        ImageView btn_detail = view.findViewById(R.id.btn_detail);
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TaskMaster.class);
                intent.putExtra("HeaderTitle",sGroup);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item,viewGroup,false);
        TextView textView = view.findViewById(R.id.tv_item);

        String sChild = String.valueOf(getChild(i,i1));
        textView.setText(sChild);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
