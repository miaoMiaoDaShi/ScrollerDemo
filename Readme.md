# 一.WHAT
在Android开发中有多种方式实现View的滑动，常见的有三种如下：
1.不断地修改View的LayoutParams
2.采用动画向View施加位移效果
3.调用View的scrollTo( )、scrollBy( )
第三种方式大家也许会比较陌生,到这里顺变讲一下.
说得再多也没用,贴源码最简单粗暴.

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

    public void scrollBy(int x, int y) {
        scrollTo(mScrollX + x, mScrollY + y);
    }

     public void invalidate(int l, int t, int r, int b) {
        final int scrollX = mScrollX;
        final int scrollY = mScrollY;
        invalidateInternal(l - scrollX, t - scrollY, r - scrollX, b - scrollY, true, false);
    }


从源码中我们可以get到3个信息,
1.scrollTo() : 当我们进入此方法时,会首先进行if判断,在传入的x和y其中有一项不等成立,我们才可向下执行.这也就是为什么该方法只能使视图内容相对于初始的位置滚动某段距离
2.scrollBy() : 其内部其实还是调用了scrollTo,注意看其中传入的参数,每次调用会在目标滚动距离上进行累加.解惑:该方法是让视图内容相对于当前的位置滚动某段距离
3.每次调用scrollTo()都会调用postInvalidateOnAnimation()引起view的重绘,在invalidate中,它会去以减法方式重新计算左上右下,这也就解惑了,为什么我们传入正值往左偏,负值往右偏.
###### 注意: 偏移的是视图内容!!!!!!!!!,比如TextView中的文字就是该view的内容,ViewGroup(布局)中的View就是该ViewGroup的内容.简单一点说就是谁要滚,找他爸.

知道了这么多,好了,进入正题,view中的scrollTo并不能满足我们的需求,用用这两个方法完成的滚动效果是跳跃式的，没有任何平滑滚动的效果.那么我们就需要我们的滚动工具类了---Scroller

也许你对Scroller并不熟悉,但是你可能在每天的开发中都接触到了使用此工具类的控件,如ViewPager、LIstView、RecyclerView等等..
Scroller是安卓中一个为实现滚动效果而生的工具类。
# 二.HOW
在这里我们偏移滚动当做一次跑步吧
首先我们需要知道几个方法

     /**
     * 跑步开始前,总要有个起始位置和目的位置吧,
     * 哦,对了,还差完成时间,时间短我们就跑快点,时间长,我们就慢慢跑
     * 这个方法吧...主要就是进行了一些初始化的赋值操作,
     * @param 开始位置x
     * @param 开始位置y
     * @param 目标位置x
     * @param 目标位置y
     * @param 完成总时间
     */
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        mMode = SCROLL_MODE;
        mFinished = false;
        mDuration = duration;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mStartX = startX;
        mStartY = startY;
        mFinalX = startX + dx;
        mFinalY = startY + dy;
        mDeltaX = dx;
        mDeltaY = dy;
        mDurationReciprocal = 1.0f / (float) mDuration;
    }
      //没错它就是一个空方法,在绘制的时候会 调用这个方法
     public void computeScroll() {}

来一demo演示一下吧.
布局部分activity_main.xml

    <?xml version="1.0" encoding="utf-8"?>
    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.xxp.zcoder.scrollerdemo.MainActivity">
    <com.xxp.zcoder.scrollerdemo.Mylayout
        android:id="@+id/myLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent" >

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher"/>
    </com.xxp.zcoder.scrollerdemo.Mylayout>
    <EditText
        android:text="1000"
        android:id="@+id/txtDuration"
        android:layout_above="@+id/btnRun"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    <Button
        android:id="@+id/btnRun"
        android:text="奔跑吧"
        android:textAllCaps="false"
        android:layout_alignParentBottom="true"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    </RelativeLayout>

自定义的viewgroup文件,为了方便直接继承了

    public class Mylayout extends LinearLayout {

    private Scroller mScroller;

    public Mylayout(Context context) {
        this(context, null);
    }

    public Mylayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Mylayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获得Scroller实例
        mScroller = new Scroller(context);

    }

    //给外部调用的方法
    public void scroll(int startX, int startY, int dx, int dy, int duration) {
        mScroller.startScroll(startX, startY, dx, dy, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}

MainActivity.Java中的关键代码

     @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myLayout:
                mDuration = Integer.parseInt(txtDuration.getText().toString());
                myLayout.scroll(0, 0, -500, -500, mDuration);
                break;
        }
    }

![GIF.gif](http://upload-images.jianshu.io/upload_images/3901809-be8a6508f2207edd.gif?imageMogr2/auto-orient/strip)


demo地址
[https://github.com/miaoMiaoDaShi/ScrollerDemo](https://github.com/miaoMiaoDaShi/ScrollerDemo "demo地址")