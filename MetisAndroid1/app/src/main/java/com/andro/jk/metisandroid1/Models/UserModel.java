package com.andro.jk.metisandroid1.Models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;


@Table(name = "User")
public class UserModel {

    @Expose
    @Column(name = "id")
    public int id;

    @Expose
    @Column(name = "name")
    public String username;

    @Expose
    @Column(name = "first_name")
    public String first_name;

    public UserModel(String first_name, int id, String username) {
        this.first_name = first_name;
        this.id = id;
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
