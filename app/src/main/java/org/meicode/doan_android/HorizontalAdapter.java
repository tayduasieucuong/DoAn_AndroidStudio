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
   public HorizontalAdapter(Context context, ArrayList<TaskGroup> taskGroups, HomePageGroup.itemClickRecycler itemClickRecycler)
   {
       this.context = context;
       this.taskGroups = taskGroups;
       this.itemClickRecycler = itemClickRecycler;
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
          holder.groupName.setText(taskGroups.get(position).getGroupName());
          holder.btn_chat.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Toast.makeText(view.getContext(), "Goto chat", Toast.LENGTH_SHORT).show();
              }
          });
          holder.setItemClickListener(new ItemClickListener() {
              @Override
              public void onClick(View view, int position, boolean isLongClick) {
                  if(isLongClick)
                  {
                      Toast.makeText(context, "Long Click " +taskGroups.get(position), Toast.LENGTH_SHORT).show();
                  }else{
//                      Toast.makeText(context, "Click"+taskGroups.get(position).getGroupId(), Toast.LENGTH_SHORT).show();
                      itemClickRecycler.onSelect(taskGroups.get(position).getGroupId());
                  }
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
       ImageView btn_chat;
       private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            btn_chat = itemView.findViewById(R.id.btn_chat);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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
