package sudoku.myself.xhc.com.sudoku.mywidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

import sudoku.myself.xhc.com.sudoku.bean.Node;
import sudoku.myself.xhc.com.sudoku.debugutil.util.DensityUtils;
import sudoku.myself.xhc.com.sudoku.util.Constant;


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
    private boolean gameFlag = false;
    private Paint paint, textPaint;
    private int width, height;
    //线的长度，和粗细程度
    private int lineLength;

    private int dp_1;

    //一个数字格子的宽度 正方形
    private float numGridWidth;
    //候选区中的数字或者颜色的宽度
    private float candicateWidth;
    private Node choiseNode = null;
    private float choiseX , choiseY ;
    //测试数据
    private int[] test = new int[]{1, 4, 6, 2};

    private Node[][] nodes = new Node[9][9];


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
//        lineWidth = DensityUtils.dip2px(context, 3);
        dp_1 = DensityUtils.dip2px(context, 1);

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                nodes[i][j] = new Node();
                nodes[i][j].setSystemColor(7);
                if (i % 2 == 0) {
                    nodes[i][j].setNumFlag(true);
                    nodes[i][j].setUserNum(5);

                    nodes[i][j].setColorFlag(true);
                    nodes[i][j].setUserColor(5);
                    continue;
                }
                if (j % 3 == 0) {
                    nodes[i][j].setNumFlag(true);
                    int[] tests = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
                    nodes[i][j].setCandidateNum(tests);

                    nodes[i][j].setColorFlag(true);
                    nodes[i][j].setCandidateColor(tests);
                    continue;
                }
                if (j % 7 == 0) {
                    nodes[i][j].setNumFlag(false);
                    nodes[i][j].setSystemNum(9);

                    nodes[i][j].setColorFlag(false);
                    nodes[i][j].setSystemColor(9);
                    continue;
                }

            }
        }

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

        if (y < numGridWidth * 9) {
            //点击到棋盘上
        }
        if (!gameFlag) {
            //有颜色数独
            if (y > height - numGridWidth) {
                //数字键盘上
            } else if (y < height - numGridWidth && y > height - 2 * numGridWidth) {
                //颜色键盘上
            } else if (y < height - 2 * numGridWidth && y > height - 3 * numGridWidth) {
                //数字候选区上
            } else if (y < height - 3 * numGridWidth && y > height - 4 * numGridWidth) {
                //颜色候选区上
            }
        } else {
            if (y > height - numGridWidth) {
                //数字键盘上
            } else if (y < height - numGridWidth && y > height - 2 * numGridWidth) {
                //数字候选区
            }
        }


        invalidate();
    }

    private void click(float x, float y) {
        if (y < numGridWidth * 9) {
            //点击到棋盘上
            calculateNode(x, y);
        }
        if (!gameFlag) {
            //有颜色数独
            if (y > height - numGridWidth) {
                //数字键盘上
            } else if (y < height - numGridWidth && y > height - 2 * numGridWidth) {
                //颜色键盘上
            } else if (y < height - 2 * numGridWidth && y > height - 3 * numGridWidth) {
                //数字候选区上
            } else if (y < height - 3 * numGridWidth && y > height - 4 * numGridWidth) {
                //颜色候选区上
            }
        } else {
            if (y > height - numGridWidth) {
                //数字键盘上
            } else if (y < height - numGridWidth && y > height - 2 * numGridWidth) {
                //数字候选区
            }
        }

        invalidate();
    }

    /**
     *
     * @param x 在屏幕上的坐标
     * @param y 坐标
     */
    private void calculateNode(float x , float y){
        if(x < 0 || x > width || y > 9 * numGridWidth || y < 0){
            return ;
        }
        int i = (int)Math.ceil(y / numGridWidth) - 1;
        int j = (int)Math.ceil(x / numGridWidth) - 1;
        choiseX = j * numGridWidth ;
        choiseY = i * numGridWidth;
        choiseNode = nodes[i][j];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255, 218, 218, 218);
        drawLineMap(canvas);
        drawKeyBoard(canvas);

        drawCandidateColor(canvas, test);
        drawCandidateNum(canvas, test, gameFlag);

        testQiPan(canvas);
        drawChoiseNodeHightLight(canvas);
    }

    //画一个被选择个格子
    private void drawChoiseNodeHightLight(Canvas canvas) {
        if(choiseNode == null)return ;
        RectF rectF = new RectF(choiseX , choiseY , choiseX + numGridWidth , choiseY + numGridWidth);
        paint.setColor(Color.parseColor("#000000"));
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(rectF,paint);

    }

    //测试函数可以删除
    private void testQiPan(Canvas canvas) {
        float x = 0;
        float y = 0;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {

                drawNode(canvas, nodes[i][j], x, y);
                x += numGridWidth;
            }
            y += numGridWidth;
            x = 0;
        }
    }

    /**
     * @param canvas    。
     * @param node      node对象
     * @param positionX 在屏幕上的坐标x
     * @param positionY 在屏幕上的坐标y
     */
    private void drawNode(Canvas canvas, Node node, float positionX, float positionY) {
        if (!gameFlag) {
            //有颜色数独
            if (node.isColorFlag()) {
                //被挖掉
                if (node.getUserColor() != 0) {
                    drawNormalColor(canvas, Constant.Color[node.getUserColor() - 1], positionX, positionY);
                } else if (node.haveCandicateColor()) {
                    drawCandicateColorInGrid(canvas, node.getCandidateColor(), positionX, positionY);
                } else /*用户没有动过这个格子*/;
            } else {
                drawNormalColor(canvas, Constant.Color[node.getSystemColor() - 1], positionX, positionY);
            }
        }
        if (node.isNumFlag()) {
            //被挖掉
            if (node.getUserNum() != 0) {
                drawNormalNumber(canvas, textPaint, node.getUserNum(), positionX, positionY);
            } else if (node.haveCandicateNum()) {
                //画候选区的数字画在格子中
                drawCandicateNumInGrid(canvas, node.getCandidateNum(), positionX, positionY);
            } else  /*用户没有动过这个格子*/;

        } else {
            drawNormalNumber(canvas, textPaint, node.getSystemNum(), positionX, positionY);
        }


    }

    /**
     * 在棋盘中画候选数字
     *
     * @param canvas     画布
     * @param candicates 候选数字
     * @param positionX  这个格子的位置
     * @param positionY  这个格子的位置
     */
    private void drawCandicateNumInGrid(Canvas canvas, int[] candicates, float positionX, float positionY) {

        int count = 0;
        float tempX = positionX, tempY = positionY;
        for (int i = 0; i < candicates.length; ++i) {
            if (candicates[i] == 0) continue;
            drawCandicateNumSmall(canvas, candicates[i], tempX, tempY);
            count++;
            tempX += candicateWidth;
            if (count == 3 || count == 6) {
                tempY += candicateWidth;
                tempX = positionX;
            }
        }
    }

    private void drawCandicateColorInGrid(Canvas canvas, int[] candicates, float positionX, float positionY) {
        int count = 0;
        float tempX = positionX, tempY = positionY;
        for (int i = 0; i < candicates.length; ++i) {
            if (candicates[i] == 0) continue;
            drawCandicateColorSmall(canvas, Constant.Color[candicates[i] - 1], tempX, tempY);
            count++;
            tempX += candicateWidth;
            if (count == 3 || count == 6) {
                tempY += candicateWidth;
                tempX = positionX;
            }
        }
    }


    private void drawKeyBoard(Canvas canvas) {
        paint.setStrokeWidth((float) (dp_1 * 1.5));
        paint.setColor(Color.parseColor("#515151"));
        canvas.drawLine(0, height - numGridWidth, lineLength, height - numGridWidth, paint);
        canvas.drawLine(0, height, lineLength, height, paint);

        for (int i = 0; i < 10; ++i) {
            canvas.drawLine(i * numGridWidth, height - numGridWidth, i * numGridWidth, height, paint);
        }
        for (int i = 0; i < 9; ++i) {
            drawKeyBoardNum(canvas, i + 1, i * numGridWidth, height - numGridWidth);
        }

        if (!gameFlag) {
            //加一个颜色键盘
            canvas.drawLine(0, height - 2 * numGridWidth, lineLength - 2, height - 2 * numGridWidth, paint);
            canvas.drawLine(0, height - numGridWidth, lineLength - 2, height - numGridWidth, paint);
            for (int i = 0; i < 10; ++i) {
                canvas.drawLine(i * numGridWidth, height - 2 * numGridWidth, i * numGridWidth, height - numGridWidth, paint);
            }
            for (int i = 0; i < 9; ++i) {
                drawKeyBoardColor(canvas, Constant.Color[i], i * numGridWidth, height - 2 * numGridWidth);
            }
        }
    }


    /**
     * 画候选区的数字
     * flag 是判断是否是有颜色的数独
     */
    private void drawCandidateNum(Canvas canvas, int[] candidates, boolean flag) {

        float x = dp_1;
        float y = height - 2 * numGridWidth;
        if (flag) {
            for (int i = 0; i < candidates.length; ++i) {
                if (candidates[i] == 0) {
                    continue;
                }
                drawSolidNumber(canvas, candidates[i], x, y);
                x += numGridWidth;
            }
            return;
        }
        x = dp_1;
        y = height - 3 * numGridWidth;
        for (int i = 0; i < candidates.length; ++i) {
            if (candidates[i] == 0) {
                continue;
            }
            drawSolidNumber(canvas, candidates[i], x, y);
            x += numGridWidth;
        }

    }

    private void drawCandidateColor(Canvas canvas, int[] candidates) {
        float x = dp_1;
        float y = height - 4 * numGridWidth;
        for (int i = 0; i < candidates.length; ++i) {
            if (candidates[i] == 0) {
                continue;
            }
            RectF rectF = new RectF(x + 3 * dp_1, y + 3 * dp_1, x + numGridWidth - 3 * dp_1, y + numGridWidth - 3 * dp_1);
            paint.setColor(Constant.Color[candidates[i] - 1]);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rectF, paint);
            x += numGridWidth;
        }
    }


    /**
     * 绘画数字 num哪个数字 positionx左上角的x坐标 positiony左上角的y坐标 计算出数字的高度和宽度
     */
    private void drawNormalNumber(Canvas canvas, Paint paint,
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

    private void drawNormalColor(Canvas canvas, int color, float positionX, float positionY) {
        RectF rectF = new RectF(positionX + 3 * dp_1, positionY + 3 * dp_1, positionX + numGridWidth - 3 * dp_1, positionY + numGridWidth - 3 * dp_1);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectF, paint);
    }

    /**
     * 在棋盘上的格子中的小数字
     *
     * @param canvas    画布
     * @param num       数字
     * @param positionX 坐标
     * @param positionY 坐标
     */
    private void drawCandicateNumSmall(Canvas canvas, int num, float positionX, float positionY) {
        paint.setColor(Color.argb(255, 0, 0, 0));
        paint.setTextSize(dp_1 * 10);
        float textWidth = paint.measureText(num + "");
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = (float) (Math.ceil(fm.descent - fm.ascent));
        positionX = positionX + candicateWidth / 2 - textWidth / 2;
        positionY = positionY + candicateWidth / 2 - textHeight / 2 + textHeight - dp_1;
        canvas.drawText(num + "", positionX, positionY, paint);
    }

    private void drawCandicateColorSmall(Canvas canvas, int color, float positionX, float positionY) {
        RectF rectF = new RectF(positionX + dp_1, positionY + dp_1, positionX + candicateWidth - dp_1, positionY + candicateWidth - dp_1);
        paint.setColor(color);
        canvas.drawRect(rectF, paint);
    }


    /**
     * 画实心的数字
     * 主要用在候选区的数字那个地方
     */
    private void drawSolidNumber(Canvas canvas, int num, float positionX, float positionY) {
        RectF rect = new RectF(positionX + 3 * dp_1, positionY + 3 * dp_1, positionX + numGridWidth - 3 * dp_1, positionY + numGridWidth - 3 * dp_1);
        textPaint.setColor(Color.parseColor("#C1C1C1"));
        canvas.drawRect(rect, textPaint);
        drawNormalNumber(canvas, textPaint, num, positionX, positionY);
    }

    private void drawKeyBoardNum(Canvas canvas, int num, float positionX, float positionY) {
        textPaint.setColor(Color.parseColor("#727272"));
        drawNormalNumber(canvas, textPaint, num, positionX, positionY);
    }

    private void drawKeyBoardColor(Canvas canvas, int color, float positionX, float positionY) {

        drawNormalColor(canvas, color, positionX, positionY);

    }


    /**
     * 界面上的格子
     *
     * @param canvas
     */
    private void drawLineMap(Canvas canvas) {
        paint.setStrokeWidth((float) (dp_1 * 1.5));
        paint.setColor(Color.parseColor("#515151"));
        //四根横向的粗线
        for (int i = 0; i < 4; ++i) {
            canvas.drawLine(0, i * 3 * numGridWidth, lineLength, i * 3 * numGridWidth, paint);
        }
        //竖向的四跟粗线
        for (int i = 0; i < 4; ++i) {
            canvas.drawLine(i * 3 * numGridWidth, 0, i * 3 * numGridWidth, 9 * numGridWidth, paint);
        }
        paint.setColor(Color.parseColor("#969696"));

        paint.setStrokeWidth(dp_1);
        //横向8根细线 会有两个与粗线重合
        for (int i = 0; i < 8; ++i) {
            canvas.drawLine(0, numGridWidth + i * numGridWidth, lineLength, numGridWidth + i * numGridWidth, paint);
        }

        //竖线8根
        for (int i = 0; i < 8; ++i) {
            canvas.drawLine(numGridWidth + i * numGridWidth, 0, numGridWidth + i * numGridWidth, 9 * numGridWidth, paint);
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        lineLength = width < height ? width : height;
        numGridWidth = lineLength / 9f;
        candicateWidth = numGridWidth / 3f;
    }
}
