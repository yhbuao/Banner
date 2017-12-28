package haibo.bannerdemo.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: yuhaibo
 * @time: 2017/12/28 11:37.
 * projectName: BannerDemo.
 * Description:
 */

public class BannerViewPager extends ViewPager {
    private BannerAdapter mBannerAdapter;

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setAdapter(BannerAdapter adapter) {
        this.mBannerAdapter = adapter;
        //设置ViewPager的Adapter
        setAdapter(new BannerPagerAdapter());
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
