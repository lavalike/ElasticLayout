package com.wangzhen.elastic;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 可自定义Header的弹性布局
 * Created by wangzhen on 2018/1/18.
 */
public class ElasticLayout extends LinearLayout {
    //默认拖动因子
    private static final float DEFAULT_DRAG_FACTOR = 0.3f;
    private float mDragFactor;
    private boolean isEnable;
    //HeaderView
    private View mHeaderView;
    //ContentView
    private View mContentView;
    private float lastX;
    private float lastY;

    public ElasticLayout(Context context) {
        this(context, null);
    }

    public ElasticLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElasticLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ElasticLayout);
        mDragFactor = validateFactor(typedArray.getFloat(R.styleable.ElasticLayout_drag_factor, DEFAULT_DRAG_FACTOR));
        isEnable = typedArray.getBoolean(R.styleable.ElasticLayout_drag_enable, true);
        typedArray.recycle();
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        createDefaultHeader();
    }

    /**
     * 添加默认透明HeaderView
     */
    private void createDefaultHeader() {
        mHeaderView = new FrameLayout(getContext());
        addView(mHeaderView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
    }

    /**
     * 设置HeaderView
     *
     * @param header HeaderView
     */
    public void setHeaderView(View header) {
        if (header != null) {
            this.mHeaderView = header;
            removeViewAt(0);
            addView(mHeaderView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        }
    }

    /**
     * 设置拖动因子 0~1
     *
     * @param factor factor
     */
    public void setDragFactor(float factor) {
        this.mDragFactor = validateFactor(factor);
    }

    /**
     * 校验DragFactor的合法性
     *
     * @param factor 拖拽因子
     * @return factor
     */
    private float validateFactor(float factor) {
        if (factor < 0) {
            factor = DEFAULT_DRAG_FACTOR;
        }
        if (factor > 1) {
            factor = 1;
        }
        return factor;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            mContentView = getChildAt(getChildCount() - 1);
            if (mContentView != null) {
                mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnable)
            return super.onInterceptTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //存在情况：mContentView内容未在顶部，下拉到顶部时mContentView会直接跳过前面下拉距离
                //解决方法：下拉过程中不断更新lastX、lastY坐标为当前坐标，达到从顶部下拉的效果
                if (isCanPullDown()) {
                    lastX = ev.getX();
                    lastY = ev.getY();
                }
                float diffX = ev.getX() - lastX;
                float diffY = ev.getY() - lastY;
                if (diffY > 0 && diffY > Math.abs(diffX)) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 详见{@link android.support.v4.widget.SwipeRefreshLayout#requestDisallowInterceptTouchEvent(boolean)}
     *
     * @param disallowIntercept disallowIntercept
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if ((android.os.Build.VERSION.SDK_INT < 21 && mContentView instanceof AbsListView)
                || ViewCompat.isNestedScrollingEnabled(mContentView)
                || isCanPullDown()) {
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float deltaY = event.getY() - lastY;
                changeHeader(deltaY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                restoreHeader();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 将HeaderView高度恢复为0
     */
    private void restoreHeader() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mHeaderView.getBottom(), 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParams = mHeaderView.getLayoutParams();
                layoutParams.height = (int) animation.getAnimatedValue();
                mHeaderView.requestLayout();
            }
        });
        valueAnimator.start();
    }

    /**
     * 改变HeaderView的高度
     *
     * @param height 高度
     */
    private void changeHeader(float height) {
        if (height < 0)
            height = 0;
        ViewGroup.LayoutParams layoutParams = mHeaderView.getLayoutParams();
        layoutParams.height = (int) (height * mDragFactor);
        mHeaderView.requestLayout();
    }

    /**
     * 判断是否滚动到顶部
     */
    private boolean isCanPullDown() {
        return mContentView != null && ViewCompat.canScrollVertically(mContentView, -1);
    }
}
