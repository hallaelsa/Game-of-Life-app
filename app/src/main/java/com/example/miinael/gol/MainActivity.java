package com.example.miinael.gol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.miinael.gol.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go(View view){
        Intent intent = new Intent(this,GameOfLifeActivity.class);
        EditText edit = (EditText)findViewById(R.id.inText);
        String msg = edit.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, msg);
        if(!msg.isEmpty())
            startActivity(intent);
    }
}
