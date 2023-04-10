package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class facility_act extends AppCompatActivity {

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= new Intent(getApplicationContext(), facilityList.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility);

        TextView facility_name_view=(TextView) findViewById(R.id.name_facility);
        TextView max_facility_view=(TextView) findViewById(R.id.max_num_f);
        TextView facility_state_view=(TextView) findViewById(R.id.num_f);
        ImageView facility_level_img=(ImageView)findViewById(R.id.state_image_f);
        TextView facility_level_view=(TextView)findViewById(R.id.state_facility);


        //Sharedpreference 불러온다
        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        String residence = sharedPreferences.getString("residence","");//사용자 거주지 저장하는 변수

        //Intent 불러온다
        Intent intent=getIntent();
        String facility_name=intent.getExtras().getString("facility_name");
        String device_code=intent.getExtras().getString("device_code");

        //편의시설 이름 set
        facility_name_view.setText(facility_name);

        //해당 편의시설 Reference
        DatabaseReference max_park_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/"+residence+"/facility/"+device_code);

        //총 인원 수 set
        int max_facility_num=intent.getExtras().getInt("max_num");
        max_facility_view.setText(String.valueOf(max_facility_num));

        //현재 인원 수
        DatabaseReference current_park_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/"+residence+"/facility/"+device_code+"/count/");

        current_park_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int current_num=snapshot.getValue(Integer.class);
                facility_state_view.setText(String.valueOf(current_num));

                double tmp=current_num/(double)max_facility_num;

                if(tmp>0.7){//혼잡
                    facility_level_img.setImageResource(R.drawable.light3);
                    facility_level_view.setText("혼잡");
                }
                else if(0.3<=tmp&&tmp<=0.7){//보통
                    facility_level_img.setImageResource(R.drawable.light2);
                    facility_level_view.setText("보통");
                }

                else{//여유
                    facility_level_img.setImageResource(R.drawable.light1);
                    facility_level_view.setText("여유");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}