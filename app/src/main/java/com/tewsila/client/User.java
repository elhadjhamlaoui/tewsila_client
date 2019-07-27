package com.tewsila.client;

public class User {

    String taxiid,name,sex,age,email, username, password,kind,model,pickup,destination,time,taxiname,driverphone,phone;
    int id,rating;

    public User(String email,String kind,String phone,int id,String name, String age, String username, String password,String model,String taxiname,String taxiid,String pickup,String destination,String time,String driverphone,int rating,String sex) {
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
        this.id=id;
        this.rating=rating;
        this.email=email;
        this.phone=phone;
        this.kind=kind;
        this.model=model;
        this.sex=sex;

        this.pickup = pickup;
        this.destination=destination;
        this.taxiid=taxiid;
        this.taxiname=taxiname;
        this.time=time;
        this.driverphone=driverphone;

    }
}
