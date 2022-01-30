package com.demo.antizha.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import com.demo.antizha.R;

import java.util.LinkedList;
import java.util.List;

/* loaded from: classes3.dex */
public class SwipBackLayout extends FrameLayout {
    private View N;
    private int O;
    private int P;
    private int b0;
    private int c0;
    private Scroller d0;
    private int e0;
    private boolean f0;
    private boolean g0;
    private boolean h0;
    private Activity i0;
    private List<ViewPager> j0;
    private Drawable k0;

    public SwipBackLayout(Context context) {
        this(context, null);
    }

    public static SwipBackLayout create(Activity activity) {
        return new SwipBackLayout(activity);
    }

    private void c() {
        int scrollX = this.N.getScrollX();
        this.d0.startScroll(this.N.getScrollX(), 0, -scrollX, 0, Math.abs(scrollX));
        postInvalidate();
    }

    private void d() {
        int scrollX = this.e0 + this.N.getScrollX();
        this.d0.startScroll(this.N.getScrollX(), 0, (-scrollX) + 1, 0, Math.abs(scrollX));
        postInvalidate();
    }

    private void setContentView(View view) {
        this.N = (View) view.getParent();
    }

    public boolean b() {
        return this.h0;
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.d0.computeScrollOffset()) {
            this.N.scrollTo(this.d0.getCurrX(), this.d0.getCurrY());
            postInvalidate();
            if (this.d0.isFinished() && this.g0) {
                this.i0.finish();
            }
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void dispatchDraw(Canvas canvas) {
        View view;
        super.dispatchDraw(canvas);
        if (this.k0 != null && (view = this.N) != null) {
            int left = view.getLeft() - this.k0.getIntrinsicWidth();
            this.k0.setBounds(left, this.N.getTop(), this.k0.getIntrinsicWidth() + left, this.N.getBottom());
            this.k0.draw(canvas);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.h0) {
            return false;
        }
        ViewPager a2 = a(this.j0, motionEvent);
        if (a2 != null && a2.getCurrentItem() != 0) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            int rawX = (int) motionEvent.getRawX();
            this.c0 = rawX;
            this.P = rawX;
            this.b0 = (int) motionEvent.getRawY();
        } else if (action == 2 && ((int) motionEvent.getRawX()) - this.P > this.O && Math.abs(((int) motionEvent.getRawY()) - this.b0) < this.O) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
        super.onLayout(z, i2, i3, i4, i5);
        if (z) {
            this.e0 = getWidth();
            a(this.j0, this);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.h0) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 1) {
            this.f0 = false;
            if (this.N.getScrollX() <= (-this.e0) / 2) {
                this.g0 = true;
                d();
            } else {
                c();
                this.g0 = false;
            }
        } else if (action == 2) {
            int rawX = (int) motionEvent.getRawX();
            int i2 = this.c0 - rawX;
            this.c0 = rawX;
            if (rawX - this.P > this.O && Math.abs(((int) motionEvent.getRawY()) - this.b0) < this.O) {
                this.f0 = true;
            }
            if (rawX - this.P >= 0 && this.f0) {
                this.N.scrollBy(i2, 0);
            }
        }
        return true;
    }

    public void setInterEvent(boolean z) {
        this.h0 = z;
    }

    public SwipBackLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public void init() {
        ViewGroup viewGroup = (ViewGroup) this.i0.getWindow().getDecorView();
        ViewGroup viewGroup2 = (ViewGroup) viewGroup.getChildAt(0);
        viewGroup.removeView(viewGroup2);
        addView(viewGroup2);
        setContentView(viewGroup2);
        viewGroup.addView(this);
    }

    public SwipBackLayout(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
        this.h0 = false;
        this.j0 = new LinkedList();
        this.i0 = (Activity) context;
        this.O = ViewConfiguration.get(context).getScaledTouchSlop();
        this.d0 = new Scroller(context);
        this.k0 = getResources().getDrawable(R.drawable.swip_left_shadow, null);
    }

    private void a(List<ViewPager> list, ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = viewGroup.getChildAt(i2);
            if (childAt instanceof ViewPager) {
                list.add((ViewPager) childAt);
            } else if (childAt instanceof ViewGroup) {
                a(list, (ViewGroup) childAt);
            }
        }
    }

    private ViewPager a(List<ViewPager> list, MotionEvent motionEvent) {
        if (!(list == null || list.size() == 0)) {
            Rect rect = new Rect();
            for (ViewPager viewPager : list) {
                viewPager.getHitRect(rect);
                if (rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                    return viewPager;
                }
            }
        }
        return null;
    }
}
