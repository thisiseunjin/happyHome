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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chatting extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ArrayList<Chat> chatList = new ArrayList<>();
    RecyclerView chat_list;
    private chatAdapter chatadapter=new chatAdapter(chatList);
    int authority;
    String ref_nick_name,residence,nick_name;

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        if(authority==0){
            Intent intent= new Intent(getApplicationContext(), Fragment_admin_main.class);
            intent.putExtra("menu_type",1);
            startActivity(intent);
        }
        else{
            //다음 화면으로 이동
            Intent intent = new Intent(getApplicationContext(), Fragment_mem_main.class);
            intent.putExtra("menu_type",0);
            startActivity(intent);
        }
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        //Sharedpreference 불러온다
        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        residence=sharedPreferences.getString("residence","");
        authority=sharedPreferences.getInt("authority",1);
        nick_name=sharedPreferences.getString("nick_name","");

        if(authority==0){//관리자
            Intent intent=getIntent();
            ref_nick_name=intent.getExtras().getString("room");
        }
        else//일반 회원인 경우
            ref_nick_name=sharedPreferences.getString("nick_name","");


        //채팅방 list Reference
        DatabaseReference chatRoom_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/"+residence+"/Chat_Room/"+ref_nick_name+"/chats");

        //사용자 uid
        mAuth = FirebaseAuth.getInstance();

        EditText send_text=findViewById(R.id.send_text);
        Button send_button=findViewById(R.id.sendchat_bt);
        TextView room_name=findViewById(R.id.chat_room_name);;

        //채팅방이름 설정
        if(nick_name.equals("관리자"))
            room_name.setText(ref_nick_name);
        else
            room_name.setText("관리자");

        //RecyclerView
        chat_list=findViewById(R.id.chat_list);
        LinearLayoutManager manager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        chat_list.setLayoutManager(manager);
        //chat_list.setAdapter(new chatAdapter(chatList));
        chat_list.setAdapter(chatadapter);


        send_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //채팅방 uid
                Map<String, Object> map = new HashMap<String, Object>();

                String key=chatRoom_Ref.push().getKey();
                chatRoom_Ref.updateChildren(map);

                DatabaseReference chat_Ref = chatRoom_Ref.child(key);

                Map<String, Object> objectMap = new HashMap<String, Object>();

                objectMap.put("room_name",ref_nick_name);
                objectMap.put("name",nick_name);
                objectMap.put("text",send_text.getText().toString());
                objectMap.put("read",false);

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat long_date_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat short_date = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat short_time = new SimpleDateFormat("aa hh:mm");

                objectMap.put("long_date_time",long_date_time.format(date));
                objectMap.put("short_date",short_date.format(date));
                objectMap.put("short_time",short_time.format(date));

                //last에 추가
                HashMap<String, Object> last_chat = new HashMap();

                DatabaseReference last_ref = FirebaseDatabase.getInstance().getReference();
                last_chat.put("residence/"+residence+"/Chat_Room/"+ref_nick_name+"/last_chat", objectMap);
                last_ref.updateChildren(last_chat);

                chat_Ref.updateChildren(objectMap);//chats에 추가
                send_text.setText("");//채팅창 지우기
            }
        });

        //채팅방 파이어베이스에서 가져오기
        chatRoom_Ref.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s){
                chatConversation(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s){
                chatConversation(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot){

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s){

            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

    private void chatConversation(DataSnapshot dataSnapshot){//"/chatRoom/nickname/chats"

        Iterator i = dataSnapshot.getChildren().iterator();

        //RecyclerView
        while(i.hasNext()) {//각 호수


            String chat_long_date = (String) ((DataSnapshot) i.next()).getValue();
            String chat_user = (String) ((DataSnapshot) i.next()).getValue();
            Boolean read = (Boolean) ((DataSnapshot) i.next()).getValue();
            String chat_room_name=(String)((DataSnapshot)i.next()).getValue();
            String chat_short_date = (String) ((DataSnapshot) i.next()).getValue();
            String chat_short_time = (String) ((DataSnapshot) i.next()).getValue();
            String chat_text = (String) ((DataSnapshot) i.next()).getValue();

            int viewType;

            //날짜비교부터
            if (chatList.size() == 0)
                chatList.add(new Chat(chat_room_name,chat_user, chat_text, chat_long_date,chat_short_date, chat_short_time, 1, read));
            else if (!chatList.get(chatList.size() - 1).getDate().equals(chat_short_date))
                chatList.add(new Chat(chat_room_name,chat_user, chat_text, chat_long_date, chat_short_date, chat_short_time, 1, read));


            //말풍선 위치설정
            if (chat_user.equals(nick_name))//본인:오른쪽
                viewType = 2;
            else//상대방:왼쪽
                viewType = 0;

            chatList.add(new Chat(chat_room_name,chat_user, chat_text, chat_long_date, chat_short_date, chat_short_time, viewType, read));
        }

        //마지막 채팅을 보낸 사람이 상대방이라면, 이전의 채팅이 있다면
        DatabaseReference lastchat_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/"+residence+"/Chat_Room/"+ref_nick_name+"/last_chat");

        if((chatList.size()!=0)&&nick_name.equals("관리자")&&(!lastchat_Ref.child("name").get().equals(nick_name))){

            Map<String, Object> objectMap = new HashMap<String, Object>();
            objectMap.put("read",true);
            lastchat_Ref.updateChildren(objectMap);
        }

        chatadapter.notifyDataSetChanged();
        chat_list.scrollToPosition(chatList.size()-1);
    }

}