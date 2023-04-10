package com.example.happy_home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class commuDetail extends AppCompatActivity {

    String Writer, Title, Text, Time, Phone;
    String ME,MY_Phone;
    String PostKey;
    String user_residence;
    String ReplyKey;
    String  Reply, replyTime ;
    String MyAuth;

    public static Context mContext;

    ListView reply_list;

    //댓글 작성을 위한 것들
    ArrayList<Reply> Rlist=new ArrayList<>();
    EditText WriteR;
    reply_ItemAdapter replyItemAdapter;


    String method;
    //뒤로가기 눌렀을 때
    @Override
    public void onBackPressed() {

        if(method.equals("myPostList")){
            Intent intent= new Intent(getApplicationContext(), MyPostList.class);
            startActivity(intent);
        }
        else if(method.equals("myReplyList")){
            //다음 화면으로 이동
            Intent intent = new Intent(getApplicationContext(), MyReplyList.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(), commuList.class);
            startActivity(intent);
        }
    }


    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu_detail);

        mContext = this;

        TextView title = (TextView) findViewById(R.id.textviewT_C);
        TextView text = (TextView) findViewById(R.id.content_C);
        TextView time = (TextView) findViewById(R.id.time_textView);
        TextView who = (TextView)findViewById(R.id.whoPost_textView);

        WriteR = (EditText)findViewById(R.id.commu_detail_reply);

        SharedPreferences sharedPreferences = getSharedPreferences("my_info", MODE_PRIVATE);
        ME = sharedPreferences.getString("nick_name", "");//작성자가 누군지 알려줄거임 ~동~호 약간의 닉네임 개념
        user_residence = sharedPreferences.getString("residence", "");//집주소 알아낼꺼임
        MY_Phone = sharedPreferences.getString("phone_num","");
        MyAuth = sharedPreferences.getString("MyAuth","");

        Intent intent=getIntent();

        Phone = intent.getExtras().getString("Phone");
        Title = intent.getExtras().getString("Title");
        Text = intent.getExtras().getString("Text");
        Time = intent.getExtras().getString("Time");
        Writer = intent.getExtras().getString("Writer");
        PostKey = intent.getExtras().getString("PostKey");
        method = intent.getExtras().getString("How");

        title.setText(Title);
        text.setText(Text);
        time.setText(Time);
        who.setText(Writer);

        //댓글 작성 부분
        reply_list = (ListView)findViewById(R.id.comment);

        replyItemAdapter = new reply_ItemAdapter(this,Rlist);
        reply_list.setAdapter(replyItemAdapter);
        replyItemAdapter.notifyDataSetChanged();


        //댓글 보여주기 시작하고
        this.make_replyList();

        //댓글 작성 부분
        Button replyButton = (Button)findViewById(R.id.button);  //댓글 작성 버튼
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reply = WriteR.getText().toString();

                if(Reply.equals("")){
                    Toast.makeText(commuDetail.this, "댓글 내용을 입력해 주세요!", Toast.LENGTH_SHORT).show();
                }
                else{

                    replyItemAdapter.clearAllItems();
                    Rlist.clear();
                    replyItemAdapter.notifyDataSetChanged();
                    reply_list.clearAnimation();


                    long now = System.currentTimeMillis();
                    Date date = new Date(now);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd - hh:mm");
                    replyTime = sdf.format(date);

                    DatabaseReference Database = FirebaseDatabase.getInstance().getReference();

                    HashMap<String,Object> newreply = new HashMap();
                    HashMap<String, Object> myReply = new HashMap();

                    ME = sharedPreferences.getString("nick_name", "");//작성자가 누군지 알려줄거임 ~동~호 약간의 닉네임 개념
                    MY_Phone = sharedPreferences.getString("phone_num","");

                    Reply reply = new Reply( replyTime,  ME,  Reply, MY_Phone, PostKey, ReplyKey, MyAuth, user_residence,Title,Writer,Text,Phone,Time);


                    Map<String,Object> userValue = reply.toMap();

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("residence/"+user_residence+"/Community/"+PostKey+"/Reply");

                    ReplyKey = rootRef.push().getKey();
                    reply.setReplyKey(ReplyKey);

                    rootRef.child(ReplyKey).setValue(userValue);

                    reply.setReplyKey(ReplyKey);

                    Database.updateChildren(newreply);

                    DatabaseReference myReplyRef = FirebaseDatabase.getInstance().getReference(MyAuth+"/"+MY_Phone+"/Reply/Community/");
                    myReplyRef.child(ReplyKey).setValue(userValue);

                    Database.updateChildren(myReply);

                    replyItemAdapter.clearAllItems();
                    Rlist.clear();
                    replyItemAdapter.notifyDataSetChanged();

                    WriteR.setText("");
                }
            }
        });

        Button edit = (Button) findViewById(R.id.buttonR_C);
        //수정 - 글쓴이만 보이게
        if(ME.equals(Writer)){
            edit.setVisibility(Button.VISIBLE);
        }
        else{
            edit.setVisibility(Button.INVISIBLE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_intent = new Intent (getApplicationContext(),commuEdit.class);

                edit_intent.putExtra("Phone",MY_Phone);
                edit_intent.putExtra("Time",Time);
                edit_intent.putExtra("Title",Title);
                edit_intent.putExtra("Text",Text);
                edit_intent.putExtra("Writer",ME);
                edit_intent.putExtra("PostKey",PostKey);
                edit_intent.putExtra("How",method);

                startActivity(edit_intent);
            }
        });

        Button DEL = (Button)findViewById(R.id.buttonD_C);
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
                        .getReference("residence/" + user_residence + "/Community/"+PostKey).removeValue();
                Task<Void> database = FirebaseDatabase.getInstance()
                        .getReference(MyAuth + "/"+MY_Phone + "/Board/Community/"+PostKey).removeValue();


                if(method.equals("myPostList")){
                    startActivity(new Intent(commuDetail.this,MyPostList.class));
                }
                else if(method.equals("myReplyList")){
                    startActivity(new Intent(commuDetail.this,MyReplyList.class));
                }
                else{
                    startActivity(new Intent(commuDetail.this,commuList.class));
                }
            }
        });
    }

    public void make_replyList(){

        replyItemAdapter.clearAllItems();
        Rlist.clear();
        replyItemAdapter.notifyDataSetChanged();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("residence/"+user_residence+"/Community/"+PostKey+"/Reply/");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {

                    ReplyKey = ds.getKey();

                    MY_Phone = ds.child("R_Phone").getValue(String.class);
                    replyTime = ds.child("R_Time").getValue(String.class);
                    ME = ds.child("R_Writer").getValue(String.class);
                    Reply = ds.child("Reply").getValue(String.class);

                    Reply R = new Reply(replyTime,  ME,  Reply, MY_Phone, PostKey, ReplyKey, MyAuth, user_residence,Title,Writer,Text,Phone,Time);
                    Rlist.add(R);

                    replyItemAdapter.addItem(R);


                }
                replyItemAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnItems(reply_list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;

            Log.d("adapter",String.valueOf(numberOfItems));
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *(numberOfItems);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;

            Log.d("height",String.valueOf(params.height));
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }
    }
}
