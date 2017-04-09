package com.example.miinael.gol;

import android.content.Intent;
import android.graphics.Canvas;
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

import java.util.Timer;
import java.util.TimerTask;

public class GameOfLifeActivity extends AppCompatActivity {
    Board board = new Board(50, 50);
    GameView gameView;
    protected boolean isRunning = false;
    private Timer timer;
    QRCodeWriter qrCode;
    BitMatrix qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);

        // hente ut string og lage QR-kode
        Intent intent = getIntent();
        String msg = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        qrCode = new QRCodeWriter();
        try {
            qr = qrCode.encode(msg, BarcodeFormat.QR_CODE, 1, 1);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        // legge QR-koden i board og gi dette boardet til GameView
        gameView = (GameView)this.findViewById(R.id.game);
        board.setQRCode(qr);
        gameView.setBoard(board);

        /*gameView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (left == 0 && top == 0 && right == 0 && bottom == 0)
                    return;

                int min = Math.min(right - left, bottom - top);
                v.width
            }
        });*/
    }

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
        },0,500);
    }

    public void stop(View view) {
        if (timer == null)
            return;

        timer.cancel();
        timer = null;
    }
}
