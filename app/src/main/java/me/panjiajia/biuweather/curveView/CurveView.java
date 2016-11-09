package me.panjiajia.biuweather.curveView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.panjiajia.biuweather.R;

/**
 * Created by ningjiaqi on 16/4/28.
 */
public class CurveView extends View {
    private List<Float> mData = new ArrayList<>();//数据
    private List<PointF> mPoints = new ArrayList<>();//坐标
    private List<PointF> mControlPoints = new ArrayList<>();//控制点坐标
    private Path mPath;
    private Paint mPaint;
    private Paint mPointPaint;
    private CurveCalculator mCal;
    private int mDotRadius = 5;
    private int textAlpha = 255;
    private float textSize;


    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a =context.obtainStyledAttributes(attrs, R.styleable.CurveView);
        textSize = a.getDimension(R.styleable.CurveView_textSize,50);
        a.recycle();
        init();
    }

    public CurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xffffffff);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setStrokeWidth(3);
        mPointPaint.setColor(0xffffffff);

        mPoints = new ArrayList<>();
        mCal = new CurveCalculator();
    }

    public void setData(List<Float> data){
        mData = data;
        mPoints = null;
        invalidate();
    }

    public void setPoints(List<PointF> points,float alpha) {
        mPoints = points;
        if (alpha < 0.5) {
            textAlpha = (int) ((2*(1 - alpha)-1) * 255);
        }
        initControlPoints(mPoints);

        invalidate();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        if (mData != null && mData.size() >2) {
            if (mPoints == null) {
                mPoints = mCal.getDisplayPoint(mData, getWidth(), getHeight());
                initControlPoints(mPoints);
            }
            mPath.reset();
            mPath.moveTo(0,getHeight());
            mPath.lineTo(0,mPoints.get(0).y);

            for (int i = 0; i < mPoints.size() -1; i++){
                if (i == 0){// 第一条为二阶贝塞尔
                    mPath.moveTo(mPoints.get(i).x, mPoints.get(i).y);// 起点
                            mPath.quadTo(mControlPoints.get(i).x, mControlPoints.get(i).y,// 控制点
                            mPoints.get(i + 1).x,mPoints.get(i + 1).y);
                }else if(i < mPoints.size() - 2){// 三阶贝塞尔
                    mPath.cubicTo(mControlPoints.get(2*i-1).x,mControlPoints.get(2*i-1).y,// 控制点
                            mControlPoints.get(2*i).x,mControlPoints.get(2*i).y,// 控制点
                            mPoints.get(i+1).x,mPoints.get(i+1).y);// 终点
                }else if(i == mPoints.size() - 2){// 最后一条为二阶贝塞尔
                    mPath.moveTo(mPoints.get(i).x, mPoints.get(i).y);// 起点
                    mPath.quadTo(mControlPoints.get(mControlPoints.size()-1).x,mControlPoints.get(mControlPoints.size()-1).y,
                            mPoints.get(i+1).x,mPoints.get(i+1).y);// 终点
                }
            }

            mPath.lineTo(getWidth(),getHeight());
            mPath.lineTo(0,getHeight());
            mPath.lineTo(0,mPoints.get(0).y);

            canvas.drawPath(mPath,mPaint);


            for (int i = 1; i < mPoints.size()-1; i++) {
                    PointF p = mPoints.get(i);
                    mPointPaint.setColor(0xffffffff);
                    mPointPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(p.x, p.y, mDotRadius, mPointPaint);

                    mPointPaint.setColor(0xff217bb4);
                    mPointPaint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(p.x,p.y,mDotRadius-3,mPointPaint);


                mPointPaint.setTextSize(textSize);
                mPointPaint.setColor(0xffffffff);
                mPointPaint.setAlpha(textAlpha);
                canvas.drawText("16:00",p.x-textSize,p.y-textSize * 3,mPointPaint);

                canvas.drawText("多云",p.x-textSize,p.y-textSize * 2,mPointPaint);

                canvas.drawText("12°", (p.x-textSize),p.y-textSize,mPointPaint);
            }
        }
    }

    private List<PointF> initMidPoints(List<PointF> points) {
        List<PointF> mMidPoints = new ArrayList<PointF>();
        for (int i = 0; i < points.size(); i++) {
            PointF midPoint = null;
            if (i == points.size()-1){
                return mMidPoints;
            }else {
                midPoint = new PointF((points.get(i).x + points.get(i + 1).x)/2, (points.get(i).y + points.get(i + 1).y)/2);
            }
            mMidPoints.add(midPoint);
        }
        return mMidPoints;
    }

    private List<PointF> initMidMidPoints(List<PointF> midPoints){
        List<PointF> mMidMidPoints = new ArrayList<PointF>();
        for (int i = 0; i < midPoints.size(); i++) {
            PointF midMidPoint = null;
            if (i == midPoints.size()-1){
                return mMidMidPoints;
            }else {
                midMidPoint = new PointF((midPoints.get(i).x + midPoints.get(i + 1).x)/2, (midPoints.get(i).y + midPoints.get(i + 1).y)/2);
            }
            mMidMidPoints.add(midMidPoint);
        }
        return mMidMidPoints;
    }

    private void initControlPoints(List<PointF> points){
        mControlPoints.clear();
        List<PointF> midPoints = initMidPoints(points);
        List<PointF> midMidPoints = initMidMidPoints(midPoints);
        for (int i = 0; i < points.size(); i ++){
            if (i ==0 || i == points.size()-1){
                continue;
            }else{
                PointF before = new PointF();
                PointF after = new PointF();
                before.x = points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i - 1).x;
                before.y = points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i - 1).y;
                after.x = points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i).x;
                after.y = points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i).y;
                mControlPoints.add(before);
                mControlPoints.add(after);
            }
        }
    }

}
