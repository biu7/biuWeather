package me.panjiajia.biuweather;

import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewTreeObserver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.panjiajia.biuweather.headerView.HeaderView;
import me.panjiajia.biuweather.curveView.CurveCalculator;
import me.panjiajia.biuweather.curveView.CurveView;

public class MainActivity extends AppCompatActivity{

    private AppBarLayout appBarLayout;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Toolbar toolbar;

    private HeaderView floatHeaderView;

    private List<Float> curveDatas;

    private CurveView curveView;

    private ViewTreeObserver observer;

    private NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        collapsingToolbarLayout.setTitle(" ");


        curveView.setData(curveDatas);

        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float y = curveView.getPivotY();
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                CurveCalculator calculator = new CurveCalculator();

                float progress = (float) Math.abs(i) / (appBarLayout.getHeight() - toolbar.getHeight() - getStatusBarHeight());
                int mCurveViewHeight = curveView.getHeight();
                int mCurveViewWith = curveView.getWidth();
                List<PointF> pointFs = calculator.getDisplayPoint(curveDatas,mCurveViewWith,mCurveViewHeight,progress);
                curveView.setPoints(pointFs,progress);
            }
        });

    }

    private void initView() {

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        floatHeaderView = (HeaderView) findViewById(R.id.float_header_view);

        curveView = (CurveView) findViewById(R.id.curve);

        observer = curveView.getViewTreeObserver();

        nestedScrollView = (NestedScrollView) findViewById(R.id.scroll);


        curveDatas = new ArrayList<>();
        curveDatas.add(15f);
        curveDatas.add(22f);
        curveDatas.add(24f);
        curveDatas.add(23f);
        curveDatas.add(19f);
        curveDatas.add(17f);
        curveDatas.add(14f);
        curveDatas.add(15f);
    }

    protected int getStatusBarHeight(){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }


}
