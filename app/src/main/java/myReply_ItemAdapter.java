package com.example.happy_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class myReply_ItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Reply> MY_RP;
    LayoutInflater mLayoutInflater = null;
    //private String ME = ((commuDetail)commuDetail.mContext).ME;


    public myReply_ItemAdapter(Context context, ArrayList<Reply> data){
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);

        MY_RP = new ArrayList<Reply>();
        MY_RP.clear();


        for(int i=0;i<data.size();i++){
            Reply r = data.get(i);

            MY_RP.add(r);
        }
        notifyDataSetChanged();
    }

    public myReply_ItemAdapter(){}

    public void addItem(Reply R){MY_RP.add(R);}

    @Override
    public int getCount() {
        return this.MY_RP.size();
    }

    @Override
    public Object getItem(int position) {
        return MY_RP.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View view = mLayoutInflater.inflate(R.layout.my_reply_item_view,null  );

        convertView = LayoutInflater.from(context).inflate(R.layout.my_reply_item_view,parent,false);

        TextView reply = (TextView) convertView.findViewById(R.id.replyText);
        TextView postTitle= (TextView)convertView.findViewById(R.id.Title_inmyReply);

        reply.setText(MY_RP.get(position).getReply());
        postTitle.setText(MY_RP.get(position).getPostTitle());



        return convertView;


    }

    public void clearAllItems(){
        MY_RP.clear();
        notifyDataSetChanged();
    }
}
