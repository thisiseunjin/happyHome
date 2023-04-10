package com.example.happy_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class commu_ItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Community> CB;
    LayoutInflater mLayoutInflater = null;

    public commu_ItemAdapter(Context context, ArrayList<Community> data){
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);

        CB = new ArrayList<Community>();


        for(int i=0;i<data.size();i++){
            Community c = data.get(i);

            CB.add(c);
        }
        notifyDataSetChanged();
    }

    public commu_ItemAdapter(){}
    public void addItem(Community C){CB.add(C);}

    @Override
    public int getCount() {
        return this.CB.size();
    }

    @Override
    public Object getItem(int position) {
        return CB.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = mLayoutInflater.inflate(R.layout.item_view,null  );
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);

        TextView title = (TextView)convertView.findViewById(R.id.Title_inmyReply);
        TextView writer = (TextView)convertView.findViewById(R.id.Writer);

        title.setText(CB.get(position).getTitle());
        writer.setText(CB.get(position).getText());

        return convertView;

    }

    public void setSort(){
        //날짜순으로 정렬
        Collections.sort(CB);
    }

    public void clearAllItems() {
        CB.clear();
        notifyDataSetChanged();
    }
}