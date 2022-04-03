package com.cis.rollthedice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private Button buttonRegister;
    private EditText fullname,email,password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

                mAuth.createUserWithEmailAndPassword(email1,password1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getInstance().getCurrentUser();
                                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("USERS");
                                    User dbuser= new User(fullname1,email1,password1,"0");
                                    ref.child(user.getUid()).setValue(dbuser);
                                    Toast.makeText(RegisterActivity.this,"Successfully registered",Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }else{
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                    //       Toast.LENGTH_SHORT).show();
                                    //       updateUI(null);
                                }
                            }
                        });
            }
        });
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}