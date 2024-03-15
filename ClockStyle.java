package com.android.settings.preferences.lockscreen;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;
import com.android.settings.R;

public class ClockStyle extends RelativeLayout {

    private static final int DEFAULT_STYLE = 0; //Disabled
    private static final String CLOCK_STYLE_KEY = "clock_style";
    private static final Uri CLOCK_STYLE_URI = Settings.System.getUriFor(CLOCK_STYLE_KEY);

    private Context mContext;
    private SparseArray<View> clockViews;
    private ContentResolver mContentResolver;

    public ClockStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findClockViews();
        new MyContentObserver(new Handler()).observe();
        updateClockView();
    }

    private void findClockViews() {
        clockViews = new SparseArray<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getTag() != null && child.getTag().equals("clock_view")) {
                clockViews.put(clockViews.size(), child);
            }
        }
    }

    private void updateClockView() {
        if (clockViews != null) {
            int clockStyle = Settings.System.getInt(mContentResolver, CLOCK_STYLE_KEY, DEFAULT_STYLE);
            for (int i = 0; i < clockViews.size(); i++) {
                View clockView = clockViews.valueAt(i);
                if (clockView != null) {
                    clockView.setVisibility(i == clockStyle ? View.VISIBLE : View.GONE);
                }
            }
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
