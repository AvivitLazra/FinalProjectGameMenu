package com.example.finalprojectgamemenu.fragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalprojectgamemenu.R;
import com.example.finalprojectgamemenu.models.Message;
import com.example.finalprojectgamemenu.models.MessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chatfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chatfrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private final int MAX_SHOW_MSG = 20;
    private final int MAX_STORE_MSG = 50;
    private String gameChannel;

    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("chat_channels");
    RecyclerView recyclerView;
    MessagesAdapter adapter;

    public Chatfrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chatfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static Chatfrag newInstance(String param1, String param2) {
        Chatfrag fragment = new Chatfrag();
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
        view = inflater.inflate(R.layout.chatfrag, container, false);
        TextView chatTestTitle = view.findViewById(R.id.chat_TestTitle);

        Button sendMsgBtn = view.findViewById(R.id.chat_sendMsgBtn);

        //Getting arguments from calling fragment
        gameChannel = "";
        Bundle args = getArguments();
        if (args!=null){
            gameChannel = args.getString("gameChannel");
            chatTestTitle.setText(getString(R.string.chatTitle,gameChannel));
        }
        else{
            chatTestTitle.setText(getString(R.string.chatTitle,"NULL"));
            Log.d("channel-selection","invalid selection detected." );
        }
        Log.d("channel-selection","Channel selected: " + gameChannel);

        //Populating recyclerView..
        recyclerView = view.findViewById(R.id.chat_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String[] demoUsernames = {"David","Avivit","Johncena"};
        String[] demoMsgs = {"I love spaghetti meatballs","Avivit al a bit, avivit al a bit","AND HIS NAME IS"};
        ArrayList<Message> demoMessageList = new ArrayList<>();
        for(int i = 0; i<demoMsgs.length; i++){
            demoMessageList.add(new Message(demoUsernames[i],demoMsgs[i]));
        }

        adapter = new MessagesAdapter(demoMessageList);

        recyclerView.setAdapter(adapter);

        sendMsgBtn.setOnClickListener(v -> {
            sendMsg(view.findViewById(R.id.chat_editMsgField));
        });
        loadLatestMessages();


        return view;
    }

    public void sendMsg(@NonNull EditText msgField){
        String messageContent = msgField.getText().toString();
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        Message insertMessage = new Message(currentUser,messageContent);
        DatabaseReference gameChannelRef = myRef.child(gameChannel);
        adapter.addItem(insertMessage);
        msgField.setText("");
        gameChannelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Maintain not over MAX_STORE_MSG amount of messages per game channel on firebase.
                if(snapshot.getChildrenCount()>= MAX_STORE_MSG){
                    DataSnapshot oldestMessageSnapshot = snapshot.getChildren().iterator().next();
                    oldestMessageSnapshot.getRef().removeValue();
                }
                gameChannelRef.push().setValue(insertMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("send_msg", "Failed to send message. ", error.toException());
            }
        });
    }

    public void loadLatestMessages(){
        DatabaseReference gameChannelRef = myRef.child(gameChannel);
        //Maintaining MAX_SHOW_MSG amount of messages in the recycler view
        gameChannelRef.limitToLast(MAX_SHOW_MSG).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Message> messagesList = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Message msg = dataSnapshot.getValue(Message.class);
                    messagesList.add(msg);
                }
                adapter.updateMessages(messagesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}