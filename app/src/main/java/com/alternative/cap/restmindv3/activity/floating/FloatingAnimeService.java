package com.alternative.cap.restmindv3.activity.floating;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.alternative.cap.restmindv3.view.AnimationView;

public class FloatingAnimeService extends Service {

    WindowManager wm;
    LinearLayout ll;
    AnimationView animationView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        wm = (WindowManager) getSystemService( WINDOW_SERVICE );

        ll = new LinearLayout( this );
        animationView = new AnimationView( this );
        ll.setBackgroundColor( Color.RED );
        LinearLayout.LayoutParams layoutParameteres = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT );
        ll.setBackgroundColor( Color.argb( 0, 0, 0, 0 ) );
        ll.setLayoutParams( layoutParameteres );

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                200, 200, WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT );

        parameters.gravity = Gravity.CENTER | Gravity.CENTER;
        parameters.x = 0;
        parameters.y = 0;

//        Button stop = new Button( this );
//        stop.setText( "Stop" );
//        ViewGroup.LayoutParams btnParameters = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
//        stop.setLayoutParams( btnParameters );
//
        ll.addView( animationView );
        wm.addView( ll, parameters );

        ll.setOnTouchListener( new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = parameters;
            int x,y;
            float pressedX, pressedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));

                        wm.updateViewLayout( ll, updatedParameters );
                    default:
                        break;
                }

                return false;
            }
        } );

        ll.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView( ll );
                stopSelf();
//                System.exit( 0 );
            }
        } );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
