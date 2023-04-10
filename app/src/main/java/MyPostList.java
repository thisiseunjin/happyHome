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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPostList extends AppCompatActivity {
    //내 게시글 모아보기 누르면 commuDetail로 이동
    String ME;
    ArrayList<Community> myPostList = new ArrayList<>();

    myPost_ItemAdapter myPostItemAdapter;
    String MyAuth;
    String PostKey;
    String user_residence;
    String Title,Text,My_Phone;

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
        setContentView(R.layout.activity_my_post_list);

        Intent intent = getIntent();

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        ME = sharedPreferences.getString("nick_name","");//작성자가 누군지 알려줄거임 ~동~호
        user_residence = sharedPreferences.getString("residence", "");//집위치
        My_Phone = sharedPreferences.getString("phone_num","");    //휴대전화번호 알아낼꺼임
        MyAuth = sharedPreferences.getString("MyAuth","");

        ListView myPost_list= (ListView)findViewById(R.id.myPostListView);

        myPostItemAdapter = new myPost_ItemAdapter(this,myPostList);
        myPost_list.setAdapter(myPostItemAdapter);
        myPostItemAdapter.notifyDataSetChanged();

        //내가 쓴 글 목록 만들어주기
        this.make_myPostList();

        myPost_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //게시글 상세 페이지로 이동
                Intent detail_intent= new Intent(getApplicationContext(),commuDetail.class);

                detail_intent.putExtra("Phone",myPostList.get(position).Phone);
                detail_intent.putExtra("Time",myPostList.get(position).Time);
                detail_intent.putExtra("Title",myPostList.get(position).Title);
                detail_intent.putExtra("Text",myPostList.get(position).Text);
                detail_intent.putExtra("Writer",myPostList.get(position).Writer);
                detail_intent.putExtra("PostKey",myPostList.get(position).PostKey);
                detail_intent.putExtra("How","myPostList");


                startActivity(detail_intent);
            }
        });


    }

    public void make_myPostList() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(MyAuth+"/"+My_Phone+"/Board/Community");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {

                    PostKey = ds.getKey();

                    String Phone = ds.child("Phone").getValue(String.class);
                    String Time = ds.child("Time").getValue(String.class);
                    Title = ds.child("Title").getValue(String.class);
                    Text = ds.child("Text").getValue(String.class);
                    String Writer = ds.child("Writer").getValue(String.class);


                    Community C = new Community(Time,Title,Writer,Text,Phone);
                    C.setPost_Key(PostKey);
                    myPostList.add(C);


                    myPostItemAdapter.notifyDataSetChanged();

                    myPostItemAdapter.addItem(C);
                    myPostItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}