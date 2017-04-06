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
            qr = qrCode.encode(msg, BarcodeFormat.QR_CODE, 50, 50);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        // legge QR-koden i board og gi dette boardet til GameView
        gameView = (GameView)this.findViewById(R.id.game);
        board.setQRCode(qr);
        gameView.setBoard(board);
    }

    public void start(View view) {
        // må lage ny timer siden vi avslutter den på stopp()
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
        timer.cancel();
    }

}
