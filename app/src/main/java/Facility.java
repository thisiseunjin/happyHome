package com.example.happy_home;

public class Facility implements Comparable<Facility>{

    private String name;//시설 이름
    private String device_code;//장치코드
    private int max_num;//총 공간
    private int current_num;//현재 인원 수
    private int level;//level

    Facility(String name,String device_code,int max_num,int current_num,int level){

        this.name=name;
        this.device_code=device_code;
        this.max_num=max_num;
        this.current_num=current_num;
        this.level=level;
    }

    public String getName() {
        return name;
    }

    public String getDevice_code() {
        return device_code;
    }

    public int getMax_num() {
        return max_num;
    }

    public int getCurrent_num() {
        return current_num;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int compareTo(Facility facility) {

        //오름차순
        return name.compareTo(facility.getName());
    }
}
