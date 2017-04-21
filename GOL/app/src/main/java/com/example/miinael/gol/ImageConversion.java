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
    BoardCallback boardCallback;

    public ImageConversion(Bitmap imageBitmap, Board board, BoardCallback boardCallback) {
        this.board = board;
        newImageBitmap = Bitmap.createScaledBitmap(imageBitmap, (int)(imageBitmap.getWidth() / 1.2), (int)(imageBitmap.getHeight() / 1.2), true);
        this.boardCallback = boardCallback;
        System.out.println("bitmap hentet inn");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        int height = newImageBitmap.getHeight();
        int width = newImageBitmap.getWidth();
        float[][] array = new float[width][height];
        //sette alle verdier inn i et vanlig array og gjøre dem om til et tall mellom 0 og 1
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                array[i][j] = luminance(newImageBitmap.getPixel(i, j));
            }
        }


        //gjøre fargene til enten 0 eller 1 og forskyve forskjellen til neste pixel
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                float pixel = array[x][y];
                int newPixel = pixel > 0.50 ? 1 : 0;
                array[x][y] = newPixel;
                float quant_error = pixel - newPixel;

                if (x < width-1)
                    array[x + 1][y] = array[x + 1][y] + quant_error * 7 / 16;

                if (y < height-1 && x > 0)
                    array[x - 1][y + 1] = array[x - 1][y + 1] + quant_error * 3 / 16;

                if (y < height-1)
                    array[x ][y + 1] = array[x][y + 1] + quant_error * 5 / 16;

                if ((x < width-1) && (y < height-1))
                    array[x + 1][y + 1] = array[x + 1][y + 1] + quant_error * 1 / 16;
            }

        board.setPicture(array);
        boardCallback.run(board);
    }

    /**
     * Finds the brightness of the pixel color. This method is using the formula made by Darel Rex Finley to find a color brightness.
     * @param pixel the pixel containing the non-premultiplied ARGB color
     * @return  the luminance in a scale between 0 and 1
     *@See http://alienryderflex.com/hsp.html
     */
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
