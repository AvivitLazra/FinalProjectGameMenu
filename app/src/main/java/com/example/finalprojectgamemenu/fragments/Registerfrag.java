package com.example.finalprojectgamemenu.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprojectgamemenu.R;
import com.example.finalprojectgamemenu.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Registerfrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registerfrag extends Fragment {
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Registerfrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment registerfrag.
     */
    // TODO: Rename and change types and number of parameters
    public static Registerfrag newInstance(String param1, String param2) {
        Registerfrag fragment = new Registerfrag();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.registerfrag, container, false);

        //Setting register button listener
        Button register=view.findViewById(R.id.register_register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) view.findViewById(R.id.register_emailAddressField)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.register_passwordField)).getText().toString();
                String passwordAuth = ((EditText) view.findViewById(R.id.register_passwordAuthField)).getText().toString();
                if(!password.equals(passwordAuth)){
                    Toast.makeText(getContext(),"Passwords do not match!",Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(),"register succeeded ",Toast.LENGTH_LONG).show();
                                    Navigation.findNavController(view).navigate(R.id.action_registerfrag_to_homefrag);
                                } else {
                                    Toast.makeText(getContext(),"register failed",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        //Setting password visibility buttons listeners
        MainActivity mainRef = (MainActivity) getActivity();
        Button showPasswordBtn = view.findViewById(R.id.register_showPasswordBtn);
        Button showPasswordAuthBtn = view.findViewById(R.id.register_showPasswordAuthBtn);

        showPasswordBtn.setOnClickListener(v -> mainRef.togglePassword(view.findViewById(R.id.register_passwordField)));
        showPasswordAuthBtn.setOnClickListener(v -> mainRef.togglePassword(view.findViewById(R.id.register_passwordAuthField)));

        return view;
    }
}