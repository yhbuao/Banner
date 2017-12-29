package haibo.bannerdemo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import haibo.bannerdemo.banner.BannerAdapter;
import haibo.bannerdemo.banner.BannerViewPager;

/**
 * @author User
 */
public class MainActivity extends AppCompatActivity {

    private BannerViewPager mBannerViewPager;
    private EditText mEditText;
    private Button mButton;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBannerViewPager = ((BannerViewPager) findViewById(R.id.bannerViewPager));
        mEditText = ((EditText) findViewById(R.id.ed_text));
        mButton = ((Button) findViewById(R.id.bt));
        mBannerViewPager.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setBackgroundResource(R.mipmap.b);
                return imageView;
            }
        });
        //开启自动滚动
        mBannerViewPager.startRoll();

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
}
