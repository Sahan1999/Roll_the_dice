package com.cis.rollthedice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.rollthedice.R;
import com.cis.rollthedice.models.User;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<User> usersList;

    public recyclerAdapter(ArrayList<User> usersList){
        this.usersList = usersList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView rankTxt;
        private TextView fullnameTxt;
        private TextView scoreTxt;

        public MyViewHolder(final View view){
            super(view);
            rankTxt = view.findViewById(R.id.textViewRank);
            fullnameTxt = view.findViewById(R.id.textViewFullname);
            scoreTxt = view.findViewById(R.id.textViewScore);
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View leaderboardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_user, parent, false);
        return new MyViewHolder(leaderboardView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String name = usersList.get(position).getFullname();
        int score = usersList.get(position).getScore();
        String rank = String.valueOf(position+1);
        holder.fullnameTxt.setText(name);
        holder.scoreTxt.setText(String.valueOf(score));
        holder.rankTxt.setText(rank);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
