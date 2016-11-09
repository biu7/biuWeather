package me.panjiajia.biuweather.headerView;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.panjiajia.biuweather.R;


public class HeaderView extends LinearLayout {

    TextView city;

    TextView weather;

    TextView temp;

    TextView aqi;

    TextView yujing;


    ImageView iv_leaf;


    public HeaderView(Context context) {
        super(context);
    }


    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        city = (TextView) findViewById(R.id.tv_city_fragment);
        weather = (TextView) findViewById(R.id.tv_weather_fragment);
        aqi = (TextView) findViewById(R.id.tv_aqi_fragment);
        iv_leaf = (ImageView) findViewById(R.id.aqi_leaf);
        temp = (TextView) findViewById(R.id.tv_temp_fragment);
        yujing = (TextView) findViewById(R.id.tv_yujing_fragment);
    }
    public void setAlpha(float alpha){
        alpha = 2 * (1 - alpha) - 1;

            weather.setAlpha(alpha);
            aqi.setAlpha(alpha);
            iv_leaf.setAlpha(alpha);
            yujing.setAlpha(alpha);


    }




}
