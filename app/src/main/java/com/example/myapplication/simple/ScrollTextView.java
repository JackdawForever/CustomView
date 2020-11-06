package com.example.myapplication.simple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.TintTypedArray;


import com.example.myapplication.R;

import java.util.Random;

/**
 * @author: Jackdaw Forever
 * @date: 2020/11/2 13:23
 * @description 简单的走马灯
 */
public class ScrollTextView extends View implements Runnable {
    private final static String TAG = "ScrollTextView";

    private float textX = 0;//文本的x坐标
    private String string;//最终绘制的文本
    private TextPaint paint;//画笔
    private String content = "";
    private Rect rect;//计算文本宽度
    private int textColor = Color.BLACK;//文字颜色,默认黑色
    private float textSize = 12;//文字颜色,默认黑色
    private int contentWidth;//内容的宽度
    private float textHeight;
    private boolean isRoll = false;//是否继续滚动
    private float speed = 1;//移动速度,其实就是每次移动间距
    private Thread thread;

    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        initPaint();
    }


    /**
     * 获取自定义属性
     *
     * @param context
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollTextView, defStyleAttr, 0);
        textColor = a.getColor(R.styleable.ScrollTextView_text_color, textColor);
        textSize = a.getDimensionPixelSize(R.styleable.ScrollTextView_text_size, 18);
        a.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        rect = new Rect();
        paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);//初始化文本画笔
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);//文字颜色值,可以不设定
        paint.setTextSize(dp2px(textSize));//文字大小
    }

    public int dp2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 设置文本内容
     *
     * @param content
     */
    public void setContent(String content) {
        if (TextUtils.isEmpty(content)) {
            Log.d(TAG, "content is empty");
            return;
        }
        //初始化文字位置
        textX = getWidth();
        this.content = content;
        contentWidth = (int) getContentWidth(content);

        this.string = content;
        if (!isRoll) {//如果没有在滚动的话，重新开启线程滚动
            continueRoll();
        }
    }

    /**
     * 继续滚动 这里只是为了开启线程改变文字绘制位置，真的绘制还是在onDraw中
     */
    public void continueRoll() {
        if (!isRoll) {
            if (thread != null) {
                thread.interrupt();

                thread = null;
            }

            isRoll = true;
            thread = new Thread(this);//这里我直接实现了Runnable接口，看run方法
            thread.start();//开启死循环线程让文字动起来
        }
    }

    /**
     * 停止滚动
     */
    public void stopRoll() {
        isRoll = false;
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }

    }


    /**
     * 获取文本内容
     *
     * @param black
     * @return
     */
    private float getContentWidth(String black) {
        if (black == null || black == "") {
            return 0;
        }

        if (rect == null) {
            rect = new Rect();
        }
        paint.getTextBounds(black, 0, black.length(), rect);
        textHeight = getContentHeight();

        return rect.width();
    }

    private float getContentHeight() {

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return Math.abs((fontMetrics.bottom - fontMetrics.top)) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //文本宽度小于等于文本X坐标，也就是说文本已经到头了，重置文本x坐标
        if (contentWidth <= (-textX)) {
            textX = getWidth();
        }
        //把文字画出来
        if (string != null) {
            canvas.drawText(string, textX, getHeight() / 2 + textHeight / 2, paint);
        }
    }

    @Override
    public void run() {
        while (isRoll && !TextUtils.isEmpty(content)) {
            try {
                Thread.sleep(10);
                textX = textX - speed;
                postInvalidate();//每隔10毫秒重绘视图
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
