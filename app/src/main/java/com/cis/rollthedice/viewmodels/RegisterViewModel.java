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
import com.cis.rollthedice.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private Context context;
    private DatabaseReference ref;

    public MutableLiveData<String> registerEmail = new MutableLiveData<>();
    public MutableLiveData<String> registerPassword = new MutableLiveData<>();
    public MutableLiveData<String> registerName = new MutableLiveData<>();

    public void initVars(Context context) {
        mAuth = FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference("USERS");

        this.context = context;
    }

    public void setLoginEmail(String email) {
        registerEmail.setValue(email);
    }

    public void setLoginPassword(String password) {
        registerPassword.setValue(password);
    }

    public void setLoginName(String name) {
        registerName.setValue(name);
    }

    public void performRegister() {

        if (TextUtils.isEmpty(registerName.getValue())) {
            Toast.makeText(context, "Enter your full name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(registerEmail.getValue())) {
            Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(registerPassword.getValue())) {
            Toast.makeText(context, "Enter your password", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(registerEmail.getValue(),registerPassword.getValue())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getInstance().getCurrentUser();

                            User dbuser= new User(registerName.getValue(),registerEmail.getValue(),registerPassword.getValue(),0);
                            ref.child(user.getUid()).setValue(dbuser);

                            Toast.makeText(context,"Successfully registered",Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, LoginActivity.class));
                            ((Activity) context).finish();
                        }else{
                            Toast.makeText(context, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}