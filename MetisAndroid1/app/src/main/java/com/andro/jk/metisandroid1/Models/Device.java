package com.andro.jk.metisandroid1.Models;


import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("dev_id")
    String dev_id;

    @SerializedName("reg_id")
    String reg_id;

    @SerializedName("name")
    String name;

    public Device(String dev_id, String reg_id, String name){
        this.dev_id = dev_id;
        this.reg_id = reg_id;
        this.name = name;
    }
}
