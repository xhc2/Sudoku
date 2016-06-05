package sudoku.myself.xhc.com.sudoku.mywidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;

import sudoku.myself.xhc.com.sudoku.bean.Node;
import sudoku.myself.xhc.com.sudoku.debugutil.util.DensityUtils;
import sudoku.myself.xhc.com.sudoku.inter.IsWinCallBack;
import sudoku.myself.xhc.com.sudoku.util.Constant;
import sudoku.myself.xhc.com.sudoku.util.Sudoku;


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

    //是否有检错机制 true是检测
    private boolean checkFlag = true;
    private int dp_1;

    //一个数字格子的宽度 正方形
    private float numGridWidth;
    //候选区中的数字或者颜色的宽度
    private float candicateWidth;
    private Node choiseNode = null;
    private float choiseX, choiseY;
    //判断是否赢的唯一标志
    private boolean winFlag = false;
    private Node[][] nodes = new Node[9][9];
    private Sudoku sudoku;

    public SudokuMap(Context context) {
        this(context, null);
    }

    //点击效果
    private final int singleClick = 1;
    private final int longClick = 2;
    private final int doubleClick = 3;

    private int clickFlag = 1;

    public SudokuMap(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Handler handler = new Handler();

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
        dp_1 = DensityUtils.dip2px(context, 1);
        //测试


    }
    public void setLevel(int level){
        sudoku = Sudoku.getInstance();
//        sudoku = new Sudoku();
        sudoku.clear();
        nodes = sudoku.getGameCombinationSudoku(3);
        invalidate();
    }



//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX = event.getX();
//                downY = event.getY();
//                downTime = Calendar.getInstance().getTimeInMillis();
//                break;
//            case MotionEvent.ACTION_UP:
//                upX = event.getX();
//                upY = event.getY();
//                upTime = Calendar.getInstance().getTimeInMillis();
//                if (upTime - downTime >= LONGCLICKTIME)
//                    longPress(upX, upY);
//                else
//                    click(upX, upY);
//                break;
//        }
//        return true;
//    }


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
                long tempUpTime;

            /* 双击 */
                tempUpTime = Calendar.getInstance().getTimeInMillis();
                if (tempUpTime - upTime <= 220) {
                    upTime = tempUpTime;
                    clickFlag = doubleClick;
                    doubleClick(upX, upY);
                }
			/* 长按 */
                else if (tempUpTime - downTime >= 220) {
                    upTime = tempUpTime;
                    clickFlag = longClick;
                }
                else{
                    clickFlag = singleClick;
                    upTime = tempUpTime;
                }
			/* 为了避免响应两次双击事件 */
                if (clickFlag != doubleClick) {
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (clickFlag == singleClick) {
                                //单击
                                click(upX, upY);
                            } else if (clickFlag == longClick) {
                                //长按
                                longPress(upX, upY);
                            }
                        }
                    }, 220);
                }
                break;
        }
        return true;
    }
    /**
        双击删除上屏的颜色
     */
    private void doubleClick(float x, float y){
        if (y < numGridWidth * 9 &&  !gameFlag) {
            calculateNode(x, y);
            if (choiseNode.getUserColor() != 0) {
                deleDecidedColor();
            }
            invalidate();
        }
    }

    private void longPress(float x, float y) {

        if (y < numGridWidth * 9) {
            //点击到棋盘上  先删掉颜色然后删掉数字
            calculateNode(x, y);
            if (choiseNode.getUserNum() != 0) {
                deleDecidedNum();
            }
//            else if (choiseNode.getUserColor() != 0) {
//                deleDecidedColor();
//            }

        }
        if (!gameFlag) {
            //有颜色数独
            if (y > height - numGridWidth) {
                //数字键盘上
                if (choiseNode != null) {
                    int num = (int) Math.ceil(x / numGridWidth);
                    choiseNode.setUserNum(num);
                    if (isWin()) {
                        Log.e("xhc", "颜色数独和数字数独都赢了");
                        winFlag = true;
                    }
                }
            } else if (y < height - numGridWidth && y > height - 2 * numGridWidth) {
                //颜色键盘上
                if (choiseNode != null) {
                    int color = (int) Math.ceil(x / numGridWidth);
                    choiseNode.setUserColor(color);
                    if (isWin()) {
                        Log.e("xhc", "颜色数独和数字数独都赢了");
                        winFlag = true;
                    }
                }
            } else if (y < height - 2 * numGridWidth && y > height - 3 * numGridWidth) {
                //数字候选区上
                delCandicateNum(x, y);
            } else if (y < height - 3 * numGridWidth && y > height - 4 * numGridWidth) {
                //颜色候选区上
                delCandicateColor(x, y);
            }
        } else {
            if (y > height - numGridWidth) {
                //数字键盘上

                if (choiseNode != null) {
                    int num = (int) Math.ceil(x / numGridWidth);
                    choiseNode.setUserNum(num);
                    if (isWin()) {
                        Log.e("xhc", "数字数独都赢了");
                        winFlag = true;
                    }
                }

            } else if (y < height - numGridWidth && y > height - 2 * numGridWidth) {
                //数字候选区
                delCandicateNum(x, y);
            }
        }
        invalidate();
    }

    private IsWinCallBack listener;

    public void setIsWinCallBack(IsWinCallBack listener) {
        this.listener = listener;
    }

    private boolean isWin() {
        boolean colorWinflag = true;
        if (!gameFlag) {
            //有颜色数独
            colorWinflag = sudoku.isColorWin();
        }
        colorWinflag = sudoku.isNumWin() && colorWinflag;
        if (colorWinflag) {
            //获得胜利
            if (listener != null) {
                listener.winner();
            }
        }

        return colorWinflag;
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
                clickNumKeyBoard(x, y);

            } else if (y < height - numGridWidth && y > height - 2 * numGridWidth) {
                //颜色键盘上
                clickColorKeyBoard(x, y);
            } else if (y < height - 2 * numGridWidth && y > height - 3 * numGridWidth) {
                //数字候选区上
                clickCandicateNum(x, y);
                if (isWin()) {
                    Log.e("xhc", "颜色和数字数独都赢了");
                    winFlag = true;
                }
            } else if (y < height - 3 * numGridWidth && y > height - 4 * numGridWidth) {
                //颜色候选区上
                clickCandicateColor(x, y);
                if (isWin()) {
                    Log.e("xhc", "颜色和数字数独都赢了");
                    winFlag = true;
                }
            }
        } else {
            if (y > height - numGridWidth) {
                //数字键盘上
                clickNumKeyBoard(x, y);
            } else if (y < height - numGridWidth && y > height - 2 * numGridWidth) {
                //数字候选区
                clickCandicateNum(x, y);
                if (isWin()) {
                    Log.e("xhc", "颜色和数字数独都赢了");
                    winFlag = true;
                }
            }
        }

        invalidate();
    }

    //删除掉选择的数字
    private void deleDecidedNum() {
        if (choiseNode == null) return;
        choiseNode.setUserNum(0);
    }

    //删除掉选择的颜色
    private void deleDecidedColor() {
        if (choiseNode == null) return;
        choiseNode.setUserColor(0);
    }


    /**
     * 单击数字键盘就是往候选区加数字
     */
    private void clickNumKeyBoard(float x, float y) {

        if (y < height - numGridWidth) return;
        if (choiseNode == null) return;
        int num = (int) Math.ceil(x / numGridWidth);
        if (choiseNode.isNumFlag()) {
            choiseNode.putCandicateNum(num);
        }
    }

    /**
     * 点击颜色键盘 ， 加到候选数中
     *
     * @param x 坐标
     * @param y 坐标
     */
    private void clickColorKeyBoard(float x, float y) {
        if (gameFlag) return;
        if (choiseNode == null) return;
        if (y < height - 2 * numGridWidth || y > height - numGridWidth) return;
        int color = (int) Math.ceil(x / numGridWidth);
        if (choiseNode.isColorFlag()) {
            choiseNode.putCandicateColor(color);
        }
    }

    private void delCandicateNum(float x, float y) {
        if (choiseNode == null) return;
        int where = (int) Math.ceil(x / numGridWidth);
        if (where > choiseNode.getCountNum()) return;
        int num = 0;
        //找到候选数是哪个
        for (int i = 0; i < choiseNode.getCandidateNum().length; ++i) {
            if (choiseNode.getCandidateNum()[i] == 0) continue;
            where--;
            if (where == 0) {
                num = choiseNode.getCandidateNum()[i];
            }
        }
        if (num < 1 || num > 9) return;
        choiseNode.removeCandicateNum(num);
    }

    /**
     * 删除候选区颜色
     *
     * @param x
     * @param y
     */
    private void delCandicateColor(float x, float y) {
        if (choiseNode == null) return;
        int where = (int) Math.ceil(x / numGridWidth);
        if (where > choiseNode.getCountColor()) return;
        int color = 0;
        //找到候选数是哪个
        for (int i = 0; i < choiseNode.getCandidateColor().length; ++i) {
            if (choiseNode.getCandidateColor()[i] == 0) continue;
            where--;
            if (where == 0) {
                color = choiseNode.getCandidateColor()[i];
            }
        }
        if (color < 1 || color > 9) return;
        choiseNode.removeCandicateColor(color);
    }

    /**
     * @param x 坐标
     * @param y 坐标
     *          点击候选区的数字就是直接是确认的数字
     */
    private void clickCandicateNum(float x, float y) {
        if (choiseNode == null) return;

        int where = (int) Math.ceil(x / numGridWidth);
        if (where > choiseNode.getCountNum()) return;

        int num = 0;
        //找到候选数是哪个
        for (int i = 0; i < choiseNode.getCandidateNum().length; ++i) {
            if (choiseNode.getCandidateNum()[i] == 0) continue;
            where--;
            if (where == 0) {
                num = choiseNode.getCandidateNum()[i];
            }
        }
        if (num < 1 || num > 9) return;
        if (!gameFlag) {
            //有颜色
            if (y < height - 3 * numGridWidth || y > height - 2 * numGridWidth) return;
        } else {
            if (y < height - 2 * numGridWidth || y > height - numGridWidth) return;
        }
        choiseNode.setUserNum(num);
    }

    /**
     * 点击候选区的颜色
     *
     * @param x
     * @param y
     */
    private void clickCandicateColor(float x, float y) {
        if (y < height - 4 * numGridWidth || y > height - 3 * numGridWidth) return;
        if (choiseNode == null) return;
        int where = (int) Math.ceil(x / numGridWidth);
        if (where > choiseNode.getCountColor()) return;
        int color = 0;
        //找到候选数是哪个
        for (int i = 0; i < choiseNode.getCandidateColor().length; ++i) {
            if (choiseNode.getCandidateColor()[i] == 0) continue;
            where--;
            if (where == 0) {
                color = choiseNode.getCandidateColor()[i];
            }
        }
        if (color < 1 || color > 9) return;
        choiseNode.setUserColor(color);

    }

    /**
     * 计算点击到的哪个node
     *
     * @param x 在屏幕上的坐标
     * @param y 坐标
     */
    private void calculateNode(float x, float y) {
        if (x < 0 || x > width || y > 9 * numGridWidth || y < 0) {
            return;
        }
        int i = (int) Math.ceil(y / numGridWidth) - 1;
        int j = (int) Math.ceil(x / numGridWidth) - 1;
        choiseX = j * numGridWidth;
        choiseY = i * numGridWidth;
        choiseNode = nodes[i][j];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255, 218, 218, 218);
        if(nodes == null) return ;
        drawLineMap(canvas);
        drawKeyBoard(canvas);

        drawChessBoard(canvas);
        drawChoiseNodeHightLight(canvas);

        drawCandidateNum(canvas);
        drawCandidateColor(canvas);
        checkConfirm(canvas);
    }


    //检查填入的数字是否符合要求
    private void checkConfirm(Canvas canvas) {
        if (!checkFlag) return;
        if (!gameFlag) {
            //有颜色数独

            for (int i = 0; i < nodes.length; ++i) {
                for (int j = 0; j < nodes[i].length; ++j) {

                    if (nodes[i][j].getUserNum() != 0) {
                        if (!sudoku.isUserNumConform(i, j, nodes[i][j].getUserNum())) {
                            //这个是不符合填入的数字
                            drawWrong(canvas, j * numGridWidth, i * numGridWidth);
                        }
                    }
                    if (nodes[i][j].getUserColor() != 0) {
                        if (!sudoku.isUserColorConform(i, j, nodes[i][j].getUserColor())) {
                            //这个是不符合填入的颜色
                            drawWrong(canvas, j * numGridWidth, i * numGridWidth);
                        }
                    }
                }
            }
        } else {

            for (int i = 0; i < nodes.length; ++i) {
                for (int j = 0; j < nodes[i].length; ++j) {

                    if (nodes[i][j].getUserNum() != 0) {
                        if (!sudoku.isUserNumConform(i, j, nodes[i][j].getUserNum())) {
                            //这个是不符合填入的数字
                            drawWrong(canvas, j * numGridWidth, i * numGridWidth);
                        }
                    }

                }
            }
        }

    }

    /**
     * @param canvas
     * @param x      屏幕上的坐标
     * @param y      坐标
     */
    private void drawWrong(Canvas canvas, float x, float y) {
        paint.setColor(Color.parseColor("#FF2929"));
        int size = 10 * dp_1;
        canvas.drawLine(x + size, y + size, x + numGridWidth - size, y + numGridWidth - size, paint);
        canvas.drawLine(x + size, y + numGridWidth - size, x + numGridWidth - size, y + size, paint);

    }

    //画一个被选择个格子
    private void drawChoiseNodeHightLight(Canvas canvas) {
        if (choiseNode == null) return;
        RectF rectF = new RectF(choiseX, choiseY, choiseX + numGridWidth, choiseY + numGridWidth);
        paint.setColor(Color.parseColor("#000000"));
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(rectF, paint);

    }

    /**
     * 绘画棋盘
     */
    private void drawChessBoard(Canvas canvas) {
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
                //换行
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
    private void drawCandidateNum(Canvas canvas) {
        if (choiseNode == null) return;
        int[] candidates = choiseNode.getCandidateNum();
        float x = dp_1;
        float y = height - 2 * numGridWidth;
        if (gameFlag) {
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

    private void drawCandidateColor(Canvas canvas) {
        if (gameFlag) return;
        if (choiseNode == null) return;
        int[] candidates = choiseNode.getCandidateColor();
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
