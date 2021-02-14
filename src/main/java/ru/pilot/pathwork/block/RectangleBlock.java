package ru.pilot.pathwork.block;

import java.util.Collections;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import ru.pilot.pathwork.ControlUtils;
import ru.pilot.pathwork.color.ColorSupplier;

public class RectangleBlock implements Block {

    private Paint paint;

    public RectangleBlock(){
        this(Color.GREEN);
    }
    
    public RectangleBlock(Paint paint){
        this.paint = paint;
    }

    @Override
    public Node getNode(double width, double height) {
        Node node = new Rectangle(width, height, paint);
        return ControlUtils.addColorReplaceEvent(this, node);
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

    @Override
    public List<Paint> getColors() {
        return Collections.singletonList(paint);
    }

    public void transform(Transform transform){
        
    }

    @Override
    public boolean isCenterSymmetry() {
        return true;
    }

    @Override
    public boolean isReadyMade() {
        return true;
    }

    @Override
    public void replaceColor(Paint oldPaint, Paint newPaint) {
        if (paint.equals(oldPaint)){
            paint = newPaint;
        }
    }
}
