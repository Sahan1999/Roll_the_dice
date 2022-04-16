package com.cis.rollthedice.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cis.rollthedice.R;
import com.cis.rollthedice.adapters.recyclerAdapter;
import com.cis.rollthedice.models.User;
import com.cis.rollthedice.viewmodels.LeaderboardViewModel;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private ArrayList<User> usersList;
    private RecyclerView recyclerView;
    private LeaderboardViewModel leaderboardViewModel;
    private recyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        leaderboardViewModel = new LeaderboardViewModel();

        recyclerView = findViewById(R.id.recycler1);
        usersList = new ArrayList<>();

        leaderboardViewModel = ViewModelProviders.of(LeaderboardActivity.this).get(LeaderboardViewModel.class);
        leaderboardViewModel.getData(LeaderboardActivity.this).observe(LeaderboardActivity.this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(@Nullable ArrayList<User> users) {
                setUpRecyclerView(users);
            }

        });

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setUpRecyclerView(final ArrayList<User> users){
        initRecyclerView();

        adapter = new recyclerAdapter(users);
        recyclerView.setAdapter(adapter);

    }

    public void goToMainMenuActivity(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
}