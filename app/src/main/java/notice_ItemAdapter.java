package com.example.happy_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class notice_ItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Notice> NB;
    LayoutInflater mLayoutInflater = null;

    public notice_ItemAdapter(Context context, ArrayList<Notice> data){
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);

        NB = new ArrayList<Notice>();


        for(int i=0;i<data.size();i++){
            Notice n = data.get(i);

            NB.add(n);
        }
    }

    public notice_ItemAdapter(){}
    public void addItem(Notice N){NB.add(N);}

    @Override
    public int getCount() {
        return this.NB.size();
    }

    @Override
    public Object getItem(int position) {
        return NB.get(position);
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

        title.setText(NB.get(position).getTitle());
        writer.setText(NB.get(position).getText());

        return convertView;

    }

    public void setSort(){
        //날짜순으로 정렬
        Collections.sort(NB);
    }

    public void clearAllItems() {
        NB.clear();
    }
}