package com.example.happy_home;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class Fragment_adminChat extends Fragment {

    View v;
    private FirebaseAuth mAuth;
    private ArrayList<Chat> chatRoomList = new ArrayList<>();
    RecyclerView chat_list;
    private chatRoomAdapter chatroomadapter=new chatRoomAdapter(chatRoomList);

    String residence;

    public Fragment_adminChat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_admin_chat, container, false);

        //RecyclerView 설정
        chat_list=v.findViewById(R.id.chatRoom_RecyclerViewF);
        LinearLayoutManager manager=new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false);
        chat_list.setLayoutManager(manager);
        chat_list.setAdapter(chatroomadapter);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FirebaseUser currentUser = mAuth.getCurrentUser();

        //Sharedpreference 불러온다
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("my_info", MODE_PRIVATE);
        residence=sharedPreferences.getString("residence","");

        //채팅방 list Reference
        DatabaseReference chatRoom_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/"+residence+"/Chat_Room");

        //채팅방 파이어베이스에서 가져오기
        chatRoom_Ref.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s){
                chattingRooms(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s){
                chattingRooms(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot){
                chattingRooms(dataSnapshot);
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s){

            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

    private void chattingRooms(DataSnapshot dataSnapshot){//"/chatRoom/"

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat short_date = new SimpleDateFormat("yyyy-MM-dd");
        String short_date_string=short_date.format(date);


        for(DataSnapshot ds : dataSnapshot.getChildren()) {//각 호수

            if(ds.getKey().equals("last_chat")){

                String chat_last_long_date=ds.child("long_date_time").getValue(String.class);
                String chat_user=ds.child("name").getValue(String.class);
                String chat_room_name=ds.child("room_name").getValue(String.class);
                String chat_last_short_date=ds.child("short_date").getValue(String.class);
                String chat_last_short_time=ds.child("short_time").getValue(String.class);
                String chat_last_text=ds.child("text").getValue(String.class);
                Boolean read=ds.child("read").getValue(Boolean.class);


                for(int i=0;i<chatRoomList.size();i++){
                    if(chatRoomList.get(i).getRoom_name().equals(chat_room_name)) {//이미 있는 채팅방이라면
                        chatRoomList.remove(i);//없앤다
                        break;
                    }
                }

                //채팅방목록에 추가
                //!!!시간이나 날짜형식 바꾸려면 여기서!!!
                if(chat_last_short_date.equals(short_date_string)) //같은 날
                    chatRoomList.add(new Chat(chat_room_name,chat_user,chat_last_text,chat_last_long_date, chat_last_short_time,chat_last_short_time,3, read));

                else//다른 날
                    chatRoomList.add(new Chat(chat_room_name,chat_user,chat_last_text,chat_last_long_date, chat_last_short_date,chat_last_short_time,3, read));

            }
        }

        //날짜순으로 정렬
        Collections.sort(chatRoomList);

        chatroomadapter.notifyDataSetChanged();
    }
}