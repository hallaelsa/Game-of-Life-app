package com.example.miinael.gol;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;


/**
 * Created by miinael on 21.04.2017.
 */

public class ImageConversion implements Runnable {
    Bitmap newImageBitmap;
    Board board;

    public ImageConversion(Bitmap imageBitmap, Board board) {
        this.board = board;
        newImageBitmap = imageBitmap.copy(Bitmap.Config.RGB_565, true);
        System.out.println("bitmap hentet inn");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        System.out.println("Bredde: " +newImageBitmap.getWidth());
        System.out.println("Høyde: " + newImageBitmap.getHeight());
        System.out.println("vanlig pixel: "+ newImageBitmap.getPixel(3, 3));
        // http://stackoverflow.com/questions/596216/formula-to-determine-brightness-of-rgb-color

        float[][] array = new float[newImageBitmap.getWidth()][newImageBitmap.getHeight()];

        //sette alle verdier i et vanlig array og gjøre dem om til et tall mellom 0 og 255
        for (int i = 0; i < newImageBitmap.getWidth(); i++) {
            for (int j = 0; j < newImageBitmap.getHeight(); j++) {
                array[i][j] = luminance(newImageBitmap.getPixel(i, j));
                //System.out.println("arrayinnhold: " + array[i][j]);
            }
        }
        System.out.println("Array 0x0: " + array[0][0]);
        System.out.println("Array 3x3: " + array[3][3]);

        //finne fargene på hver pixel og sette dem til enten 0 eller 1
        for (int y = 0; y < newImageBitmap.getHeight(); y++)
            for (int x = 0; x < newImageBitmap.getWidth(); x++) {
                float pixel = array[x][y];
                //System.out.println("pixel " + pixel);
                int newPixel = pixel > 0.50 ? 1 : 0;
                array[x][y] = newPixel;
                //System.out.println("ny pixel: " + newPixel);
                float quant_error = pixel - newPixel;

                if (x < newImageBitmap.getWidth()-1)
                    array[x + 1][y] = array[x + 1][y] + quant_error * 7 / 16;

                if (y < newImageBitmap.getHeight()-1 && x > 0)
                    array[x - 1][y + 1] = array[x - 1][y + 1] + quant_error * 3 / 16;

                if (y < newImageBitmap.getHeight()-1)
                    array[x ][y + 1] = array[x][y + 1] + quant_error * 5 / 16;

                if ((x < newImageBitmap.getWidth()-1) && (y < newImageBitmap.getHeight()-1))
                    array[x + 1][y + 1] = array[x + 1][y + 1] + quant_error * 1 / 16;
            }


        board.setPicture(array);

    }


    public float luminance(int pixel) {
        float red = (float)Color.red(pixel)/255;
        //System.out.println("red: " + red);
        float blue = (float)Color.blue(pixel)/255;
        //System.out.println("blue: " + blue);
        float green = (float)Color.green(pixel)/255;
        //System.out.println("green: " + green);

        float luminance = (float)Math.sqrt(0.299 * Math.pow(red, 2) + 0.587 * Math.pow(green, 2) + 0.114 * Math.pow(blue, 2));
        //System.out.println("lumance : " + luminance);
        //return (0.2126*red + 0.7152*green + 0.0722*blue);
        return luminance;
    }
}
