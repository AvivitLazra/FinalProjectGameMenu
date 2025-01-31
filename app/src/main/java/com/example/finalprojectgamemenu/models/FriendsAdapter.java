package com.example.finalprojectgamemenu.models;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamemenu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder>{

    private final ArrayList<PackagedUser> friendsList;

    public FriendsAdapter(ArrayList<PackagedUser> friendsList){
        this.friendsList = friendsList;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textFriendName;
        ImageButton friendRemoveBtn;


        public MyViewHolder(View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.FriendCardRes);
            textFriendName = itemView.findViewById(R.id.textFriendName);
            friendRemoveBtn = itemView.findViewById(R.id.friendRemoveBtn);
        }
    }

    public static interface onCardContentClickListener{
        void onCardButtonClick(PackagedUser friendDetails, View cardView);
    }

    @NonNull
    @Override
    public FriendsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardfriend,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView textFriendName = holder.textFriendName;
        ImageButton friendRemoveBtn = holder.friendRemoveBtn;
        //This just looks very complicated, but I'm just getting the getString from the context of the view so I can input the friend name into the placeholder string resource..
        textFriendName.setText(holder.itemView.getContext().getString(R.string.friends_card_name, friendsList.get(position).getUserName()));

        //Setting button onClickListener

//        String friendUID = friendsList.get(position).getFriendUserId();
        friendRemoveBtn.setOnClickListener(v ->{
            removeFriend(position);
        });
    }

    public void removeFriend(int position){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        PackagedUser removeUser = friendsList.get(position);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");


        Log.d("remove_friend","currentUser = " + currentUser.getUid());

        //Loop for finding the user we want to remove the friend from
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PackagedUser checkedUser = null;
                DatabaseReference removeRef = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    checkedUser = dataSnapshot.getValue(PackagedUser.class);
                    if (checkedUser.getUserId().equals(currentUser.getUid()))
                        removeRef = dataSnapshot.getRef().child("friends");
                }
                if (removeRef != null) {
                    removeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Loop for finding the friend to remove
                            PackagedUser findUser = null;
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                findUser = dataSnapshot.getValue(PackagedUser.class);
                                if (findUser != null && findUser.getUserId().equals(removeUser.getUserId())) {
                                    dataSnapshot.getRef().removeValue();
                                    Log.d("remove_user", "user: " + findUser.getUserName() + " is being removed!!!!");
                                    friendsList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, friendsList.size());
                                    return;

                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w("remove_user", "Failed to read value.", error.toException());
                        }
                    });
                }
            }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("remove_user", "Failed to read value.", error.toException());
                }

        });

    }

    public void addFriend (PackagedUser friendUser){
        int insertPosition = friendsList.size();
        friendsList.add(friendUser);
        notifyItemInserted(insertPosition);
    }

    @Override
    public int getItemCount() { return friendsList.size(); }

}
