package com.alternative.cap.restmindv3.ui.setting.sub_setting;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.activity.multi.NavigationHomePageActivity;
import com.alternative.cap.restmindv3.util.SettingListener;

public class NotificationsFragment extends Fragment {


    private static final String EXTRA_NOTIFICATION_ID = "114";
    private static final String ACTION_SNOOZE = "354";
    static SettingListener listener;
    private  final String CHANNEL_ID = "124";

    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance(SettingListener passingListener) {

        Bundle args = new Bundle();
        listener = passingListener;
        NotificationsFragment fragment = new NotificationsFragment();
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
        View rootView = inflater.inflate( R.layout.fragment_notifications, container, false );
        initInsance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);

        return rootView;
    }


    private void initInsance(View rootView, Bundle savedInstanceState) {

    }

    private void workbench(View rootView, Bundle savedInstanceState) {

        rootView.findViewById( R.id.settingNotificationBackBtn ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        } );


        Intent intent = new Intent(getContext(), NavigationHomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        Intent snoozeIntent = new Intent(getContext(), NavigationHomePageActivity.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(getContext(), 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.image_logo_bottomshadow)
                .setContentTitle("test")
                .setContentText("test text at one line!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("test text at one line!"))
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_music, getString(R.string.snooze),
                        snoozePendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getContext());
        int notificationId = 1;
        notificationManagerCompat.notify(notificationId, builder.build());

        getFragmentManager().popBackStack();
    }



    @Override
    public void onDestroy() {
        listener.onClickedDestroy();
        super.onDestroy();
    }
}
