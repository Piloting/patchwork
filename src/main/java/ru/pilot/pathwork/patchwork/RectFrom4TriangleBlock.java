package ru.pilot.pathwork.patchwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import ru.pilot.pathwork.color.ColorSupplier;

public class RectFrom4TriangleBlock implements Block {

    private final List<TriangleBlock> blocks = new ArrayList<>(4);
    private Paint paint0;
    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    
    public RectFrom4TriangleBlock(List<TriangleBlock> blocks, Paint paint0, Paint paint1, Paint paint2, Paint paint3){
        this.blocks.addAll(blocks);
        this.paint0 = paint0;
        this.paint1 = paint1;
        this.paint2 = paint2;
        this.paint3 = paint3;
    }
    
    public RectFrom4TriangleBlock(double angle, Paint paint0, Paint paint1, Paint paint2, Paint paint3) {
        // width - сторона квадрата, здесь это гипотенуза. Надо узнать сторону треугольника при такой гипотенузе
        double hypotenuse = 50;
        double width = Math.sqrt(hypotenuse * hypotenuse / 2);
        
        TriangleBlock[] newBlocks = new TriangleBlock[4];
        newBlocks[0] = (new TriangleBlock(width, new Rotate(angle - 45 + 180), null, paint0));
        newBlocks[1] = (new TriangleBlock(width, new Rotate(angle - 45 + 270), null, paint1));
        newBlocks[2] = (new TriangleBlock(width, new Rotate(angle - 45)      , null, paint2));
        newBlocks[3] = (new TriangleBlock(width, new Rotate(angle - 45 + 90) , null, paint3));
        blocks.addAll(Arrays.asList(newBlocks));
        for (Block block : blocks) {
            block.transform(new Translate(hypotenuse/2,hypotenuse/2));
        }
        
        this.paint0 = paint0;
        this.paint1 = paint1;
        this.paint2 = paint2;
        this.paint3 = paint3;
    }

    @Override
    public Node getNode(double width, double height) {
        Group group = new Group();
       
        ObservableList<Node> children = group.getChildren();
        
        children.add(blocks.get(0).getNode(width, height/2));
        children.add(blocks.get(2).getNode(width, height/2));
        
        children.add(blocks.get(1).getNode(width/2, height));
        children.add(blocks.get(3).getNode(width/2, height));
        
        
        return group;
    }

    @Override
    public String getType() {
        return "RectFrom4TriangleBlock";
    }

    @Override
    public Block copy() {
        List<TriangleBlock> newBlocks = new ArrayList<>(4);
        for (Block block : blocks) {
            newBlocks.add((TriangleBlock)block.copy());
        }
        return new RectFrom4TriangleBlock(newBlocks, paint0, paint1, paint2, paint3);
    }

    public Block mirror(boolean isX, boolean isY){
        TriangleBlock[] newBlocks = new TriangleBlock[4];
        Paint newPaint0 = paint0;
        Paint newPaint1 = paint1;
        Paint newPaint2 = paint2;
        Paint newPaint3 = paint3;
        
        if (isX){
            TriangleBlock block0 = blocks.get(0).mirror(true, false);
            block0.transform(new Translate(25, 0));
            newBlocks[2] = block0;
            newPaint2 = paint0;
            
            TriangleBlock block2 = blocks.get(2).mirror(true, false);
            block2.transform(new Translate(-25, 0));
            newBlocks[0] = block2;
            newPaint0 = paint2;
        } else {
            newBlocks[0] = blocks.get(0).copy();
            newBlocks[2] = blocks.get(2).copy();
        }
        
        if (isY){
            TriangleBlock block1 = blocks.get(1).mirror(false, true);
            block1.transform(new Translate(0, 25));
            newBlocks[3] = block1;
            newPaint3 = paint1;

            TriangleBlock block3 = blocks.get(3).mirror(false, true);
            block3.transform(new Translate(0, -25));
            newBlocks[1] = block3;
            newPaint1 = paint3;
        } else {
            newBlocks[1] = blocks.get(1).copy();
            newBlocks[3] = blocks.get(3).copy();
        }
        
        return new RectFrom4TriangleBlock(Arrays.asList(newBlocks), newPaint0, newPaint1, newPaint2, newPaint3);
    }

    @Override
    public List<TriangleBlock> getBlocks() {
        return blocks;
    }

    @Override
    public void setPaint(int i, int j, ColorSupplier colorSupplier) {
        // todo как сделать?
        this.paint0 = colorSupplier.getColor(i, j, 50, 50);
        this.paint1 = colorSupplier.getColor(i+1, j, 25, 50);
        this.paint2 = colorSupplier.getColor(i, j+1, 50, 25);
        this.paint3 = colorSupplier.getColor(i+1, j+1, 25, 25);
        blocks.get(0).setInnerPaint(this.paint0);
        blocks.get(1).setInnerPaint(this.paint1);
        blocks.get(2).setInnerPaint(this.paint2);
        blocks.get(3).setInnerPaint(this.paint3);
    }

    @Override
    public void setInnerPaint(Paint paint) {
        
    }

    @Override
    public void transform(Transform transform) {
        for (Block block : blocks) {
            block.transform(transform);
        }
    }

    @Override
    public boolean isCenterSymmetry() {
        return true;
    }
}
