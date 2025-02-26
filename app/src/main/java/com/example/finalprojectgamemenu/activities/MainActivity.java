package com.example.finalprojectgamemenu.activities;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectgamemenu.R;
import com.example.finalprojectgamemenu.models.PackagedUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void togglePassword(View view){
        EditText passwordField = (EditText) view;

        if (passwordField.getTransformationMethod() instanceof PasswordTransformationMethod) // Show password
            passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        else // Hide password
            passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }



    //Retrieve data from database via callback interface (since onDataChange is defined as void)
    public interface DatabaseCallback {
        void onCallback(ArrayList<PackagedUser> friendList);
    }
}