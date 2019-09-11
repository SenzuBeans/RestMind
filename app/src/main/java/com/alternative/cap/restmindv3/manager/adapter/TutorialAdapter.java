package com.alternative.cap.restmindv3.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.alternative.cap.restmindv3.view.tutorial.Page1Tutorial;
import com.alternative.cap.restmindv3.view.tutorial.Page2Tutorial;
import com.alternative.cap.restmindv3.view.tutorial.Page3Tutorial;
import com.alternative.cap.restmindv3.view.tutorial.Page4Tutorial;

public class TutorialAdapter extends PagerAdapter {

    private Context con;

    public TutorialAdapter(Context context) {
        super();
        con = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(con);
        ViewGroup layout;
        switch (position) {
            case 0:
                layout = new Page1Tutorial(con);
                container.addView(layout);
                return layout;
            case 1:
                layout = new Page2Tutorial(con);
                container.addView(layout);
                return layout;
            case 2:
                layout = new Page3Tutorial(con);
                container.addView(layout);
                return layout;
            case 3:
                layout = new Page4Tutorial(con);
                container.addView(layout);
                return layout;
            default:
                layout = null;
                container.addView(layout);
                return layout;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
