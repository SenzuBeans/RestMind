package com.alternative.cap.restmindv3.ui.setting.sub_setting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.SettingListener;

public class ChangePassword extends Fragment {

    static SettingListener listener;

    public ChangePassword() {
        // Required empty public constructor
    }

    public static ChangePassword newInstance(SettingListener passingListener) {

        Bundle args = new Bundle();
        listener = passingListener;
        ChangePassword fragment = new ChangePassword();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_change_password, container, false );
        initInsance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);

        return rootView;
    }


    private void initInsance(View rootView, Bundle savedInstanceState) {

    }

    private void workbench(View rootView, Bundle savedInstanceState) {

        rootView.findViewById( R.id.settingChangePasswordBackBtn ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        } );
    }

    @Override
    public void onDestroy() {
        listener.onClickedDestroy();
        super.onDestroy();
    }
}
