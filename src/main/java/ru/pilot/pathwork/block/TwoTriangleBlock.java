package ru.pilot.pathwork.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import ru.pilot.pathwork.color.ColorSupplier;

public class TwoTriangleBlock implements Block {
    
    private final List<TriangleBlock> blocks = new ArrayList<>(2);
    private Paint paint1;
    private Paint paint2;

    public TwoTriangleBlock(){
        this(0, Color.GREEN, Color.YELLOW);
    }
    
    public TwoTriangleBlock(double angle, Paint paint1, Paint paint2){
        this(angle, null, paint1, paint2);
    }
    
    public TwoTriangleBlock(List<TriangleBlock> blocks, Paint paint1, Paint paint2){
        this.blocks.addAll(blocks);
        this.paint1 = paint1;
        this.paint2 = paint2;
    }
    
    public TwoTriangleBlock(double angle, Scale scale, Paint paint1, Paint paint2){
        blocks.add(new TriangleBlock(angle, scale, paint1));
        blocks.add(new TriangleBlock(angle+180, scale, paint2));
        
        this.paint1 = paint1;
        this.paint2 = paint2;
    }

    @Override
    public Node getNode(double width, double height) {
        Group group = new Group();
        for (Block block : blocks) {
            group.getChildren().add(block.getNode(width, height));
        }
        return group;
    }

    @Override
    public String getType() {
        return "TwoTriangleBlock";
    }

    @Override
    public Block copy() {
        List<TriangleBlock> newBlocks = new ArrayList<>(2);
        for (Block block : blocks) {
            newBlocks.add((TriangleBlock)block.copy());
        }
        return new TwoTriangleBlock(newBlocks, paint1, paint2);
    }

    @Override
    public Block mirror(boolean isX, boolean isY){
        List<TriangleBlock> newBlocks = new ArrayList<>(blocks.size());
        for (TriangleBlock block : blocks) {
            newBlocks.add(block.mirror(isX, isY));
        }

        return new TwoTriangleBlock(newBlocks, paint1, paint2);
    }

    @Override
    public List<TriangleBlock> getBlocks() {
        return blocks;
    }

    @Override
    public void setPaint(int i, int j, ColorSupplier colorSupplier) {
        // todo как сделать?
        paint1 = colorSupplier.getColor(i, j, 50, 50);
        paint2 = colorSupplier.getColor(i, j, 50, 50, Collections.singleton(paint1));
        blocks.get(0).setInnerPaint(paint1);
        blocks.get(1).setInnerPaint(paint2);
    }

    @Override
    public void setInnerPaint(Paint paint) {
        
    }

    @Override
    public List<Paint> getColors() {
        return Arrays.asList(paint1,paint2);
    }

    @Override
    public void transform(Transform transform) {
        for (Block block : blocks) {
            block.transform(transform);
        }
    }

    @Override
    public boolean isCenterSymmetry() {
        return false;
    }

    @Override
    public boolean isReadyMade() {
        return false;
    }

    public Point2D getCenter(){
        Point2D center = null;
        for (Block block : blocks) {
            center = ((TriangleBlock) block).getCenter();
            System.out.println(center);
        }
        return center;
    }

    @Override
    public void replaceColor(Paint oldPaint, Paint newPaint) {
        if (paint1.equals(oldPaint)){
            paint1 = newPaint;
        }
        if (paint2.equals(oldPaint)){
            paint2 = newPaint;
        }
        for (TriangleBlock block : blocks) {
            block.replaceColor(oldPaint, newPaint);
        }
    }
}
