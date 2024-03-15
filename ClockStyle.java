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

    private static final int[] CLOCK_VIEW_IDS = {
            R.id.keyguard_clock_style_default,
            R.id.keyguard_clock_style_oos,
            R.id.keyguard_clock_style_ios,
            R.id.keyguard_clock_style_cos,
            R.id.keyguard_clock_style_custom,
            R.id.keyguard_clock_style_custom1,
            R.id.keyguard_clock_style_custom2,
            R.id.keyguard_clock_style_custom3,
            R.id.keyguard_clock_style_miui,
            R.id.keyguard_clock_style_ide,
            R.id.keyguard_clock_style_lottie,
            R.id.keyguard_clock_style_lottie2,
            R.id.keyguard_clock_style_fluid,
            R.id.keyguard_clock_style_hyper,
            R.id.keyguard_clock_style_dual,
            R.id.keyguard_clock_style_stylish,
            R.id.keyguard_clock_style_sidebar,
            R.id.keyguard_clock_style_minimal,
            R.id.keyguard_clock_style_minimal2,
            R.id.keyguard_clock_style_minimal3
    };

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
        clockViews = new SparseArray<>(CLOCK_VIEW_IDS.length);
        for (int i = 0; i < CLOCK_VIEW_IDS.length; i++) {
            View clockView = findViewById(CLOCK_VIEW_IDS[i]);
            clockViews.put(i, clockView);
        }
        new MyContentObserver(new Handler()).observe();
        updateClockView();
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
