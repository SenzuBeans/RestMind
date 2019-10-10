package com.alternative.cap.restmindv3.activity.floating;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.alternative.cap.restmindv3.R;

public class FloatingActivity extends AppCompatActivity {

    final int SYSTEM_ALERT_WINDOW_PERMISSION = 0471;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_floating );

        findViewById( R.id.floatingStartBtn ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RuntimePermissionForUser();
                finish();
            }
        } );
    }

    public void RuntimePermissionForUser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent PermissionIntent = new Intent( Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));

            startActivityForResult(PermissionIntent, SYSTEM_ALERT_WINDOW_PERMISSION);
        }else{
            startService( new Intent( FloatingActivity.this, FloatingAnimeService.class ) );
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION && requestCode == RESULT_OK){
            startService( new Intent( FloatingActivity.this, FloatingAnimeService.class ) );
        }
    }
}
