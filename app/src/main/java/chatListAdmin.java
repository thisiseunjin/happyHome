package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
        import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.Map;

public class chatListAdmin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ArrayList<Chat> chatRoomList = new ArrayList<>();
    RecyclerView chat_list;
    private chatRoomAdapter chatroomadapter=new chatRoomAdapter(chatRoomList);

    String residence;

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list_admin);

        //Sharedpreference 불러온다
        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        residence=sharedPreferences.getString("residence","");

        //채팅방 list Reference
        DatabaseReference chatRoom_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/"+residence+"/Chat_Room");

        //사용자 uid
        mAuth = FirebaseAuth.getInstance();

        //RecyclerView 설정
        chat_list=findViewById(R.id.chatRoom_RecyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        chat_list.setLayoutManager(manager);
        chat_list.setAdapter(chatroomadapter);


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