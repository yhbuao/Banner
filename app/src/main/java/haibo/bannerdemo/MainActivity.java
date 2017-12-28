package haibo.bannerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBannerViewPager = ((BannerViewPager) findViewById(R.id.bannerViewPager));
        mEditText = ((EditText) findViewById(R.id.ed_text));

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

        mEditText.setFilters(new InputFilter[]{inputFilter,new InputFilter.LengthFilter(12)});

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new Button(this));
        linearLayout.addView(new EditText(this));
        linearLayout.addView(new ImageButton(this));
        linearLayout.removeViews(1,4-1);
        int childCount = linearLayout.getChildCount();
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

    InputFilter inputFilter=new InputFilter() {

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher=  pattern.matcher(charSequence);
            if(!matcher.find()){
                return null;
            }else{
                Toast.makeText(MainActivity.this, "只能输入汉字,英文，数字", Toast.LENGTH_LONG).show();
                return "";
            }

        }
    };
}
