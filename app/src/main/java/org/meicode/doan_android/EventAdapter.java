package org.meicode.doan_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {
    ArrayList<Event_Calendar> arr=new ArrayList<>();
    Context mcontext;
    public EventAdapter(Context context,ArrayList<Event_Calendar> arrayList){
        mcontext=context;
        this.arr=arrayList;
    }
    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.event_calendar,parent,false);
        }
        Event_Calendar event_calendar= (Event_Calendar) getItem(position);
        ImageView imageView=(ImageView) convertView.findViewById(R.id.event_image);
        TextView tittle=(TextView) convertView.findViewById(R.id.event_tittle);
        TextView message=(TextView) convertView.findViewById(R.id.event_message);
        imageView.setImageResource(R.drawable.tasks);
        tittle.setText(event_calendar.getName() + " đến hạn");
        message.setText(event_calendar.getMess());
        return convertView;
    }
}
