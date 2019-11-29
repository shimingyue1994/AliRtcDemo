package com.yue.alirtcdemo.weight;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.annotation.Nullable;

import com.yue.alirtcdemo.R;

import org.webrtc.sdk.SophonSurfaceView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Random;

/**
 * Module: ARTCVideoLayout
 * <p>
 * Function:
 * <p>
 * 此 ARTCVideoLayout 封装了{@link SophonSurfaceView} 以及业务逻辑 UI 控件
 * 作用：
 * 1. 实现了手势监听，配合 {@link ARTCVideoLayoutManager} 能够实现自由拖动 View。
 * 详情可见：{@link ARTCVideoLayout#initGestureListener()}
 * 实现原理：利用 RelativeLayout 的 margin 实现了能够在父容器自由定位的特性；需要注意，{@link ARTCVideoLayout} 不能增加约束规则，如 alignParentRight 等，否则无法自由定位。
 * <p>
 * 2. 对{@link SophonSurfaceView} 与逻辑 UI 进行组合，在 muteLocal、音量回调等情况，能够进行 UI 相关的变化。若您的项目中，也相关的业务逻辑，可以参照 Demo 的相关实现。
 */
class ARTCVideoLayout extends RelativeLayout implements View.OnClickListener {
    private SophonSurfaceView mVideoView;
    private OnClickListener mClickListener;
    private GestureDetector mSimpleOnGestureListener;
    private ViewGroup mVgFuc;
    private boolean mMoveable;
    private FrameLayout mFlContent;


    public ARTCVideoLayout(Context context) {
        this(context, null);
    }

    public ARTCVideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFuncLayout();
        initGestureListener();
    }

    public SophonSurfaceView getVideoView() {
        return mVideoView;
    }

    private void initFuncLayout() {
        mVgFuc = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_trtc_func, this, true);
        mVideoView = (SophonSurfaceView) mVgFuc.findViewById(R.id.trtc_tc_cloud_view);
        mFlContent = mVgFuc.findViewById(R.id.fl_content);
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        mFlContent.setBackgroundColor(Color.rgb(r,g,b));
        mVideoView.setZOrderOnTop(true);
        mVideoView.setZOrderMediaOverlay(true);
        mVideoView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    private void initGestureListener() {
        mSimpleOnGestureListener = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mClickListener != null) {
                    mClickListener.onClick(ARTCVideoLayout.this);
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!mMoveable) return false;
                ViewGroup.LayoutParams params = ARTCVideoLayout.this.getLayoutParams();
                // 当 TRTCVideoView 的父容器是 RelativeLayout 的时候，可以实现拖动
                if (params instanceof LayoutParams) {
                    LayoutParams layoutParams = (LayoutParams) ARTCVideoLayout.this.getLayoutParams();
                    int newX = (int) (layoutParams.leftMargin + (e2.getX() - e1.getX()));
                    int newY = (int) (layoutParams.topMargin + (e2.getY() - e1.getY()));

                    layoutParams.leftMargin = newX;
                    layoutParams.topMargin = newY;

                    ARTCVideoLayout.this.setLayoutParams(layoutParams);
                }
                return true;
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mSimpleOnGestureListener.onTouchEvent(event);
            }
        });
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        mClickListener = l;
    }


    public void setMoveable(boolean enable) {
        mMoveable = enable;
    }

    @Override
    public void onClick(View v) {

    }

}
