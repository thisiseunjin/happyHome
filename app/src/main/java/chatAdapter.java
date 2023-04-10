package com.example.happy_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Chat> chatList = null;

    public chatAdapter(ArrayList<Chat> dataList){
        chatList= dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == 0){//왼쪽
            view = inflater.inflate(R.layout.chat_left_item,parent,false);
            return new LeftViewHolder(view);
        }
        else if(viewType==1){//중간
            view = inflater.inflate(R.layout.chat_center_item,parent,false);
            return new CenterViewHolder(view);
        }
        else{//오른쪽
            view = inflater.inflate(R.layout.chat_right_item,parent,false);
            return new RightViewHolder(view);
        }
    }

    //실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LeftViewHolder){//왼쪽
            ((LeftViewHolder)holder).name.setText(chatList.get(position).getName());
            ((LeftViewHolder)holder).text.setText(chatList.get(position).getText());
            ((LeftViewHolder)holder).time.setText(chatList.get(position).getTime());
        }
        else if(holder instanceof CenterViewHolder){//중간
            ((CenterViewHolder)holder).date.setText(chatList.get(position).getDate());
        }
        else{//오른쪽
            ((RightViewHolder)holder).text.setText(chatList.get(position).getText());
            ((RightViewHolder)holder).time.setText(chatList.get(position).getTime());
        }
    }

    //리사이클러뷰안에서 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // ★★★
    // 위에 3개만 오버라이드가 기본 셋팅임,
    // 이 메소드는 ViewType때문에 오버라이딩 했음(구별할려고)
    @Override
    public int getItemViewType(int position) {
        return chatList.get(position).getViewType();
    }


    public class LeftViewHolder extends RecyclerView.ViewHolder{

        TextView name,text,time;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.left_chat_nick_name);
            text = (TextView)itemView.findViewById(R.id.left_text);
            time = (TextView)itemView.findViewById(R.id.left_time);

        }
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder{

        TextView date;

        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.center_date);

        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        TextView time;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            text = (TextView)itemView.findViewById(R.id.right_text);
            time = (TextView)itemView.findViewById(R.id.right_time);
        }
    }
}
