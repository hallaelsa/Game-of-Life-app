package com.example.miinael.gol;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by miinael on 24.03.2017.
 */

public class GameView extends View {
    private Board board;
    private Paint backgroundPaint = new Paint();
    private Rect bgRect;
    private Paint cellPaint = new Paint();
    private Rect cell;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.f;
    float lastX;
    float lastY;
    float distanceX;
    float distanceY;
    int activePointerID = -1;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }



    public void setBoard(Board board) {
        this.board = board;
    }

    private void messageDialog(String text) {
        new AlertDialog.Builder(this.getContext())
            .setTitle("Debug")
            .setMessage(text)
            .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
            scaleGestureDetector.onTouchEvent(motionEvent);

            switch(motionEvent.getAction() & motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    System.out.println("action down");
                    int pointerIndex = MotionEventCompat.getActionIndex(motionEvent);
                    float x = motionEvent.getX(pointerIndex);
                    float y = motionEvent.getY(pointerIndex);
                    lastX = x;
                    lastY = y;

                    activePointerID = motionEvent.getPointerId(pointerIndex);

                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    System.out.println("action move");
                    int pointerIndex = motionEvent.findPointerIndex(activePointerID);
                    float x = motionEvent.getX(pointerIndex);
                    float y = motionEvent.getY(pointerIndex);

                    if(!scaleGestureDetector.isInProgress()) {
                        distanceX += (x - lastX);
                        distanceY += (y - lastY);
                        System.out.println("invalidate");
                        invalidate();
                    }
                    lastX = x;
                    lastY = y;
                    break;
                }

                case MotionEvent.ACTION_CANCEL: {
                    activePointerID = -1;
                    System.out.println("action cancel");
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    activePointerID = -1;
                    System.out.println("action up");
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {
                    int pointerIndex = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    int pointerID = motionEvent.getPointerId(pointerIndex);

                    if(pointerID == activePointerID) {
                        System.out.println("pointer id==actionid");
                        int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        lastX = motionEvent.getX(newPointerIndex);
                        lastY = motionEvent.getY(newPointerIndex);
                        activePointerID = motionEvent.getPointerId(newPointerIndex);
                    }
                    System.out.println("action up");
                    break;
                }
            }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale(scaleFactor, scaleFactor, distanceX, distanceY);

        backgroundPaint.setColor(Color.GREEN);
        bgRect = new Rect(0,0, (int)(canvas.getWidth() / scaleFactor), (int)(canvas.getHeight() / scaleFactor));
        canvas.drawRect(bgRect, backgroundPaint);

        int width = (int)Math.ceil((double)canvas.getWidth() / board.getWidth());
        int height = (int)Math.ceil((double)canvas.getHeight() / board.getHeight());
        // velge bredden til stående skjerm selv hvis den ligger.
        int size = width > height ? height : width;
        int widthOffset = canvas.getWidth() > canvas.getHeight() ? (canvas.getWidth() - canvas.getHeight()) / 2: 0;
        // for å zoome inn mot midten
        int scaleXOffset = (int)((canvas.getWidth() / scaleFactor - canvas.getWidth()) / 2);
        int scaleYOffset = (int)((canvas.getHeight() / scaleFactor - canvas.getHeight()) / 2);


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
                cell = new Rect(cellX + widthOffset + scaleXOffset, cellY + scaleYOffset, cellX + widthOffset + scaleXOffset + size, cellY + scaleYOffset + size);
                canvas.drawRect(cell, cellPaint);
            }
        }

        //canvas.restore();

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }
}



