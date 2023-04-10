package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class commuList extends AppCompatActivity {

    String ME;
    ArrayList<Community> clist=new ArrayList<>();

    commu_ItemAdapter commuItemAdapter;
    String user_residence;
    String Title,Text,My_Phone;
    String PostKey;
    EditText Search;

    final Context context=this;

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
        setContentView(R.layout.activity_commu_list);

        Intent intent=getIntent();

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        ME = sharedPreferences.getString("nick_name","");//작성자가 누군지 알려줄거임 ~동~호
        user_residence = sharedPreferences.getString("residence", "");//집위치
        My_Phone = sharedPreferences.getString("phone_num","");    //휴대전화번호 알아낼꺼임
        authority=sharedPreferences.getInt("authority",1);

        Search = (EditText)findViewById(R.id.commu_search_text);
        ListView commu_list = (ListView)findViewById(R.id.c_listview);

        commuItemAdapter = new commu_ItemAdapter(this,clist);
        commu_list.setAdapter(commuItemAdapter);
        commuItemAdapter.notifyDataSetChanged();

        this.make_clist();

        //게시글 상세 페이지
        commu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //게시글 상세페이지로 이동
                Intent detail_intent= new Intent(getApplicationContext(),commuDetail.class);

                detail_intent.putExtra("Phone",clist.get(position).Phone);
                detail_intent.putExtra("Time",clist.get(position).Time);
                detail_intent.putExtra("Title",clist.get(position).Title);
                detail_intent.putExtra("Text",clist.get(position).Text);
                detail_intent.putExtra("Writer",clist.get(position).Writer);
                detail_intent.putExtra("PostKey",clist.get(position).PostKey);
                detail_intent.putExtra("How","commuList");

                startActivity(detail_intent);
            }
        });

        Button commu_write = (Button) findViewById(R.id.commu_towrite_bt);
        //글쓰기 페이지로 이동
        if(ME.equals("관리자")){
            commu_write.setVisibility(View.GONE);
        }
        else{
            commu_write.setVisibility(View.VISIBLE);
        }

        commu_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent edit_intent = new Intent(getApplicationContext(), commuWrite.class);
                startActivity(edit_intent);
            }
        });

        //검색
        Button Csearch = (Button)findViewById(R.id.commu_search_bt);
        Csearch.setOnClickListener(new AdapterView.OnClickListener(){
            public void onClick(View view){

                String searchTitle = Search.getText().toString();


                if(searchTitle.equals("")){
                    Toast.makeText(getApplicationContext(), "검색어를 입력하세요!", Toast.LENGTH_SHORT).show();
                }
                else{
                    commuItemAdapter.clearAllItems();
                    clist.clear();
                    commuItemAdapter.notifyDataSetChanged();

                    CSearch(searchTitle);
                }
            }
        });
    }

    public void make_clist(){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("residence/"+user_residence+"/Community");

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
                    clist.add(C);

                    commuItemAdapter.notifyDataSetChanged();
                    commuItemAdapter.addItem(C);
                    commuItemAdapter.notifyDataSetChanged();

                    //날짜순으로 정렬
                    Collections.sort(clist);
                    commuItemAdapter.setSort();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void CSearch(String search){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("residence/"+user_residence+"/Community");

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

                        Community C = new Community(Time,Title,Writer,Text,Phone);

                        clist.add(C);
                        commuItemAdapter.addItem(C);
                        commuItemAdapter.notifyDataSetChanged();

                        //날짜순으로 정렬
                        Collections.sort(clist);
                        commuItemAdapter.setSort();

                    }
                }

                if(clist.size()==0||clist.equals("")){
                    //검색어가 없을 경우
                    //Toast.makeText(getApplicationContext(), "검색결과가 없습니다", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("소통하는 주거환경");

                    builder.setMessage("검색 결과가 없습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    make_clist();
                                }
                            });


                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}