package me.panjiajia.biuweather.headerView;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import me.panjiajia.biuweather.R;

public class ViewBehavior extends CoordinatorLayout.Behavior<HeaderView> {

    private Context mContext;

    private int mStartMarginLeft;
    private int mEndMargintLeft;
    private float mCityStartSize;
    private float mCityfinalSize;
    private float mTempStartSize;
    private float mTempfinalSize;

    public ViewBehavior(Context context, AttributeSet attrs) {
        mContext = context;
            TypedArray a =context.obtainStyledAttributes(attrs,R.styleable.ViewBehavior);
            mCityStartSize = a.getDimension(R.styleable.ViewBehavior_city_startSize,30);
            mCityfinalSize = a.getDimension(R.styleable.ViewBehavior_city_finalSize,20);
            mTempStartSize= a.getDimension(R.styleable.ViewBehavior_temp_startSize,100);
            mTempfinalSize = a.getDimension(R.styleable.ViewBehavior_temp_finalSize,50);
            a.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, HeaderView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, HeaderView child, View dependency) {
        shouldInitProperties(child,dependency);

        int maxScroll = ((AppBarLayout) dependency).getTotalScrollRange();
        float percentage = Math.abs(dependency.getY()) / (float) maxScroll;

        float citySize = mCityStartSize - (mCityStartSize - mCityfinalSize) * percentage;
        float tempSize = (mTempStartSize - (mTempStartSize - mTempfinalSize) * percentage);

        child.setAlpha(percentage);
        Log.i("TAG", String.valueOf(mCityStartSize));
        Log.i("TAG", String.valueOf(mCityfinalSize));
        child.city.setTextSize(citySize);
        child.temp.setTextSize(tempSize);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.leftMargin = (int) (mStartMarginLeft - (mStartMarginLeft - mEndMargintLeft) * percentage);
        child.setLayoutParams(lp);

        return true;
    }

    private void shouldInitProperties(HeaderView child, View dependency) {
        if (mStartMarginLeft == 0)
            mStartMarginLeft = (dependency.getWidth() - child.getWidth()) / 2;

        if (mEndMargintLeft == 0)
            mEndMargintLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.header_view_end_margin_left);
    }
}
