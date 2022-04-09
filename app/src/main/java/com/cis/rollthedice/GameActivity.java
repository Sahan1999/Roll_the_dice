package com.cis.rollthedice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {
    Button playerButton,computerButton;
    TextView scoreText, titleText;
    LottieAnimationView animationView, animationViewC;

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
                rollComputerDice();

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
                }, 2200);

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
        String url = "http://roll.diceapi.com/json/d6";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VolleyResponse", "response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray resultValue = jsonObject.getJSONArray("dice");
                    String computerMove = resultValue.optString(0);

                    Gson gson = new Gson();
                    Result result = gson.fromJson(computerMove, Result.class);
                    //Log.d("Final", "value: " + result.getValue());
                    diceValue = result.getValue();
                    diceValueP = result.getValue();

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        });
        requestQueue.add(stringRequest);

    }
    public void rollComputerDice(){
        String url = "http://roll.diceapi.com/json/d6";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VolleyResponse", "response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray resultValue = jsonObject.getJSONArray("dice");
                    String computerMove = resultValue.optString(0);

                    Gson gson = new Gson();
                    Result result = gson.fromJson(computerMove, Result.class);
                    //Log.d("Final", "value: " + result.getValue());
                    diceValue = result.getValue();
                    diceValueC = result.getValue();

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        });
        requestQueue.add(stringRequest);

    }
}