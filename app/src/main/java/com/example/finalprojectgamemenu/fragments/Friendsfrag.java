package com.example.finalprojectgamemenu.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectgamemenu.R;
import com.example.finalprojectgamemenu.models.PackagedUser;
import com.example.finalprojectgamemenu.models.FriendsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Friendsfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Friendsfrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public Friendsfrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Friendsfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static Friendsfrag newInstance(String param1, String param2) {
        Friendsfrag fragment = new Friendsfrag();
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
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.friendsfrag, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.friends_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ArrayList<PackagedUser> mockData = new ArrayList<>();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String[] mockFriendsNames = {"John","Elizabeth", "Tim","Avivit","David"};
        String[] mockFriendsIDs = {"JohnID","ElizabethID", "TimID","AvivitID","DavidID"};

        for(int i = 0; i< mockFriendsNames.length; i++){
            Log.d("ItemCollection","Collecting items...");
            mockData.add(new PackagedUser(
                    mockFriendsNames[i],
                    mockFriendsIDs[i]
            ));
        }
        // Since we implement the listener, we set this as the listener of the adapter.
        FriendsAdapter adapter = new FriendsAdapter(mockData);
        recyclerView.setAdapter(adapter);

        // Adding a listener to the Add Friend button
        Button addFriendBtn = view.findViewById(R.id.friends_addFriendBtn);
        addFriendBtn.setOnClickListener(v ->{
            //Creating a pop up window to grab the user's input friend name
            LayoutInflater popUpInflater = LayoutInflater.from(v.getContext());
            View dialogView = popUpInflater.inflate(R.layout.add_friend_popup,null);
            EditText inputField = dialogView.findViewById(R.id.add_friend_popup_Input);


            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Add Friend")
                    .setView(dialogView)
                    .setPositiveButton("Add", (dialog, which)-> {
                      String userInput = inputField.getText().toString();
                      if(!userInput.isEmpty()){
                          Log.d("UserInput","User Input: " + userInput);
                          addUser(userInput);
                      }
                      else{
                          Toast.makeText(v.getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                      }
                    }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog= builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.show();

        });


        return view;
    }

    public void addUser(String userName){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        DatabaseReference usersRef = database.getReference("users");


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DatabaseReference addRef = null;
                PackagedUser checkedUser;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    checkedUser = snapshot.getValue(PackagedUser.class);
                    if(checkedUser.getUserId().equals(currentUser.getUid()))
                        addRef = snapshot.getRef().child("friends");
                    else
                        Log.d("add_user","1 add reference is at null!");

                }

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    checkedUser = snapshot.getValue(PackagedUser.class);
                    Log.d("add_user", "Checking user: " + checkedUser.getUserName() + " with ID: " + checkedUser.getUserId());

                    if(checkedUser.getUserName().equals(userName) && addRef!=null){
                        addRef.push().setValue(checkedUser);
                        Toast.makeText(getContext(), "Friend: " + userName + " added successfully.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (addRef == null){
                        Log.d("add_user","2 add reference is at null!");
                    }
                    if(checkedUser.getUserName().equals(userName))
                        Log.d("add_user", "User not matched with: userSearch = " + userName + " current user = " + checkedUser.getUserName());

                }
                Log.d("add_user","User not found in database.");
                Toast.makeText(getContext(),"User not found in database",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("add_user", "Failed to read value.", error.toException());
            }
        });

    }

}