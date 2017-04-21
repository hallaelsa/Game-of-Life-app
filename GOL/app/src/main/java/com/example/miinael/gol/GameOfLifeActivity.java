package com.example.miinael.gol;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;

import java.sql.SQLOutput;
import java.util.Timer;
import java.util.TimerTask;

public class GameOfLifeActivity extends AppCompatActivity {
    Board board = new Board(50, 50);
    GameView gameView;
    private Timer timer;
    QRCodeWriter qrCode;
    BitMatrix qr;
    int speed = 500;
    int delay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        gameView = (GameView)this.findViewById(R.id.game);

        if(savedInstanceState == null) {
            // hente ut string og lage QR-kode
            Intent intent = getIntent();
            qrCode = new QRCodeWriter();
           
            if(intent.getStringExtra(MainActivity.EXTRA_MESSAGE1) != null) {
                String msg = intent.getStringExtra(MainActivity.EXTRA_MESSAGE1);
                try {
                    qr = qrCode.encode(msg, BarcodeFormat.QR_CODE, 1, 1);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                // legge QR-koden i board og gi dette boardet til GameView
                board.setQRCode(qr);
                gameView.setBoard(board);
            } else {
                // hente inn bitmap med bildet
                Bitmap imageBitmap = intent.getParcelableExtra(MainActivity.EXTRA_MESSAGE2);
                (new Thread(new ImageConversion(imageBitmap, board))).start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gameView.setBoard(board);

            }


        }
    }

    private int findClosestColor(double oldPixel) {
        return (int)Math.round(oldPixel/256);
    }


    /**
     * The method starting the animation of the board.
     * @param view  the view containing the canvas to be drawn on.
     */
    public void start(View view) {
        if (timer != null)
            timer.cancel();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                board.nextGeneration();
                gameView.postInvalidate();
            }
        },delay,speed);
    }

    /**
     * The method stopping the animation of the board.
     * @param view  the view containing the canvas to be drawn on.
     */
    public void stop(View view) {
        if (timer == null)
            return;

        timer.cancel();
        timer = null;
    }

    /**
     * The method making the animation run faster.
     * @param view the view containing the canvas to be drawn on.
     */
    public void faster(View view) {
        if(speed < 10)
            return;

        stop(gameView);
        speed /= 2;
        delay = 0;
        start(gameView);
    }

    /**
     * The method making the animation run slower.
     * @param view  the view containing the canvas to be drawn on.
     */
    public void slower(View view) {
        stop(gameView);
        speed *= 2;
        delay = speed;
        start(gameView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean("Timer", timer != null);
        stop(gameView);
        outState.putSerializable("Board", board);
        outState.putInt("Speed", speed);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        board = (Board)savedInstanceState.get("Board");
        speed = savedInstanceState.getInt("Speed");
        gameView.setBoard(board);

        if (savedInstanceState.getBoolean("Timer"))
            start(gameView);
    }

    @Override
    public void onStop() {
        super.onStop();
        stop(gameView);
    }



}
