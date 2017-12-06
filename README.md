##View滑动简介

现在Android设备中，滑动可以说是无处不在，`Activity`转场，侧滑菜单，下拉刷新，上拉加载，还有手机屏幕较小，为了给客户展示更多的内容，就需要使用滑动来隐藏和展示一些内容。所有学习View的滑动是非常有必要的，通过几种方式简单展示一下。

##View滑动实现

####1.layout()方式

View的摆放只需要确定**上下左右**四个点坐标即可确定View的位置，我们可以通过手指滑动的坐标来计算偏移量，然后分别与**View**的自身坐标相加即可实现
```
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();                         //获取手指点击的横纵坐标
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:       //记录下手指DOWN事件的横纵坐标
                lastX = x; 
                lastY = y;
                Log.i(TAG, "ACTION_DOWN: >>>>lastX " + lastX + " <<<>>> lastY " + lastY);
                break;
            case MotionEvent.ACTION_MOVE:
               
                int offSetX = x - lastX;                           //计算偏移量的距离
                int offSetY = y - lastY;
                Log.i(TAG, "ACTION_MOVE: >>>>lastX " + lastX + " <<<>>> lastY " + lastY);
                Log.i(TAG, "ACTION_MOVE: >>>>offSetX " + offSetX + " <<<>>> offSetY " + offSetY);
                layout(getLeft() + offSetX, getTop() + offSetY,                     //重新摆放位置
                        getRight() + offSetX, getBottom() + offSetY);
                break;
            default:
                break;
        }
        return true;
    }
```

---

####使用scrollTo/scrollBy

看下源码以及解释
```
    /**
     * Set the scrolled position of your view. This will cause a call to
     * {@link #onScrollChanged(int, int, int, int)} and the view will be
     * invalidated.
     * @param x the x position to scroll to
     * @param y the y position to scroll to
     */
    public void scrollTo(int x, int y) {
        if (mScrollX != x || mScrollY != y) {
            int oldX = mScrollX;
            int oldY = mScrollY;
            mScrollX = x;
            mScrollY = y;
            invalidateParentCaches();
            onScrollChanged(mScrollX, mScrollY, oldX, oldY);
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }

    /**
     * Move the scrolled position of your view. This will cause a call to
     * {@link #onScrollChanged(int, int, int, int)} and the view will be
     * invalidated.
     * @param x the amount of pixels to scroll by horizontally
     * @param y the amount of pixels to scroll by vertically
     */
    public void scrollBy(int x, int y) {
        scrollTo(mScrollX + x, mScrollY + y);
    }
```
- **scrollTo**:基于所传参数的绝对滑动，方法注释**Set the scrolled position of your view**，设置视图到滚动位置
- **scrollBy**:基于所传参数的相对滑动，方法注释**Move the scrolled position of your view**，移动视图到滚动位置

![Animation.gif](http://upload-images.jianshu.io/upload_images/4029647-c4858fcc172b3ef2.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

通过图片应该能够理解二者的区别了。注意给View设置以上两个方法，移动的不是View，而是View的内容。

---

####Scroller

弹性滑动，看到上图的效果，这个过程是瞬间完成的，通过Scroller在实现过渡的效果，这个过程不是瞬间完成的，是在一段时间间隔内，下面看下具体实现，
```
    public SlidingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }

```

重写`computeScroll()`方法，系统会在绘制`View`的时候在`draw()`方法中调用该方法，我们调用父类的`scrollTo()`方法并通过Scroller来不断获取当前的滚动值，每滑动一小段距离我们就调用 `postInvalidate()`方法不断的进行重绘，重绘就会调用computeScroll()方法，这样我们就通过不断的移动一个小的距离并连贯起来就实现了平滑移动的效果：
![Scroller.gif](http://upload-images.jianshu.io/upload_images/4029647-b0f282a4c72fb70f.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---

####动画

采用动画也可以完成View的滑动，主要操作VIew的`translationX`和`translationY`属性，既可以使用补间动画也可采用属性动画，看下具体实现
在res目录新建anim文件夹并创建translate.xml：
```
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:fromXDelta="0"
        android:fromYDelta="0"
        android:toXDelta="400"
        android:toYDelta="400" />
</set>
```

属性动画实现

```
ObjectAnimator.ofFloat(slidingView,"translationX",0,400)
                .setDuration(2000).start();
        ObjectAnimator.ofFloat(slidingView,"translationY",0,400)
                .setDuration(2000).start();
```
![Property Animation.gif](http://upload-images.jianshu.io/upload_images/4029647-6aca25ac250a2e50.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---

####改变布局参数LayoutParams

实现View的滑动还可以通过改变布局的参数来说实现，即改变LayoutParams

```
    public void layoutParams(View view){
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) slidingView.getLayoutParams();
        layoutParams.leftMargin += 150;
        layoutParams.topMargin += 150;
        slidingView.setLayoutParams(layoutParams);
    }
```

![layoutParams.gif](http://upload-images.jianshu.io/upload_images/4029647-63c116edd41b02a0.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
