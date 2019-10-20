package com.alternative.cap.restmindv3.ui.setting.sub_setting.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class RankingActivity extends AppCompatActivity {

    private RecyclerView rankingRecyclerView;
    private RankingAdapter adapter;

    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private FirebaseUser user;


    Random random = new Random();
    int x = random.nextInt( 1000 );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        initInstance(savedInstanceState);
        workbench(savedInstanceState);
    }

    private void initInstance(Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference = databaseReference.child( "users" );
        user = FirebaseAuth.getInstance().getCurrentUser();

        rankingRecyclerView = findViewById(R.id.rankingRecyclerView);
        rankingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void workbench(Bundle savedInstanceState) {
        reference.child( user.getUid() ).child( "temp_steam" ).setValue( x );
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails;
                ArrayList<UserDetails> userDetailsList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    userDetails = ds.getValue(UserDetails.class);
                    userDetailsList.add(userDetails);
                }

                Collections.sort(userDetailsList, UserDetails.userDetailsComparator);

                Collections.reverse(userDetailsList);

                if (adapter == null){
                    adapter = new RankingAdapter(userDetailsList);
                }else{
                    adapter.notifyDataSetChanged();
                }

                rankingRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
