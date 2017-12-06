package com.shengmingji.viewsliding;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;


public class SlidingView extends View {
    int lastX;
    int lastY;
    private String TAG = "TAG";
    private Scroller scroller;

    public SlidingView(Context context) {
        super(context);
    }

    public SlidingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    public SlidingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    //缓慢移动到指定位置
    public   void smoothScrollTo(int x,int y){
        int scrollX = getScrollX();
        int scrollY = getScrollY();

        int offsetX = scrollX - x;
        int offsetY = scrollY - y;
        scroller.startScroll(scrollX,scrollY,offsetX,offsetY,2000);
        invalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                Log.i(TAG, "ACTION_DOWN: >>>>lastX " + lastX + " <<<>>> lastY " + lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离
                int offSetX = x - lastX;
                int offSetY = y - lastY;
                Log.i(TAG, "ACTION_MOVE: >>>>lastX " + lastX + " <<<>>> lastY " + lastY);
                Log.i(TAG, "ACTION_MOVE: >>>>offSetX " + offSetX + " <<<>>> offSetY " + offSetY);
                //调用layout方法来重新放置它的位置
                layout(getLeft() + offSetX, getTop() + offSetY,
                        getRight() + offSetX, getBottom() + offSetY);
                break;
            default:
                break;
        }
        return true;
    }
}
