package com.wangzhen.elastic;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 弹性布局
 * Created by wangzhen on 2018/1/18.
 */
public class ElasticLayout extends FrameLayout {
    private View contentView;
    private float startY;
    private boolean isMoved;
    private Rect originalRect = new Rect();
    private float factor = 0.3f;
    private int pullDirection = PullDirection.DIRECTION_NONE;
    private View behindView;
    private boolean isCanPullDown;
    //上一次Y距离
    private int lastDeltaY;

    public ElasticLayout(Context context) {
        super(context);
    }

    public ElasticLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ElasticLayout);
        factor = typedArray.getFloat(R.styleable.ElasticLayout_el_factor, 0.3f);
        pullDirection = typedArray.getInt(R.styleable.ElasticLayout_el_direction, PullDirection.DIRECTION_NONE);
        int resourceId = typedArray.getResourceId(R.styleable.ElasticLayout_el_behind_view, -1);
        typedArray.recycle();
        if (resourceId > -1) {
            behindView = LayoutInflater.from(context).inflate(resourceId, null);
        }
        if (behindView != null) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(behindView, 0, layoutParams);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            contentView = getChildAt(getChildCount() - 1);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //存储子控件位置信息
        if (contentView != null)
            originalRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (contentView == null) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isCanPullDown = isCanPullDown();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isCanPullDown) break;
                int deltaY = (int) (ev.getY() - startY);
                dragMove(deltaY);
                break;
            case MotionEvent.ACTION_UP:
                if (!isMoved) break;
                collapse();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断拖动类型
     *
     * @param deltaY Y方向移动距离
     */
    private void dragMove(int deltaY) {
        if (pullDirection == PullDirection.DIRECTION_TOP) {
            if (isCanPullDown && deltaY > 0) {
                int offset = (int) (deltaY * factor);
                contentView.layout(
                        originalRect.left,
                        originalRect.top + offset,
                        originalRect.right,
                        originalRect.bottom + offset
                );
                //如何滚动过程中往回滚动，则禁用内容滚动
                if (lastDeltaY >= deltaY)
                    disableContentScroll();
                lastDeltaY = deltaY;
                isMoved = true;
            }
        }
    }

    private void disableContentScroll() {
        contentView.setOnTouchListener(disableScrollListener);
    }

    private void enableContentScroll() {
        contentView.setOnTouchListener(enableScrollListener);
    }

    /**
     * 禁用View内容滚动
     */
    OnTouchListener disableScrollListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    /**
     * 启用View内容滚动
     */
    OnTouchListener enableScrollListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    };

    /**
     * 闭合动画
     */
    private void collapse() {
        ValueAnimator animator = ValueAnimator.ofInt(contentView.getTop(), originalRect.top);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                contentView.layout(
                        originalRect.left,
                        value,
                        originalRect.right,
                        originalRect.bottom + value
                );
            }
        });
        animator.start();
        enableContentScroll();
        isMoved = false;
    }

    /**
     * 判断是否滚动到顶部
     */
    private boolean isCanPullDown() {
        return contentView.getScrollY() == 0 ||
                contentView.getHeight() < getHeight() + getScrollY();
    }

    /**
     * 获取背景View
     *
     * @return 背景View
     */
    public View getBehindView() {
        return behindView;
    }
}
