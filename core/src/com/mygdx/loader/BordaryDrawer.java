package com.mygdx.loader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.player.Fraction;

/**
 * Created by Viteker on 31.07.2017.
 */
class BordaryDrawer {

    void drawBoardary(Pixmap pixmap, int tileSize, byte b){
        //left
        int RECTANGLE_WIDTH = 5;
        int RECTANGLE_HEIGHT = 10;
        if((b & 0x01) == 1){
            int x = 0;
            for(int y = 0; y < tileSize; y+=2* RECTANGLE_HEIGHT){
                pixmap.fillRectangle(x,y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
            }
        }
        //right
        if((b & 0x02) == 2){
            int x = tileSize - RECTANGLE_WIDTH;
            for(int y = 0; y < tileSize; y+=2* RECTANGLE_HEIGHT){
                pixmap.fillRectangle(x,y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
            }
        }
        //up
        if((b & 0x04) == 4){
            int y = 0;
            for(int x = 0; x < tileSize; x += 2* RECTANGLE_HEIGHT){
                pixmap.fillRectangle(x,y, RECTANGLE_HEIGHT, RECTANGLE_WIDTH);
            }
        }
        //down
        if((b & 0x08) == 8){
            int y = tileSize - RECTANGLE_WIDTH;
            for(int x = 0; x < tileSize; x += 2* RECTANGLE_HEIGHT){
                pixmap.fillRectangle(x,y, RECTANGLE_HEIGHT, RECTANGLE_WIDTH);
            }
        }
    }
    Color getColorForFraction(Fraction fraction){
        assert Fraction.values().length == 2;

        switch (fraction){
            case Blue:
                return Color.BLUE;
            case Standart:
                return Color.GRAY;
        }
        return null;
    }
}
