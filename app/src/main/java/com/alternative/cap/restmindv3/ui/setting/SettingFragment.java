package com.alternative.cap.restmindv3.ui.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.activity.floating.FloatingActivity;
import com.alternative.cap.restmindv3.activity.multi.MemberActivity;
import com.alternative.cap.restmindv3.manager.Contextor;
import com.alternative.cap.restmindv3.ui.setting.sub_setting.ChangePassword;
import com.alternative.cap.restmindv3.ui.setting.sub_setting.ContactSupport;
import com.alternative.cap.restmindv3.ui.setting.sub_setting.Notification.NotificationActivity;
import com.alternative.cap.restmindv3.ui.setting.sub_setting.NotificationsFragment;
import com.alternative.cap.restmindv3.ui.setting.sub_setting.ProfileFragment;
import com.alternative.cap.restmindv3.ui.setting.sub_setting.ShareWithFriends;
import com.alternative.cap.restmindv3.util.SettingListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingFragment extends Fragment
    implements SettingListener {

    private SharedPreferences shared;

    private int SHOW = 0,
        HIDE = 1;

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;

    private TextView userName;

    private Button contactBtn, profileBtn, notificationsBtn, shareBtn;
    private FrameLayout settingContainerLayout;
    private LinearLayout settingMainLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = databaseReference.child(user.getUid());
        shared = getContext().getSharedPreferences("BackgroundWT", 0);
        hideNavigationBar();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        initInstance(root, savedInstanceState);
        workbench(root, savedInstanceState);
        return root;
    }

    private void initInstance(View root, Bundle savedInstanceState) {

        userName =  root.findViewById(R.id.userNameTv);
        userName.setText(user.getDisplayName());
        settingContainerLayout = root.findViewById( R.id.settingContainerLayout );
        settingMainLayout = root.findViewById( R.id.settingMainLayout );
        contactBtn = root.findViewById( R.id.contactBtn );
        profileBtn = root.findViewById( R.id.profileBtn );
        notificationsBtn = root.findViewById( R.id.notificationsBtn );
        shareBtn = root.findViewById( R.id.shareBtn );
        //changePasswordBtn  = root.findViewById( R.id.changePasswordBtn );
    }

    private void workbench(View root, Bundle savedInstanceState) {


        root.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("isBackgroundWT");
                    editor.commit();
                    FirebaseAuth.getInstance().signOut();
                    getActivity().startActivity(new Intent(getContext(), MemberActivity.class));
                    getActivity().finish();
                }
            }
        });

        contactBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapContent(SHOW);
                getChildFragmentManager().beginTransaction()
                        .add( R.id.settingContainerLayout, ContactSupport.newInstance(SettingFragment.this) )
                        .addToBackStack( null )
                        .commit();
            }
        } );

        profileBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapContent(SHOW);
                getChildFragmentManager().beginTransaction()
                        .add( R.id.settingContainerLayout, ProfileFragment.newInstance(SettingFragment.this) )
                        .addToBackStack( null )
                        .commit();
            }
        } );

        notificationsBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                swapContent(SHOW);
//                getChildFragmentManager().beginTransaction()
//                        .add( R.id.settingContainerLayout, NotificationsFragment.newInstance(SettingFragment.this) )
//                        .addToBackStack( null )
//                        .commit();
                startActivity(new Intent(getContext(), NotificationActivity.class));

//                getActivity().startActivity( new Intent( getContext(), FloatingActivity.class ) );
//                getActivity().finish();
            }
        } );
        shareBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                swapContent(SHOW);
//                getChildFragmentManager().beginTransaction()
//                        .add( R.id.settingContainerLayout, ShareWithFriends.newInstance(SettingFragment.this) )
//                        .addToBackStack( null )
//                        .commit();
                shareTextUrl();
            }
        } );

//        changePasswordBtn.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                swapContent(SHOW);
//                getChildFragmentManager().beginTransaction()
//                        .add( R.id.settingContainerLayout, ChangePassword.newInstance(SettingFragment.this) )
//                        .addToBackStack( null )
//                        .commit();
//            }
//        } );
    }

    // Method to share either text or URL.
    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "https://www.facebook.com/RestMind-124282092307252/?modal=admin_todo_tour");

        startActivity(Intent.createChooser(share, "To our site..."));
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

    private void swapContent(int status) {
        if (status == SHOW) {
            settingMainLayout.setVisibility( View.GONE );
            settingContainerLayout.setVisibility( View.VISIBLE );
        }else{
            settingMainLayout.setVisibility( View.VISIBLE );
            settingContainerLayout.setVisibility( View.GONE );
        }
    }


    @Override
    public void onClickedDestroy() {
        swapContent( HIDE );
    }
}