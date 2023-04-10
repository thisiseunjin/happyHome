package com.example.happy_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class myPost_ItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Community> MP;
    LayoutInflater mLayoutInflater = null;

    public myPost_ItemAdapter(Context context,ArrayList<Community> data){
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);

        MP = new ArrayList<Community>();

        for(int i=0;i<data.size();i++){
            Community my_c = data.get(i);

            MP.add(my_c);
        }
    }

    public myPost_ItemAdapter(){}

    public void addItem(Community C){MP.add(C);}

    @Override
    public int getCount() {
        return this.MP.size();
    }

    @Override
    public Object getItem(int position) {
        return MP.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.my_post_item_view,null);
        convertView = LayoutInflater.from(context).inflate(R.layout.my_post_item_view,parent,false);

        TextView title = (TextView)convertView.findViewById(R.id.Title_inmyReply);
        TextView time = (TextView)convertView.findViewById(R.id.whenPost);

        title.setText(MP.get(position).getTitle());
        time.setText(MP.get(position).getTime());



        return convertView;
    }

    public void clearAllItems(){
        MP.clear();
        notifyDataSetChanged();
    }


}
