package org.meicode.doan_android;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

   ArrayList<TaskGroup> taskGroups;
   Context context;
   private HomePageGroup.itemClickRecycler itemClickRecycler;
   private HomePageGroup.itemBtnComplete itemBtnComplete;
   private HomePageGroup.itemBtnDelete itemBtnDelete;
   public HorizontalAdapter(Context context, ArrayList<TaskGroup> taskGroups, HomePageGroup.itemClickRecycler itemClickRecycler, HomePageGroup.itemBtnComplete itemBtnComplete,HomePageGroup.itemBtnDelete itemBtnDelete)
   {
       this.context = context;
       this.taskGroups = taskGroups;
       this.itemClickRecycler = itemClickRecycler;
       this.itemBtnComplete = itemBtnComplete;
       this.itemBtnDelete = itemBtnDelete;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.img_view.setImageResource(taskGroups.get(position).getImg());
//        holder.tv_content.setText(taskGroups.get(position).getContent());
          holder.groupName.setText(taskGroups.get(position).getGroupTask());
          holder.time_cr.setText("Ngày tạo: "+taskGroups.get(position).TaskTime);
          if (taskGroups.get(position).getDone().equals("Xong"))
          {
              holder.btn_complete.setImageResource(R.drawable.ic_baseline_radio_button_checked_24);
          }
          holder.setItemClickListener(new ItemClickListener() {
              @Override
              public void onClick(View view, int position, boolean isLongClick) {
                  if(isLongClick)
                  {
                      Toast.makeText(context, "Long Click " +taskGroups.get(position), Toast.LENGTH_SHORT).show();
                  }else{
//                      Toast.makeText(context, "Click"+taskGroups.get(position).getGroupId(), Toast.LENGTH_SHORT).show();
                      itemClickRecycler.onSelect(taskGroups.get(position).getIdParent(),taskGroups.get(position).getGroupTask());
                  }
              }
          });
          holder.btn_complete.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (taskGroups.get(position).getDone().equals("Chưa xong"))
                  {
                      itemBtnComplete.onComplete(taskGroups.get(position).getIdParent(),taskGroups.get(position).getGroupTask());
                  }
              }
          });
          holder.btn_delete.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  itemBtnDelete.onDelete(taskGroups.get(position).getIdParent(),taskGroups.get(position).getGroupTask());
              }
          });
    }

    @Override
    public int getItemCount() {
        return taskGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
       ImageView img_view;
       TextView groupName;
       TextView time_cr;
       ImageView btn_complete;
       ImageView btn_delete;
       private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            time_cr = itemView.findViewById(R.id.tv_time_item);
            btn_complete = itemView.findViewById(R.id.btn_complete);
            btn_delete = itemView.findViewById(R.id.btn_delete);
//            img_view = itemView.findViewById(R.id.img_view);
//            tv_content = itemView.findViewById(R.id.content);
        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v){
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),true);
            return true;
        }
    }
}
