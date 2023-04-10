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

public class facilityList extends AppCompatActivity {

    private ArrayList<Facility> facilityList = new ArrayList<>();
    RecyclerView facility_list;
    private facilityAdapter facilityadapter = new facilityAdapter(facilityList);

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
        setContentView(R.layout.activity_facility_list);

        //Sharedpreference 불러온다
        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        residence = sharedPreferences.getString("residence", "");

        //편의시설 list Reference
        DatabaseReference facility_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/" + residence + "/facility");

        //RecyclerView 설정
        facility_list = findViewById(R.id.facilityList_RecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        facility_list.setLayoutManager(manager);
        facility_list.setAdapter(facilityadapter);


        //채팅방 파이어베이스에서 가져오기
        facility_Ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                facility(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                facility(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                facility(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void facility(DataSnapshot dataSnapshot) {//facility/

        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){//각 편의시설

            int current_num=((DataSnapshot)i.next()).getValue(Integer.class);
            String device_code =((DataSnapshot) i.next()).getValue(String.class);
            String facility_name =((DataSnapshot) i.next()).getValue(String.class);
            int max_num=((DataSnapshot)i.next()).getValue(Integer.class);

            for(int j=0;j<facilityList.size();j++){
                if(facilityList.get(j).getName().equals(facility_name)) {//이미 있는 편의시설이라면
                    facilityList.remove(j);//없앤다
                    break;
                }
            }

            //편의시설 목록에 추가
            int level;
            double tmp=current_num/(double)max_num;

            if(tmp>0.7)//혼잡
                level=3;
            else if(0.3<=tmp&&tmp<=0.7)//보통
                level=2;
            else//여유
                level=1;

            facilityList.add(new Facility(facility_name,device_code,max_num,current_num,level));
        }

        //편의시설이름순으로 정렬
        Collections.sort(facilityList);

        facilityadapter.notifyDataSetChanged();
    }
}
