package com.example.happy_home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class noticeDetail extends AppCompatActivity {


    String Writer, Title, Text, Time, Phone, PostKey;
    String ME,MY_Phone;

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        Intent intent= new Intent(getApplicationContext(), noticeList.class);
        startActivity(intent);
    }

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        Intent intent=getIntent();

        TextView title = (TextView) findViewById(R.id.textviewT_N);
        TextView text = (TextView) findViewById(R.id.content_N);
        TextView time = (TextView) findViewById(R.id.time_textView);
        TextView who = (TextView)findViewById(R.id.whoPost_textView);

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        ME = sharedPreferences.getString("nick_name", "");//작성자가 누군지 알려줄거임 ~동~호
        String user_residence = sharedPreferences.getString("residence", "");//집주소 알아낼꺼임
        MY_Phone = sharedPreferences.getString("phone_num","");

        Phone = intent.getExtras().getString("Phone");
        Title = intent.getExtras().getString("Title");
        Text = intent.getExtras().getString("Text");
        Time = intent.getExtras().getString("Time");
        Writer = intent.getExtras().getString("Writer");
        PostKey = intent.getExtras().getString("PostKey");


        title.setText(Title);
        text.setText(Text);
        time.setText(Time);
        who.setText(Writer);

        Button edit = (Button) findViewById(R.id.buttonR_N);
        //수정
        if(ME.equals(Writer)){
            edit.setVisibility(Button.VISIBLE);
        }
        else{
            edit.setVisibility(Button.INVISIBLE);
        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_intent = new Intent (getApplicationContext(),noticeEdit.class);

                edit_intent.putExtra("Phone",Phone);
                edit_intent.putExtra("Time",Time);
                edit_intent.putExtra("Title",Title);
                edit_intent.putExtra("Text",Text);
                edit_intent.putExtra("Writer",Writer);
                edit_intent.putExtra("PostKey",PostKey);

                startActivity(edit_intent);
            }
        });

        Button DEL = (Button)findViewById(R.id.buttonD_N);
        //삭제
        //글 삭제
        if(ME.equals(Writer)||ME.equals("관리자")){
            DEL.setVisibility(Button.VISIBLE);
        }
        else{
            DEL.setVisibility(Button.INVISIBLE);
        }
        DEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Task<Void> Database = FirebaseDatabase.getInstance()
                        .getReference("residence/" + user_residence + "/Notice/"+PostKey).removeValue();
                Task<Void> database = FirebaseDatabase.getInstance()
                        .getReference("admin/" + Phone + "/Board/Notice/"+PostKey).removeValue();
                startActivity(new Intent(noticeDetail.this,noticeList.class));
            }
        });
    }
}