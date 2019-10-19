package com.alternative.cap.restmindv3.ui.breath;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.BreathLogItem;
import com.alternative.cap.restmindv3.util.MutableValue;
import com.alternative.cap.restmindv3.util.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import cn.iwgang.countdownview.CountdownView;
import me.tankery.lib.circularseekbar.CircularSeekBar;


public class BreathFragment extends Fragment {


    private CircularImageView circularImageView;
    private MediaPlayer breathSongPlayer;
    private CountdownView timerView;
    private Button playerBtn;
    private TextView breathStatusTextView;
    private CircularSeekBar circularSeekBar;

    private String[] STATE = {"inhale", "hold", "exhale"};
    private MutableValue passingBreathData;
    private long[] breathData;
    private boolean timerResume = false;
    private boolean isSongPlaying = false;
    private boolean isSetting = false;

    private long inhale;
    private long hold;
    private long exhale;
    private long timer;

    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private FirebaseUser user;
    private ValueEventListener valueEventListener;

    Random random = new Random();
    int x = random.nextInt(1000);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passingBreathData = new MutableValue();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_breath, container, false);
        init(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);
        return rootView;
    }

    private void init(View rootView, Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference = databaseReference.child("users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        circularImageView = rootView.findViewById(R.id.circularImageView);
        playerBtn = rootView.findViewById(R.id.playerBtn);
        breathStatusTextView = rootView.findViewById(R.id.breathStatusTextView);
        timerView = rootView.findViewById(R.id.breathTimer);
        circularSeekBar = rootView.findViewById(R.id.circularSeekBar);
        circularSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

//        updateSeekBar = new Thread(this);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserDetails userDetails = dataSnapshot.child(user.getUid()).getValue(UserDetails.class);
                ArrayList<BreathLogItem> log = userDetails.breath_log;

                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                String currentDate = sdf.format(new Date());

                boolean isUpdate = false;
                if (log != null) {
                    for (int i = 0; i < log.size(); i++) {
                        if (log.get(i).date.equals(currentDate)) {
                            if (timerResume) {
                                log.get(i).updateTotalTime(timer - ((timerView.getMinute()*60000)+(timerView.getSecond()*1000)));
                                log.get(i).updateDismissTime(((timerView.getMinute()*60000)+(timerView.getSecond()*1000)));

                                userDetails.updateTotalTime(timer - ((timerView.getMinute()*60000)+(timerView.getSecond()*1000)));
                                userDetails.updateMissTime(((timerView.getMinute()*60000)+(timerView.getSecond()*1000)));
                            } else {
                                log.get(i).updateTotalTime(timer);

                                userDetails.updateTotalTime(timer);
                                userDetails.updateMissTime(0);
                            }
                            isUpdate = true;
                        }
                    }

                    if (!isUpdate) {
                        if (timerResume) {
                            log.add(new BreathLogItem(currentDate, ((timer -((timerView.getMinute()*60000)+(timerView.getSecond()*1000))) / 60000) + ""
                                    , (timerView.getMinute()) + ""));
                            userDetails.updateTotalTime(timer - ((timerView.getMinute()*60000)+(timerView.getSecond()*1000)));
                            userDetails.updateMissTime(((timerView.getMinute()*60000)+(timerView.getSecond()*1000)));
                        } else {
                            log.add(new BreathLogItem(currentDate, (timer / 60000) + ""));
                            userDetails.updateTotalTime(timer);
                            userDetails.updateMissTime(0);
                        }
                    }

                    if (log.size() > 7) {
                        log.remove(0);
                    }

                    for (int i = 0; i < log.size(); i++){
                        if (Integer.parseInt(log.get(i).date) - Integer.parseInt(log.get(log.size()-1).date) > 7){
                            log.get(i).setTotalTime("0");
                            log.get(i).setDismissTime("0");
                        }
                    }

                } else {
                    log = new ArrayList<>();
                    if (timerResume) {
                        log.add(new BreathLogItem(currentDate, ((timer - ((timerView.getMinute()*60000)+(timerView.getSecond()*1000))) / 60000) + "", (timerView.getMinute()) + ""));
                        userDetails.updateTotalTime(timer - ((timerView.getMinute()*60000)+(timerView.getSecond()*1000)));
                        userDetails.updateMissTime(((timerView.getMinute()*60000)+(timerView.getSecond()*1000)));
                    } else {
                        log.add(new BreathLogItem(currentDate, (timer / 60000) + ""));
                        userDetails.updateTotalTime(timer);
                        userDetails.updateMissTime(0);
                    }
                }

                userDetails.setBreath_log(log);

                reference.child(user.getUid()).setValue(userDetails);
                reference.removeEventListener(valueEventListener);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void workbench(View rootView, Bundle savedInstanceState) {
        hideNavigationBar();

        playerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSongPlaying) {
                    startRunningBreath();

                    if (timer > 0) {
                        circularSeekBar.setMax(timer);
                        circularSeekBar.setProgress(timer);
                        timerView.setOnCountdownIntervalListener(1000, new CountdownView.OnCountdownIntervalListener() {
                            @Override
                            public void onInterval(CountdownView cv, long remainTime) {
                                circularSeekBar.setProgress(cv.getRemainTime());
//                                Log.d("dodo", "onInterval: " + cv.getRemainTime());
                            }
                        });
                        timerView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                stopRunningBreath(false);
                            }
                        });
                    }
                } else {
                    stopRunningBreath(true);
                }
//                Log.d("dodo", "onClick: "+timerView.getRemainTime());
            }
        });

        rootView.findViewById(R.id.breathSettingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRunningBreath(false);
                if (!isSetting) {
                    isSetting = true;
                    rootView.findViewById(R.id.contentContainerBreathFragment).setVisibility(View.VISIBLE);
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .add(R.id.contentContainerBreathFragment, BreathSettingFragment.newInstance(new BreathSettingFragment.BreathSettingListener() {
                                @Override
                                public void onSetSetting() {
                                    recallBreathData();
                                    isSetting = false;
                                }

                                @Override
                                public void onPopStack() {
                                    isSetting = false;
                                }
                            }))
                            .commit();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    private void hideNavigationBar() {
        this.getActivity().getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    private void startRunningBreath() {
        recallBreathData();
        breathSongPlayer = MediaPlayer.create(getContext(), R.raw.oo);
        breathSongPlayer.start();
        breathSongPlayer.setLooping(true);
        animationPlayer(true);
        playerBtn.setText("Stop");
        isSongPlaying = true;
//        updateSeekBar.start();
        if (timerResume) {
            timerView.start((timerView.getMinute()*60000)+(timerView.getSecond()*1000));
        } else {
            timerView.start(timer);
        }
    }

//    public void run() {
//
//        int currentPosition = (timerView.getMinute()*60000)+(timerView.getSecond()*1000);
//        int total = (int) timer;
//
//
//        while (timerView != null && isSongPlaying && currentPosition < total) {
//            try {
//                Thread.sleep(1000);
//                currentPosition = (timerView.getMinute()*60000)+(timerView.getSecond()*1000);
//            } catch (InterruptedException e) {
//                return;
//            } catch (Exception e) {
//                return;
//            }
//
//            circularSeekBar.setProgress((timerView.getMinute()*60000)+(timerView.getSecond()*1000));
//
//        }
//    }

    private void stopRunningBreath(boolean timerState) {
        if (isSongPlaying) {
            breathStatusTextView.setText("Ready");
            breathSongPlayer.stop();
            breathSongPlayer.release();
            animationPlayer(false);
//            updateSeekBar.interrupt();
            playerBtn.setText("Play");
            timerView.stop();
            isSongPlaying = false;
        }

        if (timerState) {
            if (timerView != null) {
                timerView.stop();
                timerResume = true;
            }
        } else {
            if (timerView != null) {
                timerView.stop();
                timerResume = false;
            }
        }
    }

    private void updateBreathTime() {
        reference.child(user.getUid()).child("temp_steam").setValue(x);
        reference.addValueEventListener(valueEventListener);
    }

    private void recallBreathData() {
        breathData = passingBreathData.getBreathData();

        inhale = breathData[0];
        hold = breathData[1];
        exhale = breathData[2];
        timer = breathData[3];
    }

    private void animationPlayer(boolean state) {

        final ScaleAnimation scaleIn = new ScaleAnimation(1f, 1.25f, 1f, 1.25f,
                circularImageView.getWidth() / 2, circularImageView.getHeight() / 2);
        final ScaleAnimation scaleOut = new ScaleAnimation(1.25f, 1f, 1.25f, 1f,
                circularImageView.getWidth() / 2, circularImageView.getHeight() / 2);
        final ScaleAnimation textScaleIn = new ScaleAnimation(1f, 1.25f, 1f, 1.25f,
                breathStatusTextView.getWidth() / 2, breathStatusTextView.getHeight() / 2);
        final ScaleAnimation textScaleOut = new ScaleAnimation(1.25f, 1f, 1.25f, 1f,
                breathStatusTextView.getWidth() / 2, breathStatusTextView.getHeight() / 2);
        final Animation holdInAnimation = new AlphaAnimation(1, 1);
        final Animation holdOutAnimation = new AlphaAnimation(1, 1);

        Animation stopAnimation = new ScaleAnimation(1f, 1f, 1f, 1f);
        stopAnimation.setDuration(1000);

        textScaleIn.setDuration(inhale);
        scaleIn.setDuration(inhale);
        scaleIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                breathStatusTextView.setText(STATE[0]);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circularImageView.startAnimation(holdInAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation a = new ScaleAnimation(1.25f, 1.25f, 1.25f, 1.25f,
                breathStatusTextView.getWidth() / 2, breathStatusTextView.getHeight() / 2);
        a.setDuration(hold);
        holdInAnimation.setDuration(hold);
        holdInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                breathStatusTextView.setText(STATE[1]);
                breathStatusTextView.startAnimation(a);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circularImageView.startAnimation(scaleOut);
                breathStatusTextView.startAnimation(textScaleOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textScaleOut.setDuration(exhale);
        scaleOut.setDuration(exhale);
        scaleOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                breathStatusTextView.setText(STATE[2]);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circularImageView.startAnimation(holdOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        holdOutAnimation.setDuration(hold);
        holdOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                breathStatusTextView.setText(STATE[1]);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circularImageView.startAnimation(scaleIn);
                breathStatusTextView.startAnimation(textScaleIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        if (state) {
            circularImageView.startAnimation(scaleIn);
            breathStatusTextView.startAnimation(textScaleIn);
        } else {
            circularImageView.startAnimation(stopAnimation);
            breathStatusTextView.startAnimation(stopAnimation);
        }
    }

    @Override
    public void onStop() {
        stopRunningBreath(true);
        animationPlayer(false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopRunningBreath(true);
        updateBreathTime();
        animationPlayer(false);
        super.onDestroy();
    }
}