package com.alternative.cap.restmindv3.ui.setting.sub_setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.support.AboutPage;
import com.alternative.cap.restmindv3.support.Element;
import com.alternative.cap.restmindv3.util.SettingListener;


public class ContactSupport extends Fragment {

    static SettingListener listener;

    public ContactSupport() {
        // Required empty public constructor
    }

    public static ContactSupport newInstance(SettingListener passingListener) {

        Bundle args = new Bundle();
        listener = passingListener;
        ContactSupport fragment = new ContactSupport();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        init(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(getContext())
                .isRTL(false)
                .setImage(R.drawable.image_logo)
                .addItem(new Element().setTitle("Version 6.2"))
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("uppyyoyo@gmail.com")
                .addFacebook("sarawut.loujumpar")
                .addYoutube("i")
                .addGitHub("SenzuBeans\n")
                .create();
    }

    private void init(Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate( R.layout.fragment_contact_support, container, false );
        initInsance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);

        return rootView;
    }

    private void initInsance(View rootView, Bundle savedInstanceState) {

    }

    private void workbench(View rootView, Bundle savedInstanceState) {

        rootView.findViewById( R.id.settingContentBackBtn ).setOnClickListener( new View.OnClickListener() {
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
