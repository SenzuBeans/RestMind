package com.alternative.cap.restmindv3.ui.step;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.alternative.cap.restmindv3.util.StepLogItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class StepShowFragment extends Fragment
        implements StepShowAdapter.StepShowListener {

    private static StepListener listener;
    private static ArrayList<MediaItem> dataList;
    private static String header;
    private static Context cons;
    private static int current;

    private TextView stepHeader;
    private RecyclerView stepShowRecyclerView;
    private StepShowAdapter adapter;

    private LinearLayout stepShowLayout;
    private FrameLayout stepShowContentContainer;

    private FirebaseUser user;
    private FirebaseDatabase database;

    private ValueEventListener valueEventListener;

    public StepShowFragment() {
    }

    public static StepShowFragment newInstance(String passingHeader, ArrayList<MediaItem> passingDataList, Context context, StepListener passingListener) {

        Bundle args = new Bundle();

        listener = passingListener;
        dataList = passingDataList;
        cons = context;
        header = passingHeader;
        current = 0;

        StepShowFragment fragment = new StepShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_show, container, false);
        initInstance(rootView, savedInstanceState);
        workplace(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        stepShowRecyclerView = rootView.findViewById(R.id.stepShowRecyclerView);
        stepHeader = rootView.findViewById(R.id.stepHeader);

        stepShowLayout = rootView.findViewById(R.id.stepShowLayout);
        stepShowContentContainer = rootView.findViewById(R.id.stepShowContentContainer);

        Random random = new Random();
        int x = random.nextInt(1000);
        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                UserDetails userDetails = dataSnapshot.child("users").child(user.getUid()).getValue(UserDetails.class);


                for (DataSnapshot ds : dataSnapshot.child(user.getUid()).child("step_log").getChildren()) {
                    StepLogItem item = ds.getValue(StepLogItem.class);

                    if (item.stepId.equals(header)) {
                        current = Integer.parseInt(item.stepCount);
                        break;
                    }
                }

                adapter = new StepShowAdapter(dataList, current, getContext(), StepShowFragment.this::onClickedItem);
                Log.d("dodo", "onDataChange: ssss :" + current);

                stepShowRecyclerView.setLayoutManager(new LinearLayoutManager(cons));
                stepShowRecyclerView.setAdapter(adapter);
                database.getReference().removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void workplace(View rootView, Bundle savedInstanceState) {
        database.getReference().child("users").addValueEventListener(valueEventListener);

        rootView.findViewById(R.id.stepShowBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        stepHeader.setText(header);

    }

    @Override
    public void onDestroy() {
        listener.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClickedItem(int passingCurrentPlay, int passingCurrentStep) {
            stepShowLayout.setVisibility(View.GONE);
            stepShowContentContainer.setVisibility(View.VISIBLE);

            getChildFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.stepShowContentContainer, StepPlayerFragment.newInstance(header, dataList, passingCurrentPlay
                            , passingCurrentStep, getContext(), new StepPlayerFragment.StepListener() {
                                @Override
                                public void onDestroy() {
                                    stepShowLayout.setVisibility(View.VISIBLE);
                                    stepShowContentContainer.setVisibility(View.GONE);
                                }
                            }))
                    .commit();
    }

    public interface StepListener {
        void onDestroy();
    }
}
