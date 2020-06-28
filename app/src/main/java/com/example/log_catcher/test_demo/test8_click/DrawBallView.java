package com.example.log_catcher.test_demo.test8_click;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.log_catcher.util.LogHelper;

import java.security.PublicKey;
import java.util.jar.Attributes;

public class DrawBallView extends View {
    public float currentX = 60;
    public float currentY = 60;
    public float radius = 15;

    //创建笔画
    Paint p = new Paint();
    public DrawBallView(Context context){
       super(context);
    }
    public DrawBallView(Context context, AttributeSet set){
        super(context,set);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置笔画颜色
        p.setColor(Color.rgb(0xFF,00,00));

        //绘制一个小圆球
        canvas.drawCircle(currentX, currentY, radius , p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //更新当前触点坐标
        currentX = event.getX();
        currentY =  event.getY();
        LogHelper.getInstance().w("当前坐标:X="+currentX+",Y="+currentY);
        //通知重绘,触发onDraw函数
        invalidate();

        return true;
    }
}
