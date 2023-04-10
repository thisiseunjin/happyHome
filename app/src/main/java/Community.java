package com.example.happy_home;

import java.io.Serializable;

public class Community extends Board implements Serializable {

    Community(String Time, String Title, String Writer, String Text,String Phone){
        super(Time,Title,Writer,Text,Phone);

        this.Time = Time;
        this.Title = Title;
        this.Writer = Writer;
        this.Text = Text;
        this.Phone = Phone;
    }

    public String getTitle(){ return Title;}
    public String getText(){return Text;}
    public String getWriter(){return Writer;}
    public String getTime(){return Time;}
    public String getPhone(){return Phone;}
    public String getPost_Key(){return PostKey;}
    public void setPost_Key(String post_Key){this.PostKey = post_Key;}

}