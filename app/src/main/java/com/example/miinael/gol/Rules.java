package com.example.miinael.gol;

/**
 * Created by miinael on 05.04.2017.
 */

public class Rules {

    protected boolean shouldSpawnActiveCell(int counter) {
        return counter == 3;
    }

    protected static boolean shouldStayAlive(int counter) {
        return counter == 2 || counter == 3;
    }
}
