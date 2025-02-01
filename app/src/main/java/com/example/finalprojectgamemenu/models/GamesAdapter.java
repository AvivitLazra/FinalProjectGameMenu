package com.example.finalprojectgamemenu.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamemenu.R;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private List<Games> gameList;

    public GamesAdapter(List<Games> gameList) {
        this.gameList = gameList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView gameName;
        public ImageView gameImage;
        public ImageButton chatBtn;

        public MyViewHolder(View view) {
            super(view);
            gameName = view.findViewById(R.id.textGameName);
            gameImage = view.findViewById(R.id.gameImage);
            chatBtn = view.findViewById(R.id.chatBtn);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardgame, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Games game = gameList.get(position);
        holder.gameName.setText(game.getgName());
        holder.gameImage.setImageResource(game.getgImage());

        holder.chatBtn.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}
