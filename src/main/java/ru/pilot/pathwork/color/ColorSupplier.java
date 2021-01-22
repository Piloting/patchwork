package ru.pilot.pathwork.color;


import javafx.scene.paint.Color;
import ru.pilot.pathwork.patchwork.Block;

public interface ColorSupplier {
    default Color getColor(int h, int w, int hBlock, int wBlock) {
        return null;
    }
    
    default void fillColor(Block[][] blocks){
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                blocks[i][j].setPaint(i, j, this);
            }
        }
    }
}
