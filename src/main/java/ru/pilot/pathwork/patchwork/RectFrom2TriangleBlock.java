package ru.pilot.pathwork.patchwork;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import ru.pilot.pathwork.color.ColorSupplier;

public class RectFrom2TriangleBlock implements Block {
    
    private final List<TriangleBlock> blocks = new ArrayList<>(2);
    private Paint paint1;
    private Paint paint2;
    
    public RectFrom2TriangleBlock(double angle, Paint paint1, Paint paint2){
        this(angle, null, paint1, paint2);
    }
    
    public RectFrom2TriangleBlock(List<TriangleBlock> blocks, Paint paint1, Paint paint2){
        this.blocks.addAll(blocks);
        this.paint1 = paint1;
        this.paint2 = paint2;
    }
    
    public RectFrom2TriangleBlock(double angle, Scale scale, Paint paint1, Paint paint2){
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
        return "RectFrom2TriangleBlock";
    }

    @Override
    public Block copy() {
        List<TriangleBlock> newBlocks = new ArrayList<>(2);
        for (Block block : blocks) {
            newBlocks.add((TriangleBlock)block.copy());
        }
        return new RectFrom2TriangleBlock(newBlocks, paint1, paint2);
    }

    @Override
    public Block mirror(boolean isX, boolean isY){
        List<TriangleBlock> newBlocks = new ArrayList<>(blocks.size());
        for (TriangleBlock block : blocks) {
            newBlocks.add(block.mirror(isX, isY));
        }

        return new RectFrom2TriangleBlock(newBlocks, paint1, paint2);
    }

    @Override
    public List<TriangleBlock> getBlocks() {
        return blocks;
    }

    @Override
    public void setPaint(int i, int j, ColorSupplier colorSupplier) {
        // todo как сделать?
        this.paint1 = colorSupplier.getColor(i, j, 50, 50);
        this.paint2 = colorSupplier.getColor(i, j, 50, 50);
        blocks.get(0).setInnerPaint(this.paint1);
        blocks.get(1).setInnerPaint(this.paint2);
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

    public Point2D getCenter(){
        Point2D center = null;
        for (Block block : blocks) {
            center = ((TriangleBlock) block).getCenter();
            System.out.println(center);
        }
        return center;
    }
}
