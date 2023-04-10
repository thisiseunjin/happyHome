package com.example.happy_home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class chatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Chat> chatRoomList = null;
    RecyclerView recyclerView;
    Context context;
    View view;

    public chatRoomAdapter(ArrayList<Chat> dataList){
        chatRoomList= dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.chat_room_item,parent,false);

        return new RoomViewHolder(view);
    }

    //실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((chatRoomAdapter.RoomViewHolder)holder).name.setText(chatRoomList.get(position).getRoom_name());
        ((chatRoomAdapter.RoomViewHolder)holder).text.setText(chatRoomList.get(position).getText());
        ((chatRoomAdapter.RoomViewHolder)holder).date.setText(chatRoomList.get(position).getDate());

        //읽음 유무 알려줌
        if(chatRoomList.get(position).isRead())//읽었다면
            ((chatRoomAdapter.RoomViewHolder)holder).chat_read.setVisibility(View.INVISIBLE);//안보이게


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int position= holder.getChildAdapterPosition(view);
                int position= holder.getAdapterPosition();

                if(position!=RecyclerView.NO_POSITION){

                    Intent intent = new Intent(context, Chatting.class);
                    //채팅방이름 다시 적어야함!!!!!!!!!
                    intent.putExtra("room",chatRoomList.get(position).getRoom_name());
                    context.startActivity(intent);
                }
            }
        });
    }

    //리사이클러뷰안에서 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    // ★★★
    // 위에 3개만 오버라이드가 기본 셋팅임,
    // 이 메소드는 ViewType때문에 오버라이딩 했음(구별할려고)
    @Override
    public int getItemViewType(int position) {
        return chatRoomList.get(position).getViewType();
    }


    public class RoomViewHolder extends RecyclerView.ViewHolder{

        TextView name,text,date,chat_read;

        public RoomViewHolder(@NonNull View itemView) {

            super(itemView);
            name = (TextView)itemView.findViewById(R.id.room_name);
            text = (TextView)itemView.findViewById(R.id.room_text);
            date = (TextView)itemView.findViewById(R.id.room_time);
            chat_read=(TextView) itemView.findViewById(R.id.chat_read);
        }
    }
}
