package com.example.finalprojectgamemenu.fragments;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    private DatabaseReference usersRef;
    private String firebaseUid;

    public Favoritesfrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favoritesfrag, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        favoriteGamesList = new ArrayList<>();
        adapter = new GamesAdapter(favoriteGamesList, true); // ✅ שולחים true כדי לאפשר מחיקה ברשימת המועדפים
        recyclerView.setAdapter(adapter);

        firebaseUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // מחפש את ה-Random ID של המשתמש המחובר
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String storedUserId = userSnapshot.child("userId").getValue(String.class);

                    if (storedUserId != null && storedUserId.equals(firebaseUid)) {
                        String randomUserId = userSnapshot.getKey();
                        DatabaseReference favoritesRef = usersRef.child(randomUserId).child("favorites");

                        favoritesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                favoriteGamesList.clear();
                                for (DataSnapshot gameSnapshot : snapshot.getChildren()) {
                                    Games game = gameSnapshot.getValue(Games.class);
                                    if (game != null) {
                                        favoriteGamesList.add(game);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Favoritesfrag", "Database Error: " + error.getMessage());
                            }
                        });
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
}
