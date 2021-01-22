package ru.pilot.pathwork.patchwork;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import ru.pilot.pathwork.color.ColorSupplier;

public interface Block {
    
    Node getNode(double width, double height);
    String getType();
    Block copy();

    Block mirror(boolean isX, boolean isY);

    List<? extends Block> getBlocks();
    void setPaint(int i, int j, ColorSupplier colorSupplier);
    void setInnerPaint(Paint paint);
    void transform(Transform transform);
    
    List<Block> exampleBlocks = new ArrayList<>();
    
    static List<Block> getExampleBlocks(){
        if (exampleBlocks.isEmpty()){
            exampleBlocks.add(new RectangleBlock(Color.GREEN));
            exampleBlocks.add(new RectFrom2TriangleBlock(0, Color.GREEN, Color.YELLOW));
            exampleBlocks.add(new RectFrom2TriangleBlock(90, Color.GREEN, Color.YELLOW));
            exampleBlocks.add(new RectFrom4TriangleBlock(0, Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE));
        }
        return exampleBlocks;
    }
}
