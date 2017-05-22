package com.xxp.zcoder.scrollerdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Zcoder
 * Email : 1340751953@qq.com
 * Time :  2017/5/22
 * Description :
 */

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
        mScroller = new Scroller(context);

    }

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
