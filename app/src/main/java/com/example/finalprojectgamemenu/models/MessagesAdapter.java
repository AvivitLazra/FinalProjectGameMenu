package com.example.finalprojectgamemenu.models;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamemenu.R;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private final List<Message> MsgsList;

    public MessagesAdapter(List<Message> MsgsList) {
        this.MsgsList = MsgsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msgTimestamp;
        public TextView msgUsername;
        public TextView msgContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            msgTimestamp = itemView.findViewById(R.id.messageCard_TextTimestamp);
            msgUsername = itemView.findViewById(R.id.messageCard_TextUsername);
            msgContent = itemView.findViewById(R.id.messageCard_TextMessageContent);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardmessage, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message = MsgsList.get(position);
        holder.msgTimestamp.setText(message.getTimestamp().split(" ")[1]);
        holder.msgUsername.setText(message.getUserName()+":");
        holder.msgContent.setText(message.getMessageContent());

    }

    @Override
    public int getItemCount() {
        return MsgsList.size();
    }
    public void addItem(Message addMsg){
        int insertPosition = MsgsList.size();
        MsgsList.add(addMsg);
        notifyItemInserted(insertPosition);

    }

    public void updateMessages(List<Message> newMessages) {
        this.MsgsList.clear();
        this.MsgsList.addAll(newMessages);
        notifyDataSetChanged(); // Refresh the RecyclerView
    }
}

