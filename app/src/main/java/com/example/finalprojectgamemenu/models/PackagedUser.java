package com.example.finalprojectgamemenu.models;

public class PackagedUser {
    private String userName;
    private String UserId;


    public PackagedUser(String userName, String UserId){
        this.userName = userName;
        this.UserId = UserId;
    }

    public PackagedUser(){
        //required for firebase..
        this.userName= "N/A";
        this.UserId = "N/A";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

}
