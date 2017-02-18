package com.andro.jk.metisandroid1.Models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;


@Table(name = "Worker")
public class WorkerModel {

    @Expose
    @Column(name = "user")
    public UserModel user;

    @Expose
    @Column(name = "id")
    public int id;

    @Expose
    @Column(name = "category")
    public int category;


    public WorkerModel(int category, int id, UserModel user) {
        this.category = category;
        this.id = id;
        this.user = user;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
