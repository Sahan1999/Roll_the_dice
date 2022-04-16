package com.cis.rollthedice.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cis.rollthedice.view.LoginActivity;
import com.cis.rollthedice.view.MainMenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private Context context;

    public MutableLiveData<String> loginEmail = new MutableLiveData<>();
    public MutableLiveData<String> loginPassword = new MutableLiveData<>();

    public void initVars(Context context) {
        mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void setLoginEmail(String email) {
        loginEmail.setValue(email);
    }

    public void setLoginPassword(String password) {
        loginPassword.setValue(password);
    }

    public void performLogin() {
        if (TextUtils.isEmpty(loginEmail.getValue())) {
            Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(loginPassword.getValue())) {
            Toast.makeText(context, "Enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(loginEmail.getValue(), loginPassword.getValue())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            context.startActivity(new Intent(context, MainMenuActivity.class));
                            ((Activity) context).finish();
                        } else {
                            Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void checkForUser() {
        if(mAuth.getCurrentUser() != null){
            context.startActivity(new Intent(context,MainMenuActivity.class));
            ((Activity)context).finish();
        }else{
            context.startActivity(new Intent(context,LoginActivity.class));
            ((Activity)context).finish();
        }
    }

}