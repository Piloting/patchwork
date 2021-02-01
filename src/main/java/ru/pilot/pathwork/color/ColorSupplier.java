package ru.pilot.pathwork.color;


import java.util.Set;

import javafx.scene.paint.Paint;
import ru.pilot.pathwork.block.Block;

public interface ColorSupplier {
    default Paint getColor(int h, int w, int hBlock, int wBlock) {
        return getColor(h, w, hBlock, wBlock, null);
    }
    default Paint getColor(int h, int w, int hBlock, int wBlock, Set<Paint> exclude) {
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
