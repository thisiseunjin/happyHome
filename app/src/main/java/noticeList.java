package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class noticeList extends AppCompatActivity {

    String ME;
    ArrayList<Notice> nlist=new ArrayList<>();
    notice_ItemAdapter notice_Adapter;
    String user_residence;
    String Title,Text,My_Phone;
    String PostKey;
    EditText Search;

    int authority;
    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        if(authority==0){
            Intent intent= new Intent(getApplicationContext(), Fragment_admin_main.class);
            intent.putExtra("menu_type",0);
            startActivity(intent);
        }
        else{
            //다음 화면으로 이동
            Intent intent = new Intent(getApplicationContext(), Fragment_mem_main.class);
            intent.putExtra("menu_type",0);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        Intent intent=getIntent();

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        ME = sharedPreferences.getString("nick_name","");//작성자가 누군지 알려줄거임 ~동~호
        user_residence = sharedPreferences.getString("residence", "");//집위치
        My_Phone = sharedPreferences.getString("phone_num","");    //휴대전화번호 알아낼꺼임

        authority=sharedPreferences.getInt("authority",1);

        //글쓰기버튼 보일지 안보일지 정한다
        int authority=sharedPreferences.getInt("authority",0);
        Button notice_write = (Button) findViewById(R.id.notice_towrite_bt);

        if(authority==1)
            notice_write.setVisibility(View.GONE);

        Search = (EditText)findViewById(R.id.notice_search_text);
        ListView notice_list = (ListView)findViewById(R.id.n_listview);

        notice_Adapter = new notice_ItemAdapter(this,nlist);
        notice_list.setAdapter(notice_Adapter);
        notice_Adapter.notifyDataSetChanged();

        this.make_nlist();

        notice_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //게시글 상세페이지로 이동
                Intent detail_intent= new Intent(getApplicationContext(),noticeDetail.class);

                detail_intent.putExtra("Phone",nlist.get(position).Phone);
                detail_intent.putExtra("Time",nlist.get(position).Time);
                detail_intent.putExtra("Title",nlist.get(position).Title);
                detail_intent.putExtra("Text",nlist.get(position).Text);
                detail_intent.putExtra("Writer",nlist.get(position).Writer);
                detail_intent.putExtra("PostKey",nlist.get(position).PostKey);

                startActivity(detail_intent);
            }
        });


        //글쓰기 페이지로 이동
        notice_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent edit_intent = new Intent(getApplicationContext(), noticeWrite.class);
                startActivity(edit_intent);
            }
        });

        //검색
        Button Nsearch = (Button)findViewById(R.id.notice_search_bt);
        Nsearch.setOnClickListener(new AdapterView.OnClickListener(){
            public void onClick(View view){

                String searchTitle = Search.getText().toString();


                if(searchTitle.equals("")){
                    Toast.makeText(getApplicationContext(), "검색어를 입력하세요!", Toast.LENGTH_SHORT).show();
                }
                else{
                    notice_Adapter.clearAllItems();
                    nlist.clear();
                    notice_Adapter.notifyDataSetChanged();

                    NSearch(searchTitle);

                }
            }
        });
    }

    public void make_nlist(){


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("residence/"+user_residence+"/Notice");

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


                    Notice N = new Notice(Time,Title,Writer,Text,Phone);
                    N.setPost_Key(PostKey);
                    nlist.add(N);

                    notice_Adapter.notifyDataSetChanged();
                    notice_Adapter.addItem(N);
                    notice_Adapter.notifyDataSetChanged();

                    //날짜순으로 정렬
                    Collections.sort(nlist);
                    notice_Adapter.setSort();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void NSearch(String search){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("residence/"+user_residence+"/Notice");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {

                    String S_Title = ds.child("Title").getValue(String.class);


                    if(S_Title.contains(search)){

                        String Phone = ds.child("Phone").getValue(String.class);
                        String Time = ds.child("Time").getValue(String.class);
                        Title = ds.child("Title").getValue(String.class);
                        Text = ds.child("Text").getValue(String.class);
                        String Writer = ds.child("Writer").getValue(String.class);

                        Notice N = new Notice(Time,Title,Writer,Text,Phone);

                        nlist.add(N);
                        notice_Adapter.addItem(N);
                        notice_Adapter.notifyDataSetChanged();

                        //날짜순으로 정렬
                        Collections.sort(nlist);
                        notice_Adapter.setSort();

                    }
                }

                if(nlist.size()==0||nlist.equals("")){
                    Toast.makeText(getApplicationContext(), "검색결과가 없습니다", Toast.LENGTH_SHORT).show();
                    make_nlist();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}