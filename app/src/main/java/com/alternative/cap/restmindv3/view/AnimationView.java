package com.alternative.cap.restmindv3.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.alternative.cap.restmindv3.R;

import java.io.IOException;
import java.io.InputStream;

public class AnimationView extends LinearLayout {

    private final int FRAME_W = 75;
    private final int FRAME_H = 67;
    private final int NB_FRAME = 8;
    private final int COUNT_X = 8;
    private final int COUNT_Y = 1;
    private final int FRAME_DURATION = 120;
    private final int SCALE_FACTOR = 10;

    private ImageView img;
    private Bitmap[] bmps;


    public AnimationView(Context context) {
        super( context );
        initInstance( context );
        workbench( context );
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
        initInstance( context );
        workbench( context );
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        initInstance( context );
        workbench( context );
    }

    private void initInstance(Context context) {
        inflate( getContext(), R.layout.view_animation, this );
    }

    private void workbench(Context context) {
        img = findViewById( R.id.animationViewImage );
        Bitmap birdBmp = getBitmapFromAssets( context, "anime/miku/walk.png" );

        if (birdBmp != null) {

            bmps = new Bitmap[NB_FRAME];
            int currentFrame = 0;
            for (int i = 0; i < COUNT_Y; i++) {
                for (int j = 0; j < COUNT_X; j++) {
                    Log.d( "dodo", "workbench: animation : "+ j +" : "+(FRAME_W * j + FRAME_W) + " : " + birdBmp.getWidth());

                    bmps[currentFrame] = Bitmap.createBitmap( birdBmp
                            , FRAME_W * j , FRAME_H *i
                            , FRAME_W
                            , FRAME_H );

                    bmps[currentFrame] = Bitmap.createScaledBitmap( bmps[currentFrame]
                            , FRAME_W * SCALE_FACTOR, FRAME_H * SCALE_FACTOR
                            , true );

                    if (++currentFrame >= NB_FRAME) {
                        break;
                    }
                }
            }

            //Create animation programmatically
            final AnimationDrawable animation = new AnimationDrawable();
            animation.setOneShot( false );

            for (int i = 0; i < NB_FRAME; i++) {
                animation.addFrame( new BitmapDrawable( context.getResources(), bmps[i] )
                        , FRAME_DURATION );
            }

            if (Build.VERSION.SDK_INT < 16){
                img.setBackgroundDrawable( animation );
            }else{
                img.setBackground( animation );
            }

            img.post( new Runnable() {
                @Override
                public void run() {
                    animation.start();
                }
            } );
        }
    }

    private Bitmap getBitmapFromAssets(Context context, String filepath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        Bitmap bitmap = null;

        try {
            istr = assetManager.open( filepath );
            bitmap = BitmapFactory.decodeStream( istr );


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (istr != null) {
                try {
                    istr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }
}
