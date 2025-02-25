package com.example.finalprojectgamemenu.fragments;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectgamemenu.R;
import com.example.finalprojectgamemenu.models.Games;
import com.example.finalprojectgamemenu.models.GamesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Favoritesfrag extends Fragment {

    private RecyclerView recyclerView;
    private GamesAdapter adapter;
    private List<Games> favoriteGamesList;
    private DatabaseReference favoritesRef;
    private String firebaseUid;

    public Favoritesfrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favoritesfrag, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Initialize game list and adapter
        favoriteGamesList = new ArrayList<>();
        adapter = new GamesAdapter(favoriteGamesList, true); // 'true' enables delete mode in favorites list
        recyclerView.setAdapter(adapter);

        // Get the currently logged-in user's Firebase UID
        firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve the correct User ID (randomly generated key) from Firebase
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String storedUserId = userSnapshot.child("userId").getValue(String.class);

                    if (storedUserId != null && storedUserId.equals(firebaseUid)) {
                        String UserId = userSnapshot.getKey();
                        favoritesRef = usersRef.child(UserId).child("favorites");

                        // Load the user's favorite games
                        loadFavorites();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database Error: " + error.getMessage());
            }
        });

        return view;
    }

    /**
     * Loads the user's favorite games from Firebase and updates the RecyclerView.
     */
    private void loadFavorites() {
        if (favoritesRef == null) return; // Prevents crashes if reference is not initialized

        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Games> updatedList = new ArrayList<>();

                // Iterate through the data snapshot to retrieve all favorite games
                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                    Games game = gameSnapshot.getValue(Games.class);
                    if (game != null) {
                        updatedList.add(game);
                    }
                }

                // Delay updating the RecyclerView to ensure Firebase synchronization
                recyclerView.postDelayed(() -> {
                    favoriteGamesList.clear();
                    favoriteGamesList.addAll(updatedList);
                    adapter.notifyDataSetChanged(); // Refresh RecyclerView
                }, 300);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Favoritesfrag", "Database Error: " + error.getMessage());
            }
        });
    }
}
