package com.cis.rollthedice.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.cis.rollthedice.R;
import com.cis.rollthedice.viewmodels.LoginViewModel;

public class MainActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

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


