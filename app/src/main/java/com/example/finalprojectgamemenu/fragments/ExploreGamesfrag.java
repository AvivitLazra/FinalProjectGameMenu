package com.example.finalprojectgamemenu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectgamemenu.R;
import com.example.finalprojectgamemenu.models.Games;
import com.example.finalprojectgamemenu.models.GamesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ExploreGamesfrag extends Fragment {

    private RecyclerView recyclerView;
    private GamesAdapter adapter;
    private List<Games> gameList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore_gamesfrag, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewGames);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String[] gameNames = {
                "Avivit Al A BIT", "Sports World", "Cheese Bites", "Witch Craft", "Cloud Soccer",
                "Space Racer", "Galaxy Frogs", "Vikings Go Berzerk", "Candy Crushers", "Sunday Party",
                "Royal Palace", "Galaxy Frogs", "Racing Club", "Neon Noir", "Pumpkin Smash",
                "Double Dragons", "Reptoids", "Power Plant", "Trolls Bridge", "Super Heroes"
        };

        int[] gameImages = {
                R.drawable.game1, R.drawable.game2, R.drawable.game3, R.drawable.game4, R.drawable.game5,
                R.drawable.game6, R.drawable.game7, R.drawable.game8, R.drawable.game9, R.drawable.game10,
                R.drawable.game11, R.drawable.game12, R.drawable.game13, R.drawable.game14, R.drawable.game15,
                R.drawable.game16, R.drawable.game17, R.drawable.game18, R.drawable.game19, R.drawable.game20
        };

        gameList = new ArrayList<>();
        for (int i = 0; i < gameNames.length; i++) {
            gameList.add(new Games(gameImages[i], gameNames[i]));
        }

        adapter = new GamesAdapter(gameList, false); // ✅ שליחת false כדי לציין שזה מסך המשחקים הרגילים
        recyclerView.setAdapter(adapter);

        return view;
    }
}
