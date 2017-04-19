package com.example.miinael.gol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE1 = "com.example.miinael.gol.MESSAGE";
    public static final String EXTRA_MESSAGE2 = "com.example.miinael.gol.PICTURE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go(View view){
        Intent intent = new Intent(this,GameOfLifeActivity.class);
        EditText edit = (EditText)findViewById(R.id.inText);
        String msg = edit.getText().toString();
        intent.putExtra(EXTRA_MESSAGE1, msg);
        if(!msg.isEmpty())
            startActivity(intent);
    }

    public void pictureIntent(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(intent, 1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Intent intent = new Intent(this, GameOfLifeActivity.class);
            intent.putExtra(EXTRA_MESSAGE2, imageBitmap);
            if(imageBitmap != null)
                startActivity(intent);
        }
    }
}
