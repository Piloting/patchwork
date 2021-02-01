package ru.pilot.pathwork.block;

import javafx.scene.paint.Color;

public class TwoTriangleLeftBlock extends TwoTriangleBlock {

    public TwoTriangleLeftBlock(){
        super(0, Color.GREEN, Color.YELLOW);
    }
    
    @Override
    public boolean isReadyMade() {
        return true;
    }

}
