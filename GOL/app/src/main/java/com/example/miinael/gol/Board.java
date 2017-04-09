package com.example.miinael.gol;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miinael on 27.03.2017.
 */

public class Board {
    protected int defaultWidth;
    protected int defaultHeight;
    public Rules rules = new Rules();

    public List<List<Boolean>> dynamicBoardArray = new ArrayList<List<Boolean>>(160);
    public List<List<Boolean>> clone;

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
        List<List<Boolean>> array = new ArrayList<List<Boolean>>(qr.getWidth());

        for(int i = 0; i < qr.getWidth(); i++) {
            array.add(new ArrayList<Boolean>(qr.getHeight()));
            for(int j = 0; j < qr.getHeight(); j++) {
                array.get(i).add(j, qr.get(i,j));
            }
        }
        dynamicBoardArray = array;

    }

    /**
     * The method initializing the board with all values set to false.
     */
    public void initStartBoard(){
        for(int i = 0; i < defaultWidth; i++) {
            dynamicBoardArray.add(i, new ArrayList<Boolean>(defaultHeight));
        }

        for(int i = 0; i < defaultWidth; i++){
            for(int j = 0; j < defaultHeight; j++){
                dynamicBoardArray.get(i).add(j,false);
            }
        }
    }

    public void defaultStartBoard(){
        setValue(0,2,true);
        setValue(1,2,true);
        setValue(2,2,true);
        setValue(2,1,true);
        setValue(1,0,true);
    }

    public void setValue(int x, int y, boolean value) {
        dynamicBoardArray.get(x).set(y, value);
    }

    public boolean getValue(int x, int y) {
        return dynamicBoardArray.get(x).get(y);
    }

    public void toggleValue(int x, int y) {
        dynamicBoardArray.get(x).set(y, !dynamicBoardArray.get(x).get(y));
    }

    public int getWidth() {
        return dynamicBoardArray.size();
    }

    public int getHeight() {
        return dynamicBoardArray.get(0).size();
    }

    public void createClone() {
        clone = new ArrayList<List<Boolean>>(getWidth());

        for(int i = 0; i < getWidth(); i++) {
            clone.add(new ArrayList<Boolean>(getHeight()));

            for(int j = 0; j < getHeight(); j++) {
                clone.get(i).add(j, getValue(i, j));
            }
        }
    }

    public void setCloneValue(int x, int y, boolean value) {
        clone.get(x).set(y, value);
    }

    public void switchBoard() {
        for(int i = 0; i < getWidth(); i++) {
            for(int j = 0; j < getHeight(); j++) {
                setValue(i, j, clone.get(i).get(j));
            }
        }
    }

    /**
     * The method resetting all values of the board to false
     */
    public void clearBoard() {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                setValue(i,j,false);
            }
        }
    }

    /**
     * The method creating the next generation of cells to be drawn or removed.
     */
    public void nextGeneration() {
        createClone();

        for(int i = 0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++){
                int neighbors = countNeighbor(i, j);
                boolean value = getValue(i, j) ? rules.shouldStayAlive(neighbors) : rules.shouldSpawnActiveCell(neighbors);
                setCloneValue(i, j, value );
            }
        }
        switchBoard();
    }

    /**
     * The method counting the alive cells surrounding the appointed cell
     * @param i     the first column index of the array
     * @param j     the second column index of the array
     * @return      the number of alive neighboring cells
     */
    public int countNeighbor(int i, int j){
        int count = 0;

        //check top
        if (isActiveCell(i, j-1))
            count++;

        //check top-left
        if (isActiveCell(i-1, j-1))
            count++;

        //check top-right
        if (isActiveCell(i+1, j-1))
            count++;

        //check left
        if (isActiveCell(i-1, j))
            count++;

        //check right
        if (isActiveCell(i+1, j))
            count++;

        //check bottom
        if (isActiveCell(i, j+1))
            count++;

        //check bottom-right
        if (isActiveCell(i+1, j+1))
            count++;

        //check bottom-left
        if (isActiveCell(i-1, j+1))
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
    private boolean isActiveCell(int i, int j) {
        return inBounds(i, j) && getValue(i,j) == true;
    }

    /**
     * The method checking if the appointed position is within the board array.
     * @param i         the first column index of the array
     * @param j         the second column index of the array
     * @return          <code>false</code> if the position is exceeding the board array
     */
    private boolean inBounds(int i, int j){
        if(i == -1 || j == -1){
            return false;
        }

        if(i >= getWidth() || j >= getHeight()){
            return false;
        }

        return true;
    }


}
