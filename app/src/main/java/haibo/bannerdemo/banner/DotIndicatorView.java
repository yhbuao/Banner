package haibo.bannerdemo.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author: yuhaibo
 * @time: 2018/2/9 15:12.
 * projectName: Banner.
 * Description: 圆的指示器
 */

public class DotIndicatorView extends View {
    private Drawable mDrawable;

    public DotIndicatorView(Context context) {
        this(context, null);
    }

    public DotIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawable != null) {
            //画圆
            Bitmap bitmap = drawableToBitmap(mDrawable);
            //把bitmap变成圆的
            Bitmap circleBitmap = getCircleBitmap(bitmap);
            //画到画布上圆的
            canvas.drawBitmap(circleBitmap, 0, 0, null);
        }
    }

    /**
     * 设置Drawadle
     *
     * @param drawable
     */
    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        //刷新view
        invalidate();
    }

    /**
     * 将Drawadle 转换成Bitmap
     *
     * @param drawable
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        //其它类型的
        //创建bitmap
        Bitmap outBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //创建画布
        Canvas canvas = new Canvas(outBitmap);
        //把drawable画在画布上
        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        drawable.draw(canvas);
        return outBitmap;
    }

    /**
     * 把bitmap变成圆的
     *
     * @param bitmap
     * @return
     */
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        //创建bitmap
        Bitmap circleBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        //创建一个画笔
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        //设置防抖动
        paint.setDither(true);
        //在画布上画个圆
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, paint);
        //取圆和Bitmap矩形的一个交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //把原来的bitmap画到新的圆上
        canvas.drawBitmap(bitmap, 0, 0, paint);
        //内存优化，回收BItmap
        bitmap.recycle();
        bitmap = null;
        return circleBitmap;
    }
}
