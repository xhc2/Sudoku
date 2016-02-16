package sudoku.myself.xhc.com.sudoku.mywidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

import sudoku.myself.xhc.com.sudoku.debugutil.util.DensityUtils;


/**
 * Created by xhc on 2016/2/16.
 */
public class SudokuMap extends View {
    /* 触摸屏后保存的坐标 */
    private float downX = 0, downY = 0, moveX = 0, moveY = 0, upX = 0, upY = 0;
    /* 为响应各种事件 */
    private long downTime, upTime;
    private final int LONGCLICKTIME = 200;
    //判断是普通数独还是加强版数独 true 普通，false 加强
    private boolean gameFlag = true;
    private Paint paint, textPaint;
    private int width, height;
    //线的长度，和粗细程度
    private int lineLength, lineWidth;

    private int dp_1;

    //一个数字格子的宽度 正方形
    private int numGridWidth;

    public SudokuMap(Context context) {
        this(context, null);
    }

    public SudokuMap(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SudokuMap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        lineWidth = DensityUtils.dip2px(context, 3);
        dp_1 = DensityUtils.dip2px(context, 1);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downTime = Calendar.getInstance().getTimeInMillis();
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();
                upTime = Calendar.getInstance().getTimeInMillis();
                if (upTime - downTime >= LONGCLICKTIME)
                    longPress(upX, upY);
                else
                    click(upX, upY);
                break;
        }
        return true;
    }

    private void longPress(float x, float y) {
        invalidate();
    }

    private void click(float x, float y) {

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLineMap(canvas);
        drawKeyBoard(canvas);
    }

    private void drawKeyBoard(Canvas canvas) {
        paint.setStrokeWidth((float) (dp_1 * 1.5));
        paint.setColor(Color.parseColor("#515151"));
        canvas.drawLine(dp_1, height - numGridWidth, lineLength - 2 * dp_1, height - numGridWidth, paint);
        canvas.drawLine(dp_1, height, lineLength - 2 * dp_1, height, paint);
        for (int i = 0; i < 10; ++i) {
            canvas.drawLine(dp_1 + i * numGridWidth, height - numGridWidth, dp_1 + i * numGridWidth, height, paint);
        }
        for (int i = 0; i < 9; ++i) {
            drawKeyBoardNum(canvas, i + 1, dp_1 + i * numGridWidth, height - numGridWidth);
        }

        if (gameFlag) {

        } else {
            //需要加画一个颜色键盘 和颜色候选区

        }
    }


    private void drawCandidateNum(Canvas canvas , ){

    }

    /**
     * 绘画数字 num哪个数字 positionx左上角的x坐标 positiony左上角的y坐标 计算出数字的高度和宽度
     */
    public void drawNormalNumber(Canvas canvas, Paint paint,
                                 int num, float positionX, float positionY) {

        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(dp_1 * 15);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        float textWidth = paint.measureText(num + "");
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = (float) (Math.ceil(fm.descent - fm.ascent));
        positionX = positionX + numGridWidth / 2 - textWidth / 2;
        positionY = positionY + numGridWidth / 2 - textHeight / 2 + textHeight - 2 * dp_1;
        canvas.drawText(num + "", positionX, positionY, paint);

    }

    private void drawKeyBoardNum(Canvas canvas, int num, float positionX, float positionY) {
        textPaint.setColor(Color.parseColor("#727272"));
        drawNormalNumber(canvas, textPaint, num, positionX, positionY);
    }


    private void drawLineMap(Canvas canvas) {
        paint.setStrokeWidth((float) (dp_1 * 1.5));
        paint.setColor(Color.parseColor("#515151"));
        //四根横向的粗线
        for (int i = 0; i < 4; ++i) {
            canvas.drawLine(dp_1, dp_1 + i * 3 * numGridWidth, lineLength - dp_1, dp_1 + i * 3 * numGridWidth, paint);
        }
        //竖向的四跟粗线
        for (int i = 0; i < 4; ++i) {
            canvas.drawLine(i * 3 * numGridWidth, dp_1, i * 3 * numGridWidth, 9 * numGridWidth, paint);
        }
        paint.setColor(Color.parseColor("#969696"));

        paint.setStrokeWidth(dp_1);
        //横向8根细线 会有两个与粗线重合
        for (int i = 0; i < 8; ++i) {
            canvas.drawLine(dp_1, dp_1 + numGridWidth + i * numGridWidth, lineLength - dp_1, dp_1 + numGridWidth + i * numGridWidth, paint);
        }

        //竖线8根
        for (int i = 0; i < 8; ++i) {
            canvas.drawLine(dp_1 + numGridWidth + i * numGridWidth, dp_1, dp_1 + numGridWidth + i * numGridWidth, dp_1 + 9 * numGridWidth, paint);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        lineLength = width < height ? width : height;
        numGridWidth = lineLength / 9;
    }
}
