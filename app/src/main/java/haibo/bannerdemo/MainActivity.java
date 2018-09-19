package haibo.bannerdemo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import haibo.library.banner.BannerAdapter;
import haibo.library.banner.BannerView;
import haibo.library.banner.BannerViewPager;

/**
 * @author User
 */
public class MainActivity extends AppCompatActivity {
    private BannerView mBannerView;
    private EditText mEditText;
    private Button mButton;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBannerView = ((BannerView) findViewById(R.id.bannerView));
        mEditText = ((EditText) findViewById(R.id.ed_text));
        mButton = ((Button) findViewById(R.id.bt));
        //获取asset目录下的资源文件 解析本地的json文件
        String assetsData = getAssetsData("banner.json");
        Gson gson = new Gson();
        final BannerBean bannerBean = gson.fromJson(assetsData, BannerBean.class);
        final List<BannerBean.BannersBean> banners = bannerBean.getBanners();
        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView imageView;
                if (convertView == null) {
                    imageView = new ImageView(MainActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    Log.d("MainActivity", "--->>：界面复用" + convertView);
                    imageView = (ImageView) convertView;
                }
                List<BannerBean.BannersBean.BannerUrlBean.UrlListBean> url_list = banners.get(position).getBanner_url().getUrl_list();
                String urlImg = url_list.get(0).getUrl();
                Picasso.with(MainActivity.this).load(urlImg).into(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                return banners.size();
            }

//            @Override
//            public String getBannerDesc(int position) {
//                BannerBean.BannersBean.BannerUrlBean banner_url = banners.get(position).getBanner_url();
//                return banner_url.getTitle();
//            }
        });
        //开启自动滚动
        mBannerView.startRoll();
        //item的点击事件
        mBannerView.setOnItemClickListener(new BannerViewPager.BannerItemClickListener() {
            @Override
            public void itemClick(int position) {
                Toast.makeText(MainActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });
        //设置viewpager切换的动画时间
        mBannerView.setBannerScroller(850);
        //设置滚动轮询的时间
        mBannerView.setDelayTime(3000);

        mEditText.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(12)});

        countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mButton.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "结束啦", Toast.LENGTH_SHORT).show();
            }
        };
        //倒计时按钮点击事件
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.start();
            }
        });
        //暂停倒计时
        ((Button) findViewById(R.id.pause)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
            }
        });

    }

    //从资源文件中获取分类json
    private String getAssetsData(String path) {
        String result = "";
        try {
            //获取输入流
            InputStream mAssets = getAssets().open(path);
            //获取文件的字节数
            int lenght = mAssets.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer);
            mAssets.close();
            result = new String(buffer);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("fuck", e.getMessage());
            return result;
        }
    }

    InputFilter mInputFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher emojiMatcher = emoji.matcher(charSequence);
            if (emojiMatcher.find()) {
                Toast.makeText(MainActivity.this, "不支持输入表情", Toast.LENGTH_LONG).show();
                return "";
            }
            return null;
        }
    };

    InputFilter inputFilter = new InputFilter() {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher = pattern.matcher(charSequence);
            if (!matcher.find()) {
                return null;
            } else {
                Toast.makeText(MainActivity.this, "只能输入汉字,英文，数字", Toast.LENGTH_LONG).show();
                return "";
            }

        }
    };
    //1、添加一样注释
}
