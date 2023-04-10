package com.example.happy_home;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Reply {

    public String PostKey;  //내가 어느 글에 댓글을 달았지?
    public String Time ; //작성시간
    public String Writer; //댓글의 작성자 닉네임으로 할꺼얌
    public String Reply; //댓글의 내용을 저장해 줄 것이다.
    public String Phone;
    public String ReplyKey;
    public String user_residence;
    public String MyAuth;
    public String postTitle;

    public String postWriter;
    public String postText;
    public String postPhone;
    public String postTime;



    public Reply(){}
    public Reply(String Time,  String Writer, String Reply,String Phone,String PostKey, String ReplyKey, String MyAuth,String residence,String postTitle
            , String postWriter, String postText, String postPhone, String postTime){


        this.Time = Time;
        this.Writer = Writer;
        this.Reply = Reply;
        this.Phone = Phone;
        this.PostKey = PostKey;
        this.ReplyKey = ReplyKey;
        this.MyAuth = MyAuth;
        this.user_residence = residence;

        this.postTitle = postTitle;
        this.postWriter = postWriter;
        this.postText = postText;
        this.postPhone = postPhone;
        this.postTime = postTime;



    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("ReplyKey",ReplyKey);
        result.put("R_Phone",Phone);
        result.put("R_Time",Time);
        result.put("R_Writer",Writer);
        result.put("Reply",Reply);
        result.put("PostKey",PostKey);
        result.put("PostTitle",postTitle);

        result.put("PostWriter",postWriter);
        result.put("PostText",postText);
        result.put("PostPhone",postPhone);
        result.put("PostTime",postTime);


        return result;

    }



    public String Reply_getPost_Key(){return PostKey;}
    //public void Reply_setPost_Key(String post_Key){this.PostKey = post_Key;}
    //public void setUser_residence(String residence){this.user_residence = residence;}
    public String getReplyKey(){return ReplyKey;}
    public void setReplyKey(String replyKey){this.ReplyKey = replyKey;}
    //public void setMyAuth(String Auth){this.MyAuth = Auth;}

    public void setPostTitle(String str){this.postTitle = str;}


    public String getMyAuth() {
        return MyAuth;
    }

    public String getWriter(){return Writer;}
    public String getReply(){return Reply;}
    public String getPostTitle(){


        return postTitle;

    }



}