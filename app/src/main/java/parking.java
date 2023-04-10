package com.example.happy_home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class parking extends AppCompatActivity {

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= new Intent(getApplicationContext(), parkList.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        TextView park_name_view=(TextView) findViewById(R.id.name_parking);
        TextView max_park_view=(TextView) findViewById(R.id.max_num_p);
        TextView park_state_view=(TextView) findViewById(R.id.state_p);
        TextView can_park_view=(TextView) findViewById(R.id.num_p);


        //Sharedpreference 불러온다
        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        String residence = sharedPreferences.getString("residence","");//사용자 거주지 저장하는 변수

        //Intent 불러온다
        Intent intent=getIntent();
        String facility_name=intent.getExtras().getString("park_name");
        String device_code=intent.getExtras().getString("device_code");

        park_name_view.setText(facility_name);


        //총 주차공간 수 set
        int max_park_num=intent.getExtras().getInt("max_num");
        max_park_view.setText(String.valueOf(max_park_num));


        //현재 차량 수
        DatabaseReference current_park_Ref = FirebaseDatabase.getInstance()
                .getReference("residence/"+residence+"/parking/"+device_code+"/count/");

        current_park_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int current_cars = snapshot.getValue(int.class);
                park_state_view.setText(String.valueOf(current_cars));

                can_park_view.setText(String.valueOf(max_park_num-current_cars));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}