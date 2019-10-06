package com.alternative.cap.restmindv3.ui.step;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alternative.cap.restmindv3.R;
;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.alternative.cap.restmindv3.util.StepListItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class StepListFragment extends Fragment implements StepListAdapter.StepListAdapterListener {

    private ArrayList<ArrayList<MediaItem>> dataList;
    private ArrayList<String[]> headerList;
    private ArrayList<MediaItem> tempMediaList;

    private StepListAdapter stepListAdapter;
    private RecyclerView stepListRecyclerView;
    private FrameLayout stepPlayerContentContainer;

    private FirebaseUser user;
    private FirebaseDatabase database;

    private ValueEventListener valueEventListener;

    public StepListFragment() {
    }

    public static StepListFragment newInstance() {

        Bundle args = new Bundle();

        StepListFragment fragment = new StepListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        dataList = new ArrayList<ArrayList<MediaItem>>();
        headerList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);
        initInstance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        hideNavigationBar();
        stepListRecyclerView = rootView.findViewById(R.id.stepListRecyclerView);
        stepPlayerContentContainer = rootView.findViewById(R.id.stepPlayerContentContainer);


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("step").getChildren()) {
                    StepListItem item = ds.getValue(StepListItem.class);
                    headerList.add(new String[]{ds.getKey(), item.artist, item.image_link});

                    ArrayList<String> mediaId = new ArrayList(Arrays.asList(item.rawId.split(",")));
                    tempMediaList = new ArrayList<>();

                    for (String s : mediaId) {
                        tempMediaList.add(dataSnapshot.child("sound").child(s).getValue(MediaItem.class));
//                        Log.d("dodo", "onItemClicked: "+ tempMediaList.get(tempMediaList.size()-1).name);
                    }
                    dataList.add(tempMediaList);
                    Log.d("dodo", "onItemClicked: "+ dataList.get(0).get(0).name);

                }
                doStuff();
                database.getReference().removeEventListener(valueEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    private void workbench(View rootView, Bundle savedInstanceState) {
        getStep();

        rootView.findViewById( R.id.stepBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        } );
    }

    @Override
    public void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    private void hideNavigationBar() {
        this.getActivity().getWindow().getDecorView()
                .setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public void doStuff() {

        stepListAdapter = new StepListAdapter(StepListFragment.this, dataList, headerList, getContext());
        stepListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stepListRecyclerView.setAdapter(stepListAdapter);
    }

    public void getStep() {
        Random random = new Random();
        int x = random.nextInt(1000);
        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);

        database.getReference().addValueEventListener(valueEventListener);


    }

    @Override
    public void onItemClicked(String passingHeader, ArrayList<MediaItem> passingDataList) {
        stepListRecyclerView.setVisibility(View.GONE);
        stepPlayerContentContainer.setVisibility(View.VISIBLE);

        getChildFragmentManager().beginTransaction()
                .add(R.id.stepPlayerContentContainer, StepShowFragment.newInstance(passingHeader, passingDataList, getContext(), new StepShowFragment.StepListener() {
                    @Override
                    public void onDestory() {
                        stepListRecyclerView.setVisibility(View.VISIBLE);
                        stepPlayerContentContainer.setVisibility(View.GONE);
                    }
                }))
                .addToBackStack(null)
                .commit();
    }
}