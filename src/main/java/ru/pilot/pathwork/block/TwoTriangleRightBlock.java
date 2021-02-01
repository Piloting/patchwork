package ru.pilot.pathwork.block;

import javafx.scene.paint.Color;

public class TwoTriangleRightBlock extends TwoTriangleBlock {

    public TwoTriangleRightBlock(){
        super(90, Color.GREEN, Color.YELLOW);
    }
    
    @Override
    public boolean isReadyMade() {
        return true;
    }

}
