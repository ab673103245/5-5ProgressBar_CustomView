package qianfeng.a5_5progressbar_customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class MyProgressBar extends View {

    private Paint cyclePaint;

    private Paint sweepPaint;

    private int startAngle = -90;

    private int sweepAngle = 0;

    private int padding = 20;

    private static final int DEFAULTWIDTH = 200;
    private static final int DEFAULTHEIGHT = 200;

    public MyProgressBar(Context context) {
        this(context, null);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        cyclePaint = new Paint();
        cyclePaint.setAntiAlias(true);
        cyclePaint.setColor(Color.BLACK);
        cyclePaint.setStrokeWidth(6);
        cyclePaint.setStyle(Paint.Style.STROKE);


        sweepPaint = new Paint();
        sweepPaint.setAntiAlias(true);
        // 不指定绘制的Style，就是Style.FILL
        sweepPaint.setColor(Color.RED);

    }

    /** 测量控件的宽高
     * widthMeasureSpec 是一个32位的int类型数据，其中高2位表示测量模式，低30位表示测量值
     *
     *  MeasureSpec.EXACTLY:当在xml中定义的宽高为match_parent以及是给定一个具体的dp时，就会是这种测量模式
     *   MeasureSpec.AT_MOST; 当在xml中定义的宽高为wrap_content时，就是这种测量模式
         MeasureSpec.UNSPECIFIED: 表示子控件可以无限大.
                                    当父容器是ScrollView这些可以根据内容来拉长的父容器，子控件在里面就是无限大，而不受屏幕宽高影响

     * @param widthMeasureSpec 宽度的测量规格
     * @param heightMeasureSpec 高度的测量规格
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode)
        {
            case MeasureSpec.AT_MOST: // 如果在xml中设置为wrap_content
            case MeasureSpec.UNSPECIFIED: // 如果子控件可以跟随父容器无限大,那么这两种情况的处理方式是一样的,就是给它一个具体的dp值
                widthSize = DEFAULTWIDTH;
                break;
            case MeasureSpec.EXACTLY:  // 这种是精确测量模式，里面一般是指定了具体的dp值的，match_parent也是有具体值的，因为屏幕有具体值，所以就不用你作处理
                break;
        }

        switch (heightMode) // 高度和宽度上面的处理情况是一样的
        {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = DEFAULTHEIGHT;
                break;
            case MeasureSpec.EXACTLY:
                break;
        }
        // 现在问题是，如果宽度值和高度值不一样时，怎么办？有可能宽度模式是精确(exactly)的值是300dp,而高度是wrap_content，即赋给它一个200dp，两者显然不一样，那怎么画圆呢？
        widthSize = heightSize = Math.min(widthSize,heightSize); // widthSize < heightSize ? widthSize : heightSize;高度和宽度中，取最小的那个值
        // 这是设定最终确定的值
        setMeasuredDimension(widthSize,heightSize); // 这是最终的确定的值，会传递到onDraw的getMeasureWidth()方法中
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int radius = getMeasuredWidth() / 2;

        canvas.drawCircle(radius, radius, radius - 3, cyclePaint);

        // 构造矩形的这四个参数,分别是距离左边的边距，距离上面的边距，距离右边的边距，距离底部的边距，java代码里面的数字单位都是px
        RectF rectF = new RectF(padding, padding, getMeasuredWidth() - padding, getMeasuredHeight() - padding);// 指定上左，上右，下左，下右四条线即可确定一个矩形，方法的内部代码就是这样实现
        //1.扇形所在的矩形
        //2.开始绘制的度数
        //3.绘制的度数
        //4.绘制时扇形和矩形的中心是否一致
        //5.绘制的画笔
        canvas.drawArc(rectF, startAngle, sweepAngle, true, sweepPaint);
        sweepAngle = sweepAngle > 359 ? 0 : ++sweepAngle;

        invalidate();// 立马重新绘制一遍

    }
}
