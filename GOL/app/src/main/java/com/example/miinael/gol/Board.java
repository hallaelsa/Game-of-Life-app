package com.example.miinael.gol;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miinael on 27.03.2017.
 */

public class Board implements Serializable {
    protected int defaultWidth;
    protected int defaultHeight;
    public Rules rules = new Rules();
    public boolean[][] dynamicBoardArray;
    public boolean[][] clone;
    private boolean isUpdating;

    /**
     * Constructs and initiates the board to be used.
     * @param width     the width of the board.
     * @param height    the height of the board.
     */
    public Board(int width, int height) {
        this.defaultHeight = height;
        this.defaultWidth = width;
        initStartBoard();
        defaultStartBoard();
    }

    public void setQRCode(BitMatrix qr) {
        boolean[][] array = new boolean[qr.getWidth()][qr.getHeight()];

        for(int i = 0; i < qr.getWidth(); i++) {
            for(int j = 0; j < qr.getHeight(); j++) {
                array[i][j] = qr.get(i, j);
            }
        }
        dynamicBoardArray = array;
        clone = getBoard(array.length, array[0].length);

    }

    public void setPicture(float[][] pictureArray) {
        boolean[][] array = new boolean[pictureArray.length][pictureArray[0].length];

        for(int i = 0; i < pictureArray.length; i++) {
            for(int j = 0; j < pictureArray[0].length; j++) {
                array[i][j] = pictureArray[i][j] == 0? true : false;
            }
        }
        dynamicBoardArray = array;
        clone = getBoard(array.length, array[0].length);

    }

    public void defaultStartBoard(){
        setValue(0, 2, true);
        setValue(1, 2, true);
        setValue(2, 2, true);
        setValue(2, 1, true);
        setValue(1, 0, true);
    }

    /**
     * The method initializing the board with all values set to false.
     */
    public void initStartBoard(){
        dynamicBoardArray = getBoard(defaultWidth, defaultHeight);
        clone = getBoard(defaultWidth, defaultHeight);
    }

    private boolean[][] getBoard(int x, int y) {
        boolean[][] tmp = new boolean[x][y];
        return tmp;
    }

    public void setValue(int x, int y, boolean value) {
        dynamicBoardArray[x][y] = value;
    }

    public boolean getValue(int x, int y) {
        return dynamicBoardArray[x][y];
    }

    public int getWidth() {
        return dynamicBoardArray.length;
    }

    public int getHeight() {
        return dynamicBoardArray[0].length;
    }

    public void setCloneValue(int x, int y, boolean value) {
        clone[x][y] = value;
    }

    public void switchBoard() {
        boolean[][] tmp = dynamicBoardArray;
        dynamicBoardArray = clone;
        clone = tmp;
    }

    public void clearClone() {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                setCloneValue(i,j,false);
            }
        }
    }


    /**
     * The method creating the next generation of cells to be drawn or removed.
     */
    public void nextGeneration() {
        if (isUpdating)
            return;

        isUpdating = true;
        clearClone();

        double start = System.currentTimeMillis();
        int width = getWidth();
        int height = getHeight();

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                int neighbors = countNeighbor(i, j, width, height);

                if(getValue(i, j) ? rules.shouldStayAlive(neighbors) : rules.shouldSpawnActiveCell(neighbors))
                    setCloneValue(i, j, true);
            }
        }

        double end = System.currentTimeMillis();
        double elapsed = end - start;

        switchBoard();
        isUpdating = false;
        System.out.println(elapsed);
    }

    /**
     * The method counting the alive cells surrounding the appointed cell
     * @param i     the first column index of the array
     * @param j     the second column index of the array
     * @return      the number of alive neighboring cells
     */
    public int countNeighbor(int i, int j, int width, int height){
        int count = 0;

        //check top
        if (isActiveCell(i, j-1, width, height))
            count++;

        //check top-left
        if (isActiveCell(i-1, j-1, width, height))
            count++;

        //check top-right
        if (isActiveCell(i+1, j-1, width, height))
            count++;

        //check left
        if (isActiveCell(i-1, j, width, height))
            count++;

        //check right
        if (isActiveCell(i+1, j, width, height))
            count++;

        //check bottom
        if (isActiveCell(i, j+1, width, height))
            count++;

        //check bottom-right
        if (isActiveCell(i+1, j+1, width, height))
            count++;

        //check bottom-left
        if (isActiveCell(i-1, j+1, width, height))
            count++;

        return count;
    }

    /**
     * The method checking if the cell is alive.
     * @param i         the first column index of the array
     * @param j         the second column index of the array
     * @return          <code>true</code> if the cell is alive
     *                  and not exceeding the board array
     */
    private boolean isActiveCell(int i, int j, int width, int height) {
        if(i < 0 || j < 0 || i >= width || j >= height){
            return false;
        }

        return getValue(i,j);
    }
}
