package com.example.finalprojectgamemenu.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalprojectgamemenu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Homefrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Homefrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Homefrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Homefrag.
     */
    // TODO: Rename and change types and number of parameters
    public static Homefrag newInstance(String param1, String param2) {
        Homefrag fragment = new Homefrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.homefrag, container, false);

        //Setting user greeting message
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        TextView userGreeting = view.findViewById(R.id.home_userGreeting);
        userGreeting.setText(getString(R.string.home_user_greeting, currentUser.getEmail().split("@")[0]));

        //Retrieve buttons from layout
        Button favoritesBtn = view.findViewById(R.id.favoriteBtn);
        Button exploreBtn = view.findViewById(R.id.exploreBtn);
        Button friendsBtn = view.findViewById(R.id.friendsBtn);

        //Setting navigation listeners
        favoritesBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_homefrag_to_favoritesfrag));
        exploreBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_homefrag_to_exploreGamesfrag));
        friendsBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_homefrag_to_friendsfrag));

        return view;
    }
}