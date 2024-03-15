package com.android.settings.preferences.lockscreen;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.android.settings.R;

import java.util.ArrayList;
import java.util.List;

public class ClockStyle extends RelativeLayout {

    private static final int DEFAULT_STYLE = 0; //Disabled
    private static final String CLOCK_STYLE_KEY = "clock_style";
    private static final Uri CLOCK_STYLE_URI = Settings.System.getUriFor(CLOCK_STYLE_KEY);

    private Context mContext;
    private List<View> clockViews;
    private ContentResolver mContentResolver;

    public ClockStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        clockViews = findClockViews(this);
        new MyContentObserver(new Handler()).observe();
        updateClockView();
    }

    private List<View> findClockViews(View view) {
        List<View> clockViews = new ArrayList<>();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                if (isClockView(child)) {
                    clockViews.add(child);
                }
                if (child instanceof ViewGroup) {
                    clockViews.addAll(findClockViews(child));
                }
            }
        }
        return clockViews;
    }

    private boolean isClockView(View view) {
        // Add your condition here to identify clock views
        // For example, you can check if the view's ID matches a clock view ID
        // Or check if the view has specific characteristics that only clock views have
        // Replace the return statement below with your own logic
        return view.getId() == R.id.keyguard_clock_style_default ||
                view.getId() == R.id.keyguard_clock_style_oos ||
                // Add other clock view IDs as needed
                false;
    }

    private void updateClockView() {
        int clockStyle = Settings.System.getInt(mContentResolver, CLOCK_STYLE_KEY, DEFAULT_STYLE);
        for (View clockView : clockViews) {
            clockView.setVisibility(clockViews.indexOf(clockView) == clockStyle ? View.VISIBLE : View.GONE);
        }
    }

    class MyContentObserver extends ContentObserver {
        public MyContentObserver(Handler h) {
            super(h);
        }

        public void observe() {
            mContentResolver.registerContentObserver(CLOCK_STYLE_URI, false, this);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateClockView();
        }
    }
}
