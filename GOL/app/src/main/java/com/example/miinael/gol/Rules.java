package com.example.miinael.gol;

import java.io.Serializable;

/**
 * Created by miinael on 05.04.2017.
 */

public class Rules implements Serializable {

    protected boolean shouldSpawnActiveCell(int counter) {
        return counter == 3;
    }

    protected static boolean shouldStayAlive(int counter) {
        return counter == 2 || counter == 3;
    }
}
