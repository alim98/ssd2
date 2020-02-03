package com.alim.ssn.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class YekanTextView extends AppCompatTextView {
    public YekanTextView(Context context) {
        super(context);
        setCustomFont();
    }

    public YekanTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont();
    }

    public YekanTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont();
    }
    public boolean setCustomFont() {
        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/yekan.ttf");
        } catch (Exception e) {
            Log.e(TAG, "Unable to load typeface: "+e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}
