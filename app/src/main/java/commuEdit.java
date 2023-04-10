package com.example.happy_home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class commuEdit extends AppCompatActivity {

    EditText title, text;
    Button edit;
    String  ME,MY_Phone;
    String PostKey, Writer, Title, Text, Time, Phone,method;

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        //게시글 상세페이지로 이동
        Intent detail_intent= new Intent(getApplicationContext(),commuDetail.class);

        detail_intent.putExtra("Phone",Phone);
        detail_intent.putExtra("Time",Time);
        detail_intent.putExtra("Title",Title);
        detail_intent.putExtra("Text",Text);
        detail_intent.putExtra("Writer",Writer);
        detail_intent.putExtra("PostKey",PostKey);
        detail_intent.putExtra("How",method);

        startActivity(detail_intent);
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu_edit);

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        ME = sharedPreferences.getString("nick_name", "");//작성자가 누군지 알려줄거임 ~동~호
        String user_residence = sharedPreferences.getString("residence", "");//집주소 알아낼꺼임
        MY_Phone = sharedPreferences.getString("phone_num","");    //작성자 휴대전화 번호 알려줄거임

        Intent intent=getIntent();
        Phone = intent.getExtras().getString("Phone");
        Title = intent.getExtras().getString("Title");
        Text = intent.getExtras().getString("Text");
        Time = intent.getExtras().getString("Time");
        Writer = intent.getExtras().getString("Writer");
        PostKey = intent.getExtras().getString("PostKey");
        method = intent.getExtras().getString("How");


        title = (EditText) findViewById(R.id.commu_title_E);
        text = (EditText) findViewById(R.id.commu_content_E);

        title.setText(Title);
        text.setText(Text);

        edit = (Button) findViewById(R.id.edit_commu);
        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String Title = title.getText().toString();
                String Text = text.getText().toString();

                if(Title.equals("")){
                    Toast.makeText(commuEdit.this,"제목을 입력해 주세요.",Toast.LENGTH_SHORT).show();
                }

                else if(Text.equals(""))   {
                    Toast.makeText(commuEdit.this,"내용을 입력해 주세요.",Toast.LENGTH_SHORT).show();
                }

                else{
                    DatabaseReference Database = FirebaseDatabase.getInstance().getReference();

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh:mm");
                    Time = sdf.format(date);

                    HashMap<String,Object> newpost=new HashMap();

                    Community Commu=new Community(Time, Title, ME, Text,MY_Phone);
                    Map<String,Object> userValue=Commu.toMap();

                   Database.child("residence/" + user_residence + "/Community/"+PostKey).setValue(userValue);
                    Database.updateChildren(newpost);


                    Intent edit_intent= new Intent(getApplicationContext(), commuList.class);

                    startActivity(edit_intent);

                }
            }
        });
    }
}
