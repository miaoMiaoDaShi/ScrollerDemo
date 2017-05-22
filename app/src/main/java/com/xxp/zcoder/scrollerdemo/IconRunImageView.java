package com.xxp.zcoder.scrollerdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by Zcoder
 * Email : 1340751953@qq.com
 * Time :  2017/5/22
 * Description :
 */

public class IconRunImageView extends android.support.v7.widget.AppCompatImageView {

    private Scroller mScroller;

    public IconRunImageView(Context context) {
        this(context, null);
    }

    public IconRunImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconRunImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
