package com.cis.rollthedice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cis.rollthedice.viewmodels.GameViewModel;
import com.cis.rollthedice.viewmodels.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginViewModel = new LoginViewModel();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginViewModel.initVars(MainActivity.this);
                loginViewModel.checkForUser();
            }
        }, 1000);
    }
}


