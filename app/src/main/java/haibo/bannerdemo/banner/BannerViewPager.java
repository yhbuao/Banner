package haibo.bannerdemo.banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yuhaibo
 * @time: 2017/12/28 11:37.
 * projectName: BannerDemo.
 * Description: BannerViewPager
 */

public class BannerViewPager extends ViewPager {
    private static final int SCROLL_MSG = 0X0011;
    private BannerAdapter mBannerAdapter;
    //默认的滚动轮询时间
    private int delayTime = 5000;
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
    //内存优化 界面复用
    private List<View> mConvertViews;
    private Activity mActivity;

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mActivity = (Activity) context;
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
        mConvertViews = new ArrayList<>();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (true) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startRoll();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopRoll();
            }
        }
        return super.dispatchTouchEvent(ev);
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
        //管理Activity的生命周期 注册生命周期
        mActivity.getApplication().registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    /**
     * 开启滚动
     */
    public void startRoll() {
        //清除消息
        mHandler.removeMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG, delayTime);
    }

    /**
     * 停止轮播
     */
    public void stopRoll() {
        //清除消息
        mHandler.removeMessages(SCROLL_MSG);
    }

    /**
     * 销毁handler ,防止内存泄露
     */
    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeMessages(SCROLL_MSG);
        mHandler = null;
        //取消绑定注册
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(mLifecycleCallbacks);
        super.onDetachedFromWindow();
    }

    /**
     * 获取复用的界面
     *
     * @return
     */
    private View getConvertView() {
        for (int i = 0; i < mConvertViews.size(); i++) {
            //获取没有添加在viewpager里面的View
            if (mConvertViews.get(i).getParent() == null) {
                return mConvertViews.get(i);
            }
        }
        return null;
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
        public Object instantiateItem(ViewGroup container, final int position) {
            View bannerItemView = mBannerAdapter.getView(position % mBannerAdapter.getCount(), getConvertView());
            container.addView(bannerItemView);
            //item的点击事件
            bannerItemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //回调点击监听
                    if (mListener != null) {
                        mListener.itemClick(position % mBannerAdapter.getCount());
                    }
                }
            });
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
            mConvertViews.add((View) object);
        }
    }

    //item的点击事件
    private BannerItemClickListener mListener;

    public void setOnItemClickListener(BannerItemClickListener listener) {
        this.mListener = listener;
    }

    //接口回调思想
    public interface BannerItemClickListener {
        void itemClick(int position);
    }

    //管理Activity的生命周期
    private Application.ActivityLifecycleCallbacks mLifecycleCallbacks = new DefaultActivityLifecycleCallbacks() {
        @Override
        public void onActivityResumed(Activity activity) {
            super.onActivityResumed(activity);
            //判断是不是监听的当前Activity的生命周期
            if (activity == mActivity) {
                //开启轮播
                startRoll();
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            super.onActivityPaused(activity);
            if (activity == mActivity) {
                //停止轮播
                stopRoll();
            }
        }
    };

}
