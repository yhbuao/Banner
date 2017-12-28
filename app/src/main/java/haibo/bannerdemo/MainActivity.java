package haibo.bannerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import haibo.bannerdemo.banner.BannerAdapter;
import haibo.bannerdemo.banner.BannerViewPager;

/**
 * @author User
 */
public class MainActivity extends AppCompatActivity {

    private BannerViewPager mBannerViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBannerViewPager = ((BannerViewPager) findViewById(R.id.bannerViewPager));

        mBannerViewPager.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setBackgroundResource(R.mipmap.b);
                return imageView;
            }
        });
    }
}
