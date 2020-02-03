package com.alim.ssn.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.alim.ssn.R;

public class CircularImageView extends ImageView {
    private static final ScaleType SCALE_TYPE=ScaleType.CENTER_CROP;
    private static final int DEF_PRESS_HIGHLIGHT_COLOR = 0x32000000;
    private Shader mBitmapShader;
    private Matrix mShaderMatrix;

    private Bitmap mBitmap;

    private Paint mBitmapPaint;
    private Paint mStrokePaint;
    private Paint mPressedPaint;

    private boolean mInitialized;
    private boolean mHighlighted;
    private boolean mPressed;

    public CircularImageView(Context context) {
        super(context);

        init();
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);
        super.setAdjustViewBounds(true);

    }

    public CircularImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int strokeColor= Color.TRANSPARENT;
        float strokeWidth=0;
        boolean highlightEnabled=true;
        int highlightColor=DEF_PRESS_HIGHLIGHT_COLOR;

        if (attrs!=null)
        {
            TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0);

        }
    }

    public CircularImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircularImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
