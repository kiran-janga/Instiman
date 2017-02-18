package com.andro.jk.metisandroid1.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;


@Table(name = "History")
public class HistoryModel implements Serializable{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("category")
    @Expose
    private String area;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private Date timestamp;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("assigned")
    @Expose
    private int assigned;
    @SerializedName("priority")
    @Expose
    private int priority;
    @SerializedName("assigned_to")
    @Expose
    private String assignedTo;
    @SerializedName("assigned_by")
    @Expose
    private String assignedBy;

    @SerializedName("number")
    @Expose
    private String number;

    public HistoryModel() {
    }


    public HistoryModel(String area, int assigned, String assignedBy, String assignedTo, String comment, String description, int id, String image, String location, String number, int priority, String status, Date timestamp, String title, String user) {
        this.area = area;
        this.assigned = assigned;
        this.assignedBy = assignedBy;
        this.assignedTo = assignedTo;
        this.comment = comment;
        this.description = description;
        this.id = id;
        this.image = image;
        this.location = location;
        this.number = number;
        this.priority = priority;
        this.status = status;
        this.timestamp = timestamp;
        this.title = title;
        this.user = user;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getAssigned() {
        return assigned;
    }

    public void setAssigned(int assigned) {
        this.assigned = assigned;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
