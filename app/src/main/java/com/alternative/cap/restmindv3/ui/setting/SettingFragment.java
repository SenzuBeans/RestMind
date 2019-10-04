package com.alternative.cap.restmindv3.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.activity.multi.MemberActivity;
import com.alternative.cap.restmindv3.ui.setting.sub_setting.ContactSupport;
import com.alternative.cap.restmindv3.ui.setting.sub_setting.ProfileFragment;
import com.alternative.cap.restmindv3.util.SettingListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingFragment extends Fragment
    implements SettingListener {

    private int SHOW = 0,
        HIDE = 1;

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;

    private TextView userName;

    private Button contactBtn, profileBtn;
    private FrameLayout settingContainerLayout;
    private LinearLayout settingMainLayout;


    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        initInstance(root, savedInstanceState);
        workbench(root, savedInstanceState);
        return root;
    }

    private void initInstance(View root, Bundle savedInstanceState) {
        hideNavigationBar();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = databaseReference.child(user.getUid());

        userName =  root.findViewById(R.id.userNameTv);
        userName.setText(user.getDisplayName());

        settingContainerLayout = root.findViewById( R.id.settingContainerLayout );
        settingMainLayout = root.findViewById( R.id.settingMainLayout );

        contactBtn = root.findViewById( R.id.contactBtn );
        profileBtn = root.findViewById( R.id.profileBtn );
    }

    private void workbench(View root, Bundle savedInstanceState) {


        root.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
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
        //ทุกอันที่ต่อใช้ R.id.settingContainerLayout อันเดียว ไม่ต้องสร้างเพิ่ม วางซ้ำไปเลย อย่างอื่นถูกล่ะ
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