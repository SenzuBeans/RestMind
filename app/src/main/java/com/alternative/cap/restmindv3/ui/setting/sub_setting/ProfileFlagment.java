package com.alternative.cap.restmindv3.ui.setting.sub_setting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alternative.cap.restmindv3.R;

public class ProfileFlagment extends Fragment {


    public ProfileFlagment() {
        // Required empty public constructor
    }

    public static ProfileFlagment newInstance() {

        Bundle args = new Bundle();

        ProfileFlagment fragment = new ProfileFlagment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_profile__flagment, container, false );
        initInsance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);
        return rootView;
    }

    private void initInsance(View rootView, Bundle savedInstanceState) {

    }

    private void workbench(View rootView, Bundle savedInstanceState) {

    }


}
