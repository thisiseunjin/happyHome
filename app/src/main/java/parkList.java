package com.example.happy_home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class parkList extends AppCompatActivity {

    private ArrayList<Park> parkList = new ArrayList<>();
    RecyclerView park_list;
    private parkAdapter parkadapter = new parkAdapter(parkList);

    String residence;

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        Intent intent= new Intent(getApplicationContext(), Fragment_mem_main.class);
        intent.putExtra("menu_type",0);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_list);

        //Sharedpreference 불러온다
        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        residence = sharedPreferences.getString("residence", "");

        //주차장 list Reference
        DatabaseReference park_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/" + residence + "/parking");

        //RecyclerView 설정
        park_list = findViewById(R.id.parkList_RecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        park_list.setLayoutManager(manager);
        park_list.setAdapter(parkadapter);


        //주차장목록 파이어베이스에서 가져오기
        park_Ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                parking(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                parking(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                parking(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void parking(DataSnapshot dataSnapshot) {//parking/

        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){//각 주차장

            int current_num=((DataSnapshot)i.next()).getValue(Integer.class);
            String device_code =((DataSnapshot) i.next()).getValue(String.class);
            String park_name =((DataSnapshot) i.next()).getValue(String.class);
            int max_num=((DataSnapshot)i.next()).getValue(Integer.class);

            for(int j=0;j<parkList.size();j++){
                if(parkList.get(j).getName().equals(park_name)) {//이미 있는 주차장이라면
                    parkList.remove(j);//없앤다
                    break;
                }
            }

            //주차장목록에 추가
            int level;
            double tmp=current_num/(double)max_num;

            if(tmp==1)//만차
                level=3;
            else if(0.7<tmp&&tmp<1)//혼잡
                level=2;
            else//여유
                level=1;

            parkList.add(new Park(park_name,device_code,max_num,current_num,max_num-current_num,level));
        }

        //주차장이름순으로 정렬
        Collections.sort(parkList);

        parkadapter.notifyDataSetChanged();
    }
}
