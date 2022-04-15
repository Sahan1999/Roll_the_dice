package com.cis.rollthedice.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cis.rollthedice.adapters.recyclerAdapter;
import com.cis.rollthedice.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardViewModel extends ViewModel {

    private Context context;
    private DatabaseReference ref;
    private ArrayList<User> usersList = new ArrayList<>();
    private MutableLiveData<ArrayList<User>> leaderboardList;

    public LiveData<ArrayList<User>> getData(Context context){
        if(leaderboardList == null) {
            leaderboardList = new MutableLiveData<ArrayList<User>>();
            loadLeaderboard(context);
        }
        return leaderboardList;
    }


    public void loadLeaderboard(Context context) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("USERS");

        ref.orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    //Toast.makeText(context,postSnapshot.child("score").getValue().toString(),Toast.LENGTH_SHORT).show();
                    Log.d("Firebase", "score: " + postSnapshot.child("score").getValue().toString());

                    User user = new User(postSnapshot.child("fullname").getValue().toString(),postSnapshot.child("email").getValue().toString(),postSnapshot.child("password").getValue().toString(), Integer.parseInt(postSnapshot.child("score").getValue().toString()) );
                    usersList.add(user);
                }
                Collections.reverse(usersList);
                Log.d("Array", "data: " + usersList.toString());

                leaderboardList.setValue(usersList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }

}