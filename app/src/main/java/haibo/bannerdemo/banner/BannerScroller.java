package haibo.bannerdemo.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author: yuhaibo
 * @time: 2018/2/8 16:59.
 * projectName: BannerDemo.
 * Description: 自定义改变ViewPager切换速率的类
 */

public class BannerScroller extends Scroller {
    //动画持续的时间
    private int mScrollerDuration = 950;

    public void setScrollerDuration(int scrollerDuration) {
        this.mScrollerDuration = scrollerDuration;
    }

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollerDuration);
    }
}
