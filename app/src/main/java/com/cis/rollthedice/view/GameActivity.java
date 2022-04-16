package com.cis.rollthedice.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cis.rollthedice.R;
import com.cis.rollthedice.models.Dice;
import com.cis.rollthedice.viewmodels.GameViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {
    private Button playerButton,computerButton;
    private TextView scoreText, titleText;
    private LottieAnimationView animationView, animationViewC;
    private GameViewModel gameViewModel;

    int calScore=0;
    int diceValueP=0;
    int diceValueC=0;
    int diceValue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        scoreText = findViewById(R.id.scoreTxt);
        titleText = findViewById(R.id.titleTxt);
        animationView = findViewById(R.id.animationView1);
        animationView.setVisibility(View.GONE);
        animationViewC = findViewById(R.id.animationView2);
        animationViewC.setVisibility(View.GONE);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userId=user.getUid();

        DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("USERS").child(userId);
        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    calScore= Integer.parseInt(dataSnapshot.child("score").getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        playerButton = findViewById(R.id.playerbtn);
        computerButton = findViewById(R.id.computerbtn);

        playerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animationView.playAnimation();
                animationView.setVisibility(View.VISIBLE);
                animationViewC.playAnimation();
                animationViewC.setVisibility(View.VISIBLE);
                playerButton.setVisibility(View.GONE);
                computerButton.setVisibility(View.GONE);
                scoreText.setText(" ");
                titleText.setText(" ");

                rollPlayerDice();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rollComputerDice();

                    }
                }, 1300);
                Log.d("Score", "diceValueP: " + diceValueP+" diceValueC: " + diceValueC);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playerButton.setVisibility(View.VISIBLE);
                        computerButton.setVisibility(View.VISIBLE);
                        animationView.pauseAnimation();
                        animationViewC.pauseAnimation();
                        animationView.setVisibility(View.GONE);
                        animationViewC.setVisibility(View.GONE);

                        Log.d("Score", "diceValueP: " + diceValueP+" diceValueC: " + diceValueC);

                        if(diceValueP>diceValueC){
                            calScore=calScore+10;

                            Log.d("Score", "increased to: " + calScore);
                            scoreText.setText("Score: +10");
                            titleText.setText("You Win!");

                            readRef.child("score").setValue(calScore);

                        }else  if(diceValueP==diceValueC){
                            titleText.setText("Draw!");
                            scoreText.setText(" ");
                        }else{
                            titleText.setText("You Loss!");
                            scoreText.setText(" ");
                        }
                    }
                }, 3000);

                try {

                   // Toast.makeText(GameActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(GameActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void goToLeaderboardActivity(View view) {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }

    public void goToMainMenuActivity(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
    public void rollPlayerDice(){

        gameViewModel = ViewModelProviders.of(GameActivity.this).get(GameViewModel.class);
        gameViewModel.getData(GameActivity.this).observe(GameActivity.this, new Observer<Dice>() {
            @Override
            public void onChanged(@Nullable Dice dice) {
                    Log.d("final", "dice: " + dice.getValue());
                    Log.d("final", "type: " + dice.getType());
                    diceValue = dice.getValue();
                    diceValueP = dice.getValue();

                    Log.d("Player Dice", "value: " + diceValue);

                    if(diceValue==1){
                        playerButton.setBackgroundResource(R.drawable.dice1);
                    }else if(diceValue==2){
                        playerButton.setBackgroundResource(R.drawable.dice2);
                    }else if(diceValue==3){
                        playerButton.setBackgroundResource(R.drawable.dice3);
                    }else if(diceValue==4){
                        playerButton.setBackgroundResource(R.drawable.dice4);
                    }else if(diceValue==5){
                        playerButton.setBackgroundResource(R.drawable.dice5);
                    }else if(diceValue==6){
                        playerButton.setBackgroundResource(R.drawable.dice6);
                    }
                }


        });

    }
    public void rollComputerDice(){

        gameViewModel = ViewModelProviders.of(GameActivity.this).get(GameViewModel.class);
        gameViewModel.getData(GameActivity.this).observe(GameActivity.this, new Observer<Dice>() {
            @Override
            public void onChanged(@Nullable Dice dice) {
                    Log.d("final", "dice: " + dice.getValue());
                    Log.d("final", "type: " + dice.getType());
                    diceValue = dice.getValue();
                    diceValueC = dice.getValue();

                    Log.d("Computer Dice", "value: " + diceValue);

                    if(diceValue==1){
                        computerButton.setBackgroundResource(R.drawable.dice1);
                    }else if(diceValue==2){
                        computerButton.setBackgroundResource(R.drawable.dice2);
                    }else if(diceValue==3){
                        computerButton.setBackgroundResource(R.drawable.dice3);
                    }else if(diceValue==4){
                        computerButton.setBackgroundResource(R.drawable.dice4);
                    }else if(diceValue==5){
                        computerButton.setBackgroundResource(R.drawable.dice5);
                    }else if(diceValue==6){
                        computerButton.setBackgroundResource(R.drawable.dice6);
                    }

            }
        });

    }
}