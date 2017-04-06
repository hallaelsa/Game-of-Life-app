package com.example.miinael.gol;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by miinael on 24.03.2017.
 */

public class GameView extends View {
    Board board;
    Canvas canvas;
    Paint backgroundPaint = new Paint();
    Rect bgRect;
    //Paint grid = new Paint();
    Paint cellPaint = new Paint();
    Rect cell;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        backgroundPaint.setColor(Color.WHITE);
        bgRect = new Rect(0,0, canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(bgRect, backgroundPaint);

        int size = (int)Math.ceil((double)canvas.getWidth() / board.getWidth());

        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {

                if (board.getValue(i,j) == true) {
                    cellPaint.setColor(Color.BLACK);
                }
                else {
                    cellPaint.setColor(Color.YELLOW);
                }

                int cellX = size * i;
                int cellY = size * j;
                //grid.setStrokeWidth(1);
                //grid.setStyle(Paint.Style.STROKE);
                //grid.setColor(Color.BLACK);
                cell = new Rect(cellX, cellY, (cellX*1)+size, (cellY*1)+size);
                canvas.drawRect(cell, cellPaint);
                //canvas.drawRect(cell, grid);
            }
        }

    }

    public void callInvalidate() {
        invalidate();
    }
}


