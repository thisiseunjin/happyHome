package com.example.happy_home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class commuWrite extends AppCompatActivity {
    EditText title, text;   //글 제목, 글 내용
    Button upload;  //작성하기 버튼

    String ME; //작성자의 닉네임을 저장해 줄 것이다.
    String Time; //시간을 저장해 줄 변수
    String MY_Phone;

    String PostKey;

    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        Intent intent= new Intent(getApplicationContext(), commuList.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_commu);

        Intent intent = getIntent();

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        ME = sharedPreferences.getString("nick_name", "");//작성자가 누군지 알려줄거임 ~동~호
        String user_residence = sharedPreferences.getString("residence", "");//집주소 알아낼꺼임
        MY_Phone = sharedPreferences.getString("phone_num","");    //휴대전화 번호 저장하고있음

        title = (EditText) findViewById(R.id.commu_name);
        text = (EditText) findViewById(R.id.commu_contents);

        String MyAuth = sharedPreferences.getString("MyAuth","");

        upload = (Button) findViewById(R.id.commu_write);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = title.getText().toString();
                String Text = text.getText().toString();

                if (title.equals("")||title.equals(null)||title.equals("\n")) {
                    Toast.makeText(commuWrite.this, "제목을 입력해 주세요!", Toast.LENGTH_SHORT).show();
                } else if (text.equals("")||text.equals(null)||text.equals("\n")) {
                    Toast.makeText(commuWrite.this, "내용을 입력해 주세요!", Toast.LENGTH_SHORT).show();
                } else {

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM. dd. HH:mm:ss");
                    Time = sdf.format(date);

                    DatabaseReference Database = FirebaseDatabase.getInstance().getReference();



                    HashMap<String, Object> newpost = new HashMap();
                    HashMap<String,Object> myBoard = new HashMap<>();

                    Community commu = new Community(Time, Title, ME, Text,MY_Phone);

                    Map<String, Object> userValue = commu.toMap();

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("residence/"+user_residence+"/Community");

                    PostKey = rootRef.push().getKey();


                    rootRef.child(PostKey).setValue(userValue);

                    commu.setPost_Key(PostKey);

                    Database.updateChildren(newpost);


                    DatabaseReference myBoardRef = FirebaseDatabase.getInstance().getReference(MyAuth+"/"+MY_Phone+"/Board/Community/");
                    myBoardRef.child(PostKey).setValue(userValue);
                    // myBoard.put(MyAuth+"/"+MY_Phone+"/Board/Community/"+ PostKey, 1);

                    Database.updateChildren(myBoard);




                    Intent iitent = new Intent(getApplicationContext(), commuList.class);

                    startActivity(iitent);


                }
            }
        });


    }
}
