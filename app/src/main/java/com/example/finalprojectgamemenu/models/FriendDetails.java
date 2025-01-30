package com.example.finalprojectgamemenu.models;

public class FriendDetails {
    private String friendName;
    private String friendUserId;

    public FriendDetails(String friendName, String friendUserId) {
        this.friendName = friendName;
        this.friendUserId = friendUserId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }
}
