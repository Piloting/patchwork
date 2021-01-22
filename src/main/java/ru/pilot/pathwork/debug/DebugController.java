package ru.pilot.pathwork.debug;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import ru.pilot.pathwork.patchwork.Block;
import ru.pilot.pathwork.patchwork.RectFrom2TriangleBlock;
import ru.pilot.pathwork.patchwork.RectFrom4TriangleBlock;
import ru.pilot.pathwork.patchwork.TriangleBlock;

public class DebugController {
    
    public Pane leftTopPane;
    public Pane rightTopPane;
    public Pane leftBottomPane;
    public Pane rightBottomPane;
    
    private double size = 50;


    List<Block> exampleBlocks = Block.getExampleBlocks();

    private TriangleBlock triangleBlock0 = new TriangleBlock(0, Color.BLUE);
    private TriangleBlock triangleBlock90 = new TriangleBlock(90, Color.GREEN);
    private TriangleBlock triangleBlock180 = new TriangleBlock(180, Color.GREY);
    private TriangleBlock triangleBlock270 = new TriangleBlock(270, Color.RED);
    
    private RectFrom2TriangleBlock rectFrom2TriangleBlock = new RectFrom2TriangleBlock(0, Color.RED, Color.YELLOW);
    private RectFrom4TriangleBlock rectFrom4TriangleBlock = new RectFrom4TriangleBlock(0, Color.RED, Color.YELLOW, Color.BLUE, Color.VIOLET);
    
    
    public void oneButton(){
        //triangleBlock0.transform(new Translate(size, 0));
        //triangleBlock180.transform(new Translate(0, size));
        //triangleBlock270.transform(new Translate(size, size));
        //rectFrom2TriangleBlock.transform(new Translate(size*2, 0));
        
        //leftTopPane.getChildren().add(triangleBlock0.getNode(size,size));
        //leftTopPane.getChildren().add(triangleBlock90 .getNode(size,size));
        //leftTopPane.getChildren().add(triangleBlock180.getNode(size,size));
        //leftTopPane.getChildren().add(triangleBlock270.getNode(size,size));
        //leftTopPane.getChildren().add(rectFrom2TriangleBlock.getNode(size,size));
        
        //leftTopPane.getChildren().add(rectFrom4TriangleBlock.getNode(size,size));
        leftTopPane.getChildren().add(exampleBlocks.get(0).getNode(size,size));
    }

    public void twoButton(){
        //TriangleBlock copy0 = (TriangleBlock) triangleBlock0.copy();
        //rightTopPane.getChildren().add(copy0.getNode(size,size));
        
        //TriangleBlock copy90 = (TriangleBlock) triangleBlock90.copy();
        //TriangleBlock copy180 = (TriangleBlock) triangleBlock180.copy();
        //TriangleBlock copy270 = (TriangleBlock) triangleBlock270.copy();
        //RectFrom4TriangleBlock copyRT = (RectFrom4TriangleBlock) rectFrom4TriangleBlock.copy();
        //copyRT.transform(new Translate(size*2, 0));

        //Point2D center = copy0.getCenter();
        //copy0.transform(new Rotate(45));
        //rightTopPane.getChildren().add(copy0.getNode(size,size));

        //Block triangleBlock = copyRT.mirror(true, true);
        //rightTopPane.getChildren().add(triangleBlock.getNode(size,size));
        //copy180.transform(new Rotate(90));
        
       

        //rightTopPane.getChildren().add(copy90.getNode(size,size));
        //rightTopPane.getChildren().add(copy180.getNode(size,size));
        //rightTopPane.getChildren().add(copy270.getNode(size,size));
        //rightTopPane.getChildren().add(copyRT.getNode(size,size));
        leftTopPane.getChildren().add(exampleBlocks.get(1).getNode(size,size));
    }
    
    public void threeButton(){

        leftTopPane.getChildren().add(exampleBlocks.get(2).getNode(size,size));
    }
    
    public void fourButton(){

        leftTopPane.getChildren().add(exampleBlocks.get(3).getNode(size,size));
    }
    
}

