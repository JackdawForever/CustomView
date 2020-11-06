package com.example.myapplication.simple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.Random;

/**
 * @author: Jackdaw Forever
 * @date: 2020/11/2 13:23
 * @description
 */
public class ScrollTextView extends AppCompatTextView {
    private float textX = 0;

    private Paint paint = new Paint();
    private MyThead thead = null;
    private String content = "";

    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        content = this.getText().toString();
        float size = this.getTextSize();
        if (size <= 0) {
            size = 30;
        }
        paint.setTextSize(size);//初始化文字大小
        canvas.drawText("我是文字", textX, 500, paint);//画出文字的开始位置；
        if (thead == null) {
            thead = new MyThead();
            thead.start();
        }
    }

    private boolean running = true;

    private class MyThead extends Thread {
        private Random random = new Random();

        @Override
        public void run() {
            super.run();
            while (running) {
                //文字跑马灯效果啊
                textX = textX + 3;
                if (textX > getWidth()) {
                    textX = 0 - paint.measureText(content);//截取文字的长度
                }
                paint.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));// 设置颜色 第一个参数：透明度
                postInvalidate();//重新进行绘制

                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        //离开屏幕的操作
        running = false;
        super.onDetachedFromWindow();
    }
}
