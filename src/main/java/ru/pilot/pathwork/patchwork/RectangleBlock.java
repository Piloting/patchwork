package ru.pilot.pathwork.patchwork;

import java.util.Collections;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import ru.pilot.pathwork.color.ColorSupplier;

public class RectangleBlock implements Block {

    private Paint paint;
    private double width;
    private double height;

    public RectangleBlock(Paint paint){
        this.width = 50;
        this.height = 50;
        this.paint = paint;
    }

    @Override
    public Node getNode(double width, double height) {
        return new Rectangle(width, height, paint);
    }

    @Override
    public String getType() {
        return "RectangleBlock";
    }

    @Override
    public Block copy() {
        return new RectangleBlock(paint);
    }

    @Override
    public Block mirror(boolean isX, boolean isY) {
        return copy();
    }

    @Override
    public List<Block> getBlocks() {
        return Collections.singletonList(this);
    }
    
    @Override
    public void setPaint(int i, int j, ColorSupplier colorSupplier) {
        this.paint = colorSupplier.getColor(i, j, 50, 50);
    }

    @Override
    public void setInnerPaint(Paint paint) {
        this.paint = paint;
    }

    public void transform(Transform transform){
        
    }

    @Override
    public boolean isCenterSymmetry() {
        return true;
    }
}
