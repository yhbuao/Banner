package haibo.bannerdemo.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * @author: yuhaibo
 * @time: 2017/12/28 11:37.
 * projectName: BannerDemo.
 * Description:
 */

public class BannerViewPager extends ViewPager {
    private static final int SCROLL_MSG = 0X0011;
    private BannerAdapter mBannerAdapter;
    //默认的滚动轮询时间
    private int mCutDownTime = 5000;
    //改变viewpager切换速率 自定义
    private BannerScroller mScroller;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setCurrentItem(getCurrentItem() + 1);
            //循环执行
            startRoll();
        }
    };

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //改变viewpager切换的速率
        //duration持续时间 是viewpager源码中局部变量通过反射拿到这个
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            //设置参数 第一个Object代表设置的属性在那个类 第二个Object代表要设置的值
            mScroller = new BannerScroller(context);
            //设置强制改变private属性
            field.setAccessible(true);
            field.set(this, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义BannerAdapter
     *
     * @param adapter
     */
    public void setAdapter(BannerAdapter adapter) {
        this.mBannerAdapter = adapter;
        //设置ViewPager的Adapter
        setAdapter(new BannerPagerAdapter());
    }

    /**
     * 开启滚动
     */
    public void startRoll() {
        //清除消息
        mHandler.removeMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDownTime);
    }

    /**
     * 销毁handler ,防止内存泄露
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(SCROLL_MSG);
        mHandler = null;
    }

    /**
     * 设置页面切换的时间
     *
     * @param scrollerDuration
     */
    public void setBannerScroller(int scrollerDuration) {
        mScroller.setScrollerDuration(scrollerDuration);
    }

    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //返回int的最大值
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 创建item的实例化
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View bannerItemView = mBannerAdapter.getView(position);
            container.addView(bannerItemView);
            return bannerItemView;
        }

        /**
         * 销毁item
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }
    }
}
