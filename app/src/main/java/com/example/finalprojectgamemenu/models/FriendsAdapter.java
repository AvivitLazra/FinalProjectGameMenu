package com.example.finalprojectgamemenu.models;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamemenu.R;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder>{

    private ArrayList<FriendDetails> friendsList;

    public FriendsAdapter(ArrayList<FriendDetails> friendsList){
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
        void onCardButtonClick(FriendDetails friendDetails,View cardView);
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
        textFriendName.setText(holder.itemView.getContext().getString(R.string.friends_card_name, friendsList.get(position).getFriendName()));

        //Setting button onClickListener

//        String friendUID = friendsList.get(position).getFriendUserId();
        friendRemoveBtn.setOnClickListener(v ->{
            removeFriend(position);
        });
    }

    public void removeFriend(int position){
        friendsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,friendsList.size());
    }

    @Override
    public int getItemCount() { return friendsList.size(); }

}
