package com.cis.rollthedice.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cis.rollthedice.R;
import com.cis.rollthedice.viewmodels.RegisterViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private Button buttonRegister;
    private EditText fullname,email,password;
    private FirebaseAuth mAuth;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = new RegisterViewModel();

        fullname = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        mAuth=FirebaseAuth.getInstance();

        buttonRegister = findViewById(R.id.button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String fullname1 = fullname.getText().toString();
                String email1 = email.getText().toString();
                String password1 = password.getText().toString();

                registerViewModel.initVars(RegisterActivity.this);
                registerViewModel.setLoginName(fullname1);
                registerViewModel.setLoginEmail(email1);
                registerViewModel.setLoginPassword(password1);

                registerViewModel.performRegister();
            }

        });
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}