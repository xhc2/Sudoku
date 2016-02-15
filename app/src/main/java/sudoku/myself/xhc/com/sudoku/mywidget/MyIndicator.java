package sudoku.myself.xhc.com.sudoku.mywidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sudoku.myself.xhc.com.sudoku.R;


/**
 * Created by xhc on 2015/11/27.
 */
public class MyIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private LinearLayout linearContain;
    //下面的滑动的线，现在的位置
    private float currentPosition;
    private ViewPager viewPager;
    private int scroll = 100;
    /**
     * 下面的线的高度
     * 和宽度
     */
    private float lineHeight;
    private float lineWidth;
    private Paint linePaint;
    private int lineColor;
    private CustomeTextViewInter customeTextViewInter;
    private Context context;

    public MyIndicator(Context context) {
        this(context, null, 0);
    }

    /**
     * 用于用户自定义textview
     * unSelect没有被选择的状态
     * selected 用于被选择的状态
     */
    public interface CustomeTextViewInter {
        TextView unSelect(TextView tv);

        TextView selected(TextView tv);
    }

    /**
     * 设置监听
     *
     * @param customeTextViewInter
     */
    public void setCustomeTextViewInter(CustomeTextViewInter customeTextViewInter) {
        this.customeTextViewInter = customeTextViewInter;
    }

    public MyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyIndicator, defStyleAttr, 0);
        lineHeight = a.getDimension(R.styleable.MyIndicator_lineHeight, 10);
        lineColor = a.getColor(R.styleable.MyIndicator_lineColor, Color.BLACK);

        a.recycle();

        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.setHorizontalScrollBarEnabled(false);
        linearContain = new LinearLayout(context);
        linearContain.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(linearContain, params);
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(lineColor);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(this);
        this.post(new Runnable() {
            @Override
            public void run() {
                //写在这里的原因是能获取到宽高等数据
                changeLinePosition(0, 0);
            }
        });
        setHeader();
        changeTextView(0);
    }

    /**
     * 添加头部的tab
     */
    public void setHeader() {
        if (this.viewPager != null) {
            for (int i = 0; i < viewPager.getAdapter().getCount(); ++i) {
                addTextView(viewPager.getAdapter().getPageTitle(i).toString(), i);
            }
        }
    }

    /**
     * 绘画下面的线
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int textHeight = linearContain.getHeight();
        canvas.drawRect(currentPosition, textHeight - lineHeight, currentPosition + lineWidth, textHeight, linePaint);
    }

    /**
     * @param text
     */
    private void addTextView(String text, int position) {
        //判断用户是否自定义了textview
        TextView textView = new Text(context, position);
        if (customeTextViewInter != null) {
            textView = customeTextViewInter.unSelect(textView);
            if (textView == null) {
                throw new RuntimeException("customeTextViewInter textview == null");
            }
        }

        textView.setText(text);
        linearContain.addView(textView);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        LinearLayout contain = (LinearLayout) getChildAt(0);

        int childCount = contain.getChildCount();


        if (contain.getMeasuredWidth() < getMeasuredWidth()) {
            //宽度不够,让子控件平分宽度
            contain.setMinimumWidth(getMeasuredWidth());
            int childWidth = getMeasuredWidth() / childCount;
            for (int i = 0; i < childCount; ++i) {
                View view = contain.getChildAt(i);
                view.setMinimumWidth(childWidth);
            }
        } else {
            super.onLayout(changed, l, t, r, b);
        }
    }

    /**
     * 默认类型的text
     */
    private class Text extends TextView {

        int position;

        public Text(Context context, int position) {
            super(context);
            this.position = position;
            this.init(context);
        }

        private void init(Context context) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            this.setLayoutParams(params);
            int size = dip2px(context, 10);
            params.bottomMargin = (int) lineHeight;
            this.setPadding(size, 0, size, 0);
            this.setGravity(Gravity.CENTER);
            this.setTextSize(20);
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position);
                }
            });
        }

    }

    private void changeLinePosition(int position, float offset) {
        int count = linearContain.getChildCount();
        if (count <= 0) return;

        int left = linearContain.getChildAt(position).getLeft();
        int right = linearContain.getChildAt(position).getRight();
        lineWidth = right - left;
        offset = lineWidth * offset;
        currentPosition = left + offset;
        this.scrollTo((int) currentPosition - scroll, 0);

        invalidate();
    }

    private void unSelect(TextView tv) {
        tv.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    private void selected(TextView tv) {
        tv.setBackgroundColor(Color.parseColor("#1A3DFF"));
    }

    private void changeTextView(int position) {

        int count = linearContain.getChildCount();

        for (int i = 0; i < count; ++i) {
            TextView t = (TextView) linearContain.getChildAt(i);
            if (customeTextViewInter != null) {
                if (i == position) {
                    customeTextViewInter.selected(t);
                } else {
                    customeTextViewInter.unSelect(t);
                }
            }

        }

    }


    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        changeLinePosition(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        changeTextView(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        if (state == ViewPager.SCROLL_STATE_IDLE) {
//            //滑动到点后
//
//        }
    }
}
