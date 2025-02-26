package com.example.finalprojectgamemenu.models;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;


import androidx.navigation.Navigation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamemenu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.MyViewHolder> {

    private final List<Games> gameList;

    private final boolean isFavoritesMode; // determines rather the favorites fragment or explorer fragment has created this adapter.

    public GamesAdapter(List<Games> gameList, boolean isFavoritesMode) {
        this.gameList = gameList;
        this.isFavoritesMode = isFavoritesMode;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout gameContainer;
        public TextView gameName;
        public ImageView gameImage;
        public ImageButton chatBtn;
        public Button heartBtn;

        public MyViewHolder(View view) {
            super(view);
            gameName = view.findViewById(R.id.textGameName);
            gameImage = view.findViewById(R.id.gameImage);
            chatBtn = view.findViewById(R.id.chatBtn);
            heartBtn = view.findViewById(R.id.heartBtn);
            gameContainer = view.findViewById(R.id.gameContainer);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardgame, parent, false);
        return new MyViewHolder(itemView);
    }@Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Games game = gameList.get(position);
        //Games game = gameList.get(holder.getAdapterPosition());
        String firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();;

        // Setting card text and image resource
        holder.gameName.setText(game.getgName());
        holder.gameImage.setImageResource(game.getgImage());
        //holder.gameContainer.setBackgroundResource(game.getgImage());

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        //Loop for finding target user
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String storedUserId = userSnapshot.child("userId").getValue(String.class);
                    if (storedUserId != null && storedUserId.equals(firebaseUid)) { // Hit - found our user in the database
                        String correctUserId = userSnapshot.getKey();
                        DatabaseReference favoritesRef = usersRef.child(correctUserId).child("favorites").child(game.getgName());

                        // Updating each game heart image depending if it's added to the user favorites list.
                        favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                holder.heartBtn.setBackgroundResource( snapshot.exists()?
                                        R.drawable.heartlogofull:
                                        R.drawable.heartlogoempty);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("GamesAdapter", "error in checking favorites: " + error.getMessage());
                            }
                        });

                        holder.heartBtn.setOnClickListener(v -> {
                            int adapterPosition = holder.getAdapterPosition();
                            if (adapterPosition == RecyclerView.NO_POSITION) return;

                            favoritesRef.get().addOnCompleteListener(task -> {

                                if (task.isSuccessful() && task.getResult().exists()) {
                                    // If the game exists - remove it.
                                    favoritesRef.removeValue().addOnSuccessListener(aVoid -> {
                                        if (isFavoritesMode) {
                                            if (adapterPosition >= 0 && adapterPosition < gameList.size()) {
                                                gameList.remove(adapterPosition);
                                                notifyItemRemoved(adapterPosition);
                                            }
                                        }
                                        holder.heartBtn.setBackgroundResource(R.drawable.heartlogoempty);
                                        Log.d("GamesAdapter", "Game removed from favorites: " + game.getgName());
                                    }).addOnFailureListener(e -> Log.e("GamesAdapter", "Error in removing a game from favorites: ", e));
                                } else {
                                    // If the game doesn't exist - add it.
                                    favoritesRef.setValue(game).addOnSuccessListener(aVoid -> {
                                        holder.heartBtn.setBackgroundResource(R.drawable.heartlogofull);
                                        Log.d("GamesAdapter", "Game added to favorites: " + game.getgName());
                                    }).addOnFailureListener(e -> Log.e("GamesAdapter", "Error in adding a game to favorites: ", e));
                                }
                            });
                        });

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "error in database reading: " + error.getMessage());
            }
        });

        holder.chatBtn.setOnClickListener(v -> {
            Bundle argsBundle = new Bundle();
            argsBundle.putString("gameChannel",game.getgName());

            Navigation.findNavController(holder.itemView).navigate(isFavoritesMode?
                            R.id.action_favoritesfrag_to_chatfrag:
                            R.id.action_exploreGamesfrag_to_chatfrag, argsBundle);

        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}