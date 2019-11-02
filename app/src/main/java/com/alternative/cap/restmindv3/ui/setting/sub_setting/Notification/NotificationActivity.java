package com.alternative.cap.restmindv3.ui.setting.sub_setting.Notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.activity.multi.NavigationHomePageActivity;

public class NotificationActivity extends AppCompatActivity {

    private  final String CHANNEL_ID = "124";
//    private Button notificationShowBtn;
//    private Button notificationHideBtn;
//    private EditText notificationEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getWindow().getDecorView()
                .setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        findViewById( R.id.settingNotificationBackBtn2 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationActivity.super.onBackPressed();
            }
        } );

//        notificationShowBtn = findViewById(R.id.notificationShowBtn);
//        notificationHideBtn = findViewById(R.id.notificationHideBtn);
//        notificationEditText = findViewById(R.id.notificationEditText);


        // Create an explicit intent for an Activity in your app
//        Intent intent = new Intent(this, NavigationHomePageActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.image_logo_bottomshadow)
//                .setContentTitle("test")
//                .setContentText("test text at one line!")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("test text at one line!"))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        int notificationId = 1;
//        notificationManagerCompat.notify(notificationId, builder.build());

//        notificationShowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startService(v);
//            }
//        });
//
//        notificationHideBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopService(v);
//            }
//        });
    }

//    public void startService(View v){
//        String input = notificationEditText.getText().toString();
//        Intent serviceIntent = new Intent(this, NotificationService.class);
//        serviceIntent.putExtra("inputExtra", input);
//
//        startService(serviceIntent);
//    }
//    public void stopService(View v){
//        Intent serviceIntent = new Intent(this, NotificationService.class);
//        stopService(serviceIntent);
//    }



}
