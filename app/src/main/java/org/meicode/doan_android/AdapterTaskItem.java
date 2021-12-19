package org.meicode.doan_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterTaskItem extends BaseExpandableListAdapter{
    //Init
    ArrayList<String>  listGroup;
    HashMap<String,ArrayList<String>> listChild;
    String headerTitle;
    FirebaseDatabase database;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    String userid;
    String tag;
    private TaskMaster.OnCompleteTaskMasterListener masterListener;
    private Context context;
    public AdapterTaskItem(ArrayList<String> listGroup, HashMap<String,ArrayList<String>> listChild, String headerTitle, TaskMaster.OnCompleteTaskMasterListener masterListener)
    {
        this.listChild = listChild;
        this.listGroup = listGroup;
        this.headerTitle = headerTitle;
        this.masterListener = masterListener;
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
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_taskmaster,viewGroup,false);
        ImageView btn_add_task_child = view.findViewById(R.id.btn_add_child_task_right);
        ImageView btn_complete = view.findViewById(R.id.btn_complete);
        int images[] = {R.drawable.task_area, R.drawable.task_subtraction, R.drawable.task_car, R.drawable.task_pen, R.drawable.task_barchart, R.drawable.task_cylinder};
        int amountItem=listChild.get(listGroup.get(i).toString()).size();
        int flag = i;
        if(flag > 5)
            flag=0;
        ImageView img = view.findViewById(R.id.img);
        img.setImageResource(images[flag]);
        if(amountItem==0)
        {
            btn_add_task_child.setVisibility(View.VISIBLE);
        }
        else{
            btn_add_task_child.setVisibility(View.GONE);
        }
        btn_add_task_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TaskChildPerson.class);
                intent.putExtra("NameOfTask",listGroup.get(i).toString());
                intent.putExtra("HeaderTitle",headerTitle);
                view.getContext().startActivity(intent);
            }
        });
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masterListener.onCompleteTaskMaster(listGroup.get(i).toString());
            }
        });
        String sGroup = String.valueOf(getGroup(i));
        TextView tvgroup = view.findViewById(R.id.tv_group);
        tvgroup.setText(sGroup);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_child_taskmaster, viewGroup, false);
        TextView textView = view.findViewById(R.id.tv_content);
        TextView tv_time = view.findViewById(R.id.tv_time);
        String sChild = String.valueOf(getChild(i, i1));
        String[] temp = sChild.split("-",3);
        String content = temp[0];
        String status = temp[1];
        String Time = temp[2];
        tv_time.setText(Time);
        textView.setText(content);
        ImageView btn_add_child_task = view.findViewById(R.id.btn_add_child_task);
        ImageView btn_star = view.findViewById(R.id.btn_star);
        ImageView btn_complete = view.findViewById(R.id.btn_checkbox);
        if (status.equals("Xong")){
            btn_complete.setImageResource(R.drawable.ic_baseline_radio_button_checked_24);
            btn_complete.setEnabled(false);
        }else{
            btn_complete.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
        }
        btn_star.setTag("0");
        btn_add_child_task.setVisibility(View.GONE);
        int amountItem=listChild.get(listGroup.get(i).toString()).size();
        if(amountItem-1==i1)
        {
            btn_add_child_task.setImageResource(R.drawable.ic_baseline_add_circle_24);
            btn_add_child_task.setVisibility(View.VISIBLE);
        }
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),CompleteForm.class);
                intent.putExtra("NameOfChildTask",listChild.get(listGroup.get(i)).get(i1).toString().split("-")[0]);
                intent.putExtra("NameOfTask",listGroup.get(i).toString());
                intent.putExtra("HeaderTitle",headerTitle);
                intent.putExtra("Index Group",i);
                intent.putExtra("Index item",i1);
                view.getContext().startActivity(intent);
                //Toast.makeText(view.getContext(),listChild.get(listGroup.get(i)).get(i1).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Detail_Child_Task.class);
                intent.putExtra("NameOfTask",listGroup.get(i).toString());
                intent.putExtra("NameOfChildTask",listChild.get(listGroup.get(i)).get(i1).toString().split("-")[0]);
                intent.putExtra("HeaderTitle",headerTitle);
                view.getContext().startActivity(intent);
            }
        });
        btn_add_child_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),TaskChildPerson.class);
                intent.putExtra("NameOfTask",listGroup.get(i).toString());
                intent.putExtra("HeaderTitle",headerTitle);
                intent.putExtra("forwardTo","TaskMasterChild");
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
