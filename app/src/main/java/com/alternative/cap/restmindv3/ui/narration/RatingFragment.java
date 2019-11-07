package com.alternative.cap.restmindv3.ui.narration;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {

    static RatingListener listener;

    private RatingBar ratingBar;
    private Button rateBtn;
    private Button cancelBtn;

    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private FirebaseUser user;

    Random random = new Random();
    int x = random.nextInt( 1000 );

    private static MediaItem item;

    public RatingFragment() {
        // Required empty public constructor
    }

    public static RatingFragment newInstance(RatingListener passingListener, MediaItem passingItem) {

        Bundle args = new Bundle();
        item = passingItem;
        listener = passingListener;
        RatingFragment fragment = new RatingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference = databaseReference.child( "users" );
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rating, container, false);
        initInstance(rootView,savedInstanceState);
        workbench(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        ratingBar = rootView.findViewById(R.id.ratingBar);
        rateBtn = rootView.findViewById(R.id.ratingRateBtn);
        cancelBtn = rootView.findViewById(R.id.ratingCancelBtn);
    }

    private void workbench(View rootView, Bundle savedInstanceState) {

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.updateRating_score(ratingBar.getRating());
                databaseReference.child("sound").child(item.target).setValue(item);
                getFragmentManager().popBackStack();
                listener.onCancel();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                listener.onCancel();
            }
        });

    }

    public interface RatingListener{
        void onCancel();
    }

}
