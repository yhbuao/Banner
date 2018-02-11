package haibo.bannerdemo.banner;

import android.view.View;

/**
 * @author: yuhaibo
 * @time: 2017/12/28 11:51.
 * projectName: BannerDemo.
 * Description:
 */

public abstract class BannerAdapter {
    /**
     * 根据position来获取ViewPager中的子view
     *
     * @param position
     * @return
     */
    public abstract View getView(int position,View convertView);

    /**
     * 获取轮播的数量
     */
    public abstract int getCount();

    /**
     * 根据位置获取广告的描述
     *
     * @param position
     * @return
     */
    public String getBannerDesc(int position) {
        return "";
    }
}
