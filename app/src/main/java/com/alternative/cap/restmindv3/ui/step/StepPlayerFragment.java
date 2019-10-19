package com.alternative.cap.restmindv3.ui.step;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.MediaItem;
import com.alternative.cap.restmindv3.util.StepLogItem;
import com.alternative.cap.restmindv3.util.UserDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Random;

public class StepPlayerFragment extends Fragment {

    private CircularImageView stepPlayerCover;
    private Animation anim;
    private SimpleExoPlayer stepPlayer;
    private MediaSource stepMediaSource;
    private ConcatenatingMediaSource stepConcatenatingMediaSource;
    private DefaultDataSourceFactory stepDataSourceFactory;
    private PlayerControlView stepController;

    private TextView stepPlayerName;
    private TextView stepPlayerArtist;
    private TextView stepPlayerHeader;

    private static StepListener listener;
    private static ArrayList<MediaItem> dataList;
    private static String header;
    private static Context cons;
    private static int currentPlay = 0;
    private static int currentStep = 0;

    private boolean isAnimationPlaying = false;
    private View exoNextBtn;

    private FirebaseUser user;
    private FirebaseDatabase database;

    public StepPlayerFragment() {
    }

    public static StepPlayerFragment newInstance(String passingHeader
            , ArrayList<MediaItem> passingDataList, int passingCurrentPlay
            , int passingCurrentStep, Context context, StepListener passingListener) {

        Bundle args = new Bundle();

        listener = passingListener;
        dataList = passingDataList;
        header = passingHeader;
        currentPlay = passingCurrentPlay;
        currentStep = passingCurrentStep;
        cons = context;

        StepPlayerFragment fragment = new StepPlayerFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_step_player, container, false);
        initInstance(rootView, savedInstanceState);
        workplace(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        stepController = rootView.findViewById(R.id.stepPlayerControlView);
        stepPlayerCover = rootView.findViewById(R.id.stepPlayerCover);

        stepPlayerName = rootView.findViewById(R.id.stepPlayerName);
        stepPlayerArtist = rootView.findViewById(R.id.stepPlayerArtist);
        stepPlayerHeader = rootView.findViewById(R.id.stepPlayerHeader);
        exoNextBtn = rootView.findViewById(R.id.exo_next);
    }

    private void workplace(View rootView, Bundle savedInstanceState) {

        stepPlayerHeader.setText(header);
        rootView.findViewById(R.id.stepPlayerBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        if (stepPlayer == null) {
            stepPlayer = ExoPlayerFactory.newSimpleInstance(cons, new DefaultTrackSelector());
            stepController.setPlayer(stepPlayer);

            stepDataSourceFactory = new DefaultDataSourceFactory(cons, Util.getUserAgent(cons, "Step Player"));
            if (stepConcatenatingMediaSource == null)
                stepConcatenatingMediaSource = new ConcatenatingMediaSource();

            updateDataList(dataList);
        }

        if (stepPlayer != null) {
            stepPlayer.seekTo(currentPlay, 0);
            lockNextChapter(currentPlay == currentStep);
            stepPlayer.setPlayWhenReady(true);
        }
        stepPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        stepController.setVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                stepController.show();
            }
        });
        stepPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPositionDiscontinuity(int reason) {
                int newIndex = stepPlayer.getCurrentWindowIndex();

                if (newIndex != currentPlay) {
                    stopAnimation();
                    loadAnimation();

                    if (newIndex > currentStep) {
                        //TODO : update step here
                        currentStep = newIndex;
                        Random random = new Random();
                        int x = random.nextInt(1000);
                        database.getReference().child("users").child(user.getUid()).child("temp_steam").setValue(x);
                        database.getReference().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserDetails userDetails = dataSnapshot.child("users").child(user.getUid()).getValue(UserDetails.class);
                                ArrayList<StepLogItem> log = userDetails.step_log;
                                boolean isUpdate = false;

                                if (log != null) {
                                    for (int i = 0; i < log.size(); i++) {
                                        if (log.get(i).stepId.equals(header)) {
                                            log.get(i).updateStep();
                                            isUpdate = true;
                                        }
                                    }

                                    if (!isUpdate){
                                        log.add(new StepLogItem(header, currentStep +""));
                                    }
                                }else{
                                    log = new ArrayList<>();
                                    log.add(new StepLogItem(header, currentStep +""));
                                }

                                userDetails.setStep_log(log);
                                database.getReference().child("users").child(user.getUid()).setValue(userDetails);

                                database.getReference().removeEventListener(this);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    lockNextChapter(newIndex == currentStep);

                    currentPlay = newIndex;
                    Log.d("dodo", "onPositionDiscontinuity: " + currentPlay + " : " + currentStep);
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    loadAnimation();
                } else {
                    stopAnimation();
                }
            }
        });
    }

    private void updateDataList(ArrayList<MediaItem> dataList) {
        for (MediaItem item : dataList) {
            stepMediaSource = new ProgressiveMediaSource.Factory(stepDataSourceFactory)
                    .createMediaSource(Uri.parse(item.link_2));
            stepConcatenatingMediaSource.addMediaSource(stepMediaSource);
        }
        stepPlayer.prepare(stepConcatenatingMediaSource);
    }

    private void lockNextChapter(boolean condition) {
        if (condition) {
            exoNextBtn.setBackgroundResource(R.drawable.ic_action_next_lock);
            exoNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Please finish this chapter!!", Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
            exoNextBtn.setBackgroundResource(R.drawable.ic_action_next);
            exoNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stepPlayer.next();
                }
            });
        }
    }

    private void loadAnimation() {
        stepPlayerName.setText(dataList.get(stepPlayer.getCurrentWindowIndex()).name);
        stepPlayerArtist.setText(dataList.get(stepPlayer.getCurrentWindowIndex()).artist);

        Glide.with(cons)
                .load(dataList.get(stepPlayer.getCurrentWindowIndex()).image_link_2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(stepPlayerCover);

        anim = new RotateAnimation(0, 360, stepPlayerCover.getPivotX(), stepPlayerCover.getPivotY());
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(15000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (stepPlayer.isPlaying()) {
                    stepPlayerCover.startAnimation(anim);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        stepPlayerCover.startAnimation(anim);
        isAnimationPlaying = true;
    }

    private void stopAnimation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isAnimationPlaying) {
                    stepPlayerCover.getAnimation().cancel();
                    isAnimationPlaying = false;
                }
            }
        });
    }


    @Override
    public void onStop() {
        if (stepPlayer != null) {
            stepController.setPlayer(null);
            stepPlayer.release();
            stepPlayer = null;
        }
//        getFragmentManager().popBackStack();
        listener.onDestroy();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (stepPlayer != null) {
            stepController.setPlayer(null);
            stepPlayer.release();
            stepPlayer = null;
        }
//        getFragmentManager().popBackStack();
        listener.onDestroy();
        super.onDestroy();
    }

    public interface StepListener {
        void onDestroy();
    }
}
