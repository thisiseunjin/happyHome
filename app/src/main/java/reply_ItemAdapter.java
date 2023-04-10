package com.example.happy_home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;



public class reply_ItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Reply> RP;
    LayoutInflater mLayoutInflater = null;
    private String ME = ((commuDetail)commuDetail.mContext).ME;

    public reply_ItemAdapter(Context context, ArrayList<Reply> data){
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);

        RP = new ArrayList<Reply>();
        RP.clear();


        for(int i=0;i<data.size();i++){
            Reply r = data.get(i);

            RP.add(r);
        }
        notifyDataSetChanged();
    }

    public reply_ItemAdapter(){}
    public void addItem(Reply R){RP.add(R);}

    @Override
    public int getCount() {
        return this.RP.size();
    }

    @Override
    public Object getItem(int position) {
        return RP.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = mLayoutInflater.inflate(R.layout.reply_item_view,null  );

        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = LayoutInflater.from(context).inflate(R.layout.reply_item_view,parent,false);


        TextView writer = (TextView)convertView.findViewById(R.id.Writer_R);
        TextView reply = (TextView)convertView.findViewById(R.id.Reply);
        ImageView img=(ImageView) convertView.findViewById(R.id.reply_item_img);

        if((RP.get(position).getWriter().contains("동")))
            img.setImageResource(R.drawable.icon);
        else{
            img.setImageResource(R.drawable.admin);
            writer.setTextColor(Color.parseColor("#FF5A5A"));
        }

        writer.setText(RP.get(position).getWriter()); //댓글 작성자 저장
        reply.setText(RP.get(position).getReply()); //댓글 내용 저장

        Button button = (Button) convertView.findViewById(R.id.reply_delete);
        String R_Writer = RP.get(position).Writer;

        if(ME.equals(R_Writer)||ME.equals("관리자")){
            button.setVisibility(Button.VISIBLE);
        }
        else{
            button.setVisibility(Button.INVISIBLE);
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String R_Text = RP.get(position).Reply;
                String R_Phone = RP.get(position).Phone;
                String R_PostKey = RP.get(position).PostKey;
                String R_ReplyKey = RP.get(position).ReplyKey;
                String R_MyAuth = RP.get(position).MyAuth;
                String R_userResident = RP.get(position).user_residence;

                //게시판 속 그 뭐냐 댓글 지우기
                Task<Void> Database_Community = FirebaseDatabase.getInstance()
                        .getReference("residence/"+R_userResident+"/Community/"+R_PostKey+"/Reply/"+R_ReplyKey).removeValue();

                //멤버 속 댓글 모음집 삭제하기기

                Task<Void> database = FirebaseDatabase.getInstance()
                        .getReference(R_MyAuth+"/"+R_Phone+"/Reply/Community/"+R_ReplyKey).removeValue();

                RP = new ArrayList<Reply>();
                RP.clear();

                ArrayList<Reply> data = new ArrayList<>();

                for(int i=0;i<data.size();i++){
                    Reply r = data.get(i);

                    RP.add(r);
                }
                notifyDataSetChanged();

            }
        });

        return convertView;
    }

    public void clearAllItems() {

        RP.clear();
        notifyDataSetChanged();
    }
}