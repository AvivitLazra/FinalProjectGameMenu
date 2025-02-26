package com.example.finalprojectgamemenu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                R.raw.game1, R.raw.game2, R.raw.game3, R.raw.game4, R.raw.game5,
                R.raw.game6, R.raw.game7, R.raw.game8, R.raw.game9, R.raw.game10,
                R.raw.game11, R.raw.game12, R.raw.game13, R.raw.game14, R.raw.game15,
                R.raw.game16, R.raw.game17, R.raw.game18, R.raw.game19, R.raw.game20
        };

        gameList = new ArrayList<>();
        for (int i = 0; i < gameNames.length; i++) {
            gameList.add(new Games(gameImages[i], gameNames[i]));
        }

        adapter = new GamesAdapter(gameList, false);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
