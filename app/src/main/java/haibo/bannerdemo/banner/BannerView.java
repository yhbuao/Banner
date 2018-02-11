package haibo.bannerdemo.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import haibo.bannerdemo.R;

/**
 * @author: yuhaibo
 * @time: 2018/2/9 14:24.
 * projectName: Banner.
 * Description: 自定义bannerView 相当于把BannerViewPager包装了一层
 */

public class BannerView extends RelativeLayout {
    private Context mContext;
    //轮播的BannerViewPager
    private BannerViewPager bannerVp;
    //自定义轮播的描述
    private TextView mBannerDescTv;
    //自定义轮播的指示器容器
    private LinearLayout mDotContainer;
    //自定义bannerView 的adapter
    private BannerAdapter mBannerAdapter;
    //选中点的Drawable
    private Drawable mDotIndicatorFocus;
    //未选中的Drawable
    private Drawable mDotIndicatorNormal;
    //当前的位置
    private int mCurrentPosition;
    //自定义属性 点的位置
    private int mDotGravity = -1;
    //自定义属性 点的大小
    private int mDotSize = 8;
    //自定义属性 点的间距
    private int mDotDistance = 10;
    //底部的view
    private View mBannerBottomViewBg;
    //底部容器的颜色默认透明
    private int mBottomColor = Color.TRANSPARENT;
    //宽高比例
    private float mWidthProportion, mHeightProportion;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //把布局加载到这个View中
        inflate(context, R.layout.ui_banner_layout, this);
        initAttribute(attrs);
        initView();
    }

    /**
     * 初始化自定义属性
     *
     * @param attrs
     */
    private void initAttribute(AttributeSet attrs) {
        TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mDotGravity = array.getInt(R.styleable.BannerView_dotGravity, mDotGravity);
        mDotIndicatorFocus = array.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        if (mDotIndicatorFocus == null) {
            mDotIndicatorFocus = new ColorDrawable(Color.RED);
        }
        mDotIndicatorNormal = array.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if (mDotIndicatorNormal == null) {
            mDotIndicatorNormal = new ColorDrawable(Color.WHITE);
        }
        mDotSize = (int) array.getDimension(R.styleable.BannerView_dotSize, dip2px(mDotSize));
        mDotDistance = (int) array.getDimension(R.styleable.BannerView_dotDistance, dip2px(mDotDistance));
        //获取底部的颜色
        mBottomColor = array.getColor(R.styleable.BannerView_bottomColor, mBottomColor);
        //宽高比例
        mWidthProportion = array.getFloat(R.styleable.BannerView_widthProportion, mWidthProportion);
        mHeightProportion = array.getFloat(R.styleable.BannerView_heightProportion, mHeightProportion);
        array.recycle();
    }

    /**
     * 初始化View
     */
    private void initView() {
        bannerVp = (BannerViewPager) findViewById(R.id.banner_vp);
        mBannerDescTv = (TextView) findViewById(R.id.banner_desc_tv);
        mDotContainer = ((LinearLayout) findViewById(R.id.dot_container));
        mBannerBottomViewBg = findViewById(R.id.banner_bottom_view);
        mBannerBottomViewBg.setBackgroundColor(mBottomColor);
    }

    /**
     * 设置Adapter
     *
     * @param adapter
     */
    public void setAdapter(BannerAdapter adapter) {
        mBannerAdapter = adapter;
        bannerVp.setAdapter(adapter);
        //初始化点的指示器
        initDotIndicator();
        //监听viewPager滑动的监听
        bannerVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //监听当前选中的位置
                pageSelect(position);
            }
        });
        //默认的时候取第一条描述
        mBannerDescTv.setText(mBannerAdapter.getBannerDesc(0));
        //动态计算宽高
        if (mWidthProportion == 0 || mHeightProportion == 0) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                int width = getMeasuredWidth();
                int height = (int) (width * mHeightProportion / mWidthProportion);
                //指定宽高
                getLayoutParams().height = height;
            }
        });
    }

    /**
     * 页面切换的回调
     *
     * @param position
     */
    private void pageSelect(int position) {
        //选中位置和未选中的状态变化
        DotIndicatorView dotIndicatorView = (DotIndicatorView) mDotContainer.getChildAt(mCurrentPosition);
        dotIndicatorView.setDrawable(mDotIndicatorNormal);

        mCurrentPosition = position % mBannerAdapter.getCount();
        DotIndicatorView currentIndicatorView = (DotIndicatorView) mDotContainer.getChildAt(mCurrentPosition);
        currentIndicatorView.setDrawable(mDotIndicatorFocus);

        //设置广告描述的状态变化
        String bannerDesc = mBannerAdapter.getBannerDesc(mCurrentPosition);
        mBannerDescTv.setText(bannerDesc);
    }

    /**
     * 开启滚动
     */
    public void startRoll() {
        bannerVp.startRoll();
    }

    /**
     * 滚动轮询的时间
     *
     * @param delayTime
     */
    public void setDelayTime(int delayTime) {
        bannerVp.setDelayTime(delayTime);
    }

    /**
     * 设置页面切换的时间
     *
     * @param scrollerDuration
     */
    public void setBannerScroller(int scrollerDuration) {
        bannerVp.setBannerScroller(scrollerDuration);
    }

    public void setOnItemClickListener(BannerViewPager.BannerItemClickListener listener) {
        bannerVp.setOnItemClickListener(listener);
    }

    /**
     * 初始化点的指示器
     */
    private void initDotIndicator() {
        //获取广告位的数量
        int count = mBannerAdapter.getCount();
        //点的位置居右
        mDotContainer.setGravity(getDotGravity());
        for (int i = 0; i < count; i++) {
            //添加点的指示器
            DotIndicatorView dotIndicatorView = new DotIndicatorView(mContext);
            //设置大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize, mDotSize);
            //设置左右的间距
            params.leftMargin = mDotDistance;
            dotIndicatorView.setLayoutParams(params);
            if (i == 0) {
                //选中
                dotIndicatorView.setDrawable(mDotIndicatorFocus);
            } else {
                //没有选中
                dotIndicatorView.setDrawable(mDotIndicatorNormal);
            }
            //添加到点的指示器
            mDotContainer.addView(dotIndicatorView);
        }
    }

    /**
     * 获取点的位置
     *
     * @return
     */
    private int getDotGravity() {
        switch (mDotGravity) {
            case 0:
                return Gravity.CENTER;
            case -1:
                return Gravity.LEFT;
            case 1:
                return Gravity.RIGHT;
        }
        return Gravity.LEFT;
    }

    /**
     * 把dip转成px
     *
     * @param dip
     * @return
     */
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }
}
