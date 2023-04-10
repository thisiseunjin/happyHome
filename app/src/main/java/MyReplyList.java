package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyReplyList extends AppCompatActivity {

    String ME;
    ArrayList<Reply> Rlist = new ArrayList<>();

    myReply_ItemAdapter myReplyItemAdapter;
    String MyAuth;
    String PostKey,ReplyKey;
    String user_residence;
    String Title,Text,My_Phone;
    String replyTime;
    String Reply;
    String Time;
    String Writer;
    String Phone;
    String postWhere;

    final Context context=this;

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), Fragment_mem_main.class);
        intent.putExtra("menu_type",2);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reply_list);

        Intent intent = getIntent();

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        ME = sharedPreferences.getString("nick_name","");//작성자가 누군지 알려줄거임 ~동~호
        user_residence = sharedPreferences.getString("residence", "");//집위치
        My_Phone = sharedPreferences.getString("phone_num","");    //휴대전화번호 알아낼꺼임
        MyAuth = sharedPreferences.getString("MyAuth","");

        ListView myPost_list= (ListView)findViewById(R.id.myReplyList);

        myReplyItemAdapter = new myReply_ItemAdapter(this,Rlist);
        myPost_list.setAdapter(myReplyItemAdapter);
        myReplyItemAdapter.notifyDataSetChanged();

        //내가 쓴 댓글들 보여주기
        this.make_myReplyList();

        //내댓글 클릭하면?
        myPost_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                //게시글 상세 페이지로 이동
                Intent detail_intent = new Intent(getApplicationContext(),commuDetail.class);
                postWhere = Rlist.get(position).PostKey;
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                DatabaseReference postRef = database.getReference("residence/"+user_residence+"/Community/"+postWhere+"/");



                detail_intent.putExtra("Phone",Phone);
                detail_intent.putExtra("Time",Time);
                detail_intent.putExtra("Title",Title);
                detail_intent.putExtra("Text",Text);
                detail_intent.putExtra("Writer",Writer);
                detail_intent.putExtra("PostKey",PostKey);
                detail_intent.putExtra("How","myReplyList");
                startActivity(detail_intent);
            }
        });
    }

    public void make_myReplyList(){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(MyAuth+"/"+My_Phone+"/Reply/Community");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {

                    ReplyKey = ds.getKey();

                    My_Phone = ds.child("R_Phone").getValue(String.class);
                    replyTime = ds.child("R_Time").getValue(String.class);
                    ME = ds.child("R_Writer").getValue(String.class);
                    Reply = ds.child("Reply").getValue(String.class);
                    Title = ds.child("PostTitle").getValue(String.class);
                    PostKey = ds.child("PostKey").getValue(String.class);

                    Writer=ds.child("PostWriter").getValue(String.class);
                    Phone = ds.child("PostPhone").getValue(String.class);
                    Time = ds.child("PostTime").getValue(String.class);
                    Text = ds.child("PostText").getValue(String.class);

                    Reply R = new Reply( replyTime,  ME,  Reply, My_Phone, PostKey, ReplyKey, MyAuth, user_residence,Title,Writer,Text,Phone,Time);
                    Rlist.add(R);


                    myReplyItemAdapter.addItem(R);
                    myReplyItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}