package com.yue.libalirtc.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.yue.libalirtc.R;

import org.webrtc.sdk.SophonSurfaceView;

import java.lang.ref.WeakReference;

import static android.graphics.PixelFormat.TRANSPARENT;

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
public class ARTCVideoLayout extends RelativeLayout implements View.OnClickListener {
    public WeakReference<IVideoLayoutListener> mWefListener;
    private SophonSurfaceView mVideoView;
    private OnClickListener mClickListener;
    private GestureDetector mSimpleOnGestureListener;
    private ViewGroup mVgFuc;
    private FrameLayout mVideoContent;
    private boolean mMoveable;


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

    public FrameLayout getVideoContent() {
        return mVideoContent;
    }

    private void initFuncLayout() {
        mVgFuc = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_artc_func, this, true);
        mVideoContent = mVgFuc.findViewById(R.id.fl_artc_video_content);
        mVideoView = (SophonSurfaceView) mVgFuc.findViewById(R.id.artc_tc_cloud_view);
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
    public void setOnClickListener(@Nullable OnClickListener l) {
        mClickListener = l;
    }


    public void setMoveable(boolean enable) {
        mMoveable = enable;
    }


    /**
     * 由于sufaceview zorder问题存在遮挡view的情况 所以需要重新设置
     *
     * @param isZorder true 显示在其他view之上 false 不显示在其他view之上
     */
    public void setZorder(boolean isZorder) {
        if (getVideoView() != null) {
            if (!isZorder)
                getVideoView().getHolder().setFormat(TRANSPARENT);
            getVideoView().setZOrderMediaOverlay(isZorder);
            getVideoView().setZOrderMediaOverlay(isZorder);
        }
    }

    @Override
    public void onClick(View v) {
        IVideoLayoutListener listener = mWefListener != null ? mWefListener.get() : null;
        if (listener == null) return;
        int id = v.getId();
        if (id == R.id.btn_roate) {
//            mVideoView.setRotation((mVideoView.getRotation()+90)%360);
            listener.onVideoRoate(this);
        }
    }


    public void setIVideoLayoutListener(IVideoLayoutListener listener) {
        if (listener == null) {
            mWefListener = null;
        } else {
            mWefListener = new WeakReference<>(listener);
        }
    }

    public interface IVideoLayoutListener {

        void onVideoRoate(ARTCVideoLayout view);
    }

}
