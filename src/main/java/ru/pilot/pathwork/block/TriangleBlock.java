package ru.pilot.pathwork.block;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import ru.pilot.pathwork.color.ColorSupplier;

public class TriangleBlock implements Block {
    
    private Paint paint;
    private double[] points;

    public TriangleBlock(){
        this(0, null, Color.GREEN);
    }
            
    public TriangleBlock(Rotate rot, Paint color){
        this(50, rot, null, color);
    }
    
    public TriangleBlock(double angle, Paint color){
        this(new Rotate(angle, 25, 25), color);
    }
    
    public TriangleBlock(double angle, Scale scale, Paint color){
        this(50, new Rotate(angle, 25, 25), scale, color);
    }
    
    public TriangleBlock(double[] points, Paint color){
        this.points = points;
        this.paint = color;
    }
    
    public TriangleBlock(double width, Rotate rot, Scale scale, Paint color){
        points = new double[]{
                0.0, 0.0,
                0.0, width,
                width, 0.0};
        
        if (rot != null){
            rot.transform2DPoints(points, 0, points, 0, points.length/2);
        }
        if (scale != null){
            scale.transform2DPoints(points, 0, points, 0, points.length/2);
        }
        
        paint = color;
    }
    

    @Override
    public Node getNode(double width, double height) {

        // преобразование к нужным размерам
        double[] newPoints = Arrays.copyOf(points, points.length); 
        double sizeY = max(newPoints[0], newPoints[2], newPoints[4]) - min(newPoints[0], newPoints[2], newPoints[4]);
        double sizeX = max(newPoints[1], newPoints[3], newPoints[5]) - min(newPoints[1], newPoints[3], newPoints[5]);

        Scale scale = new Scale(width/sizeX, height/sizeY);
        scale.transform2DPoints(newPoints, 0, newPoints, 0, newPoints.length/2);

        Polygon polygon = new Polygon();
        for (double point : newPoints) {
            polygon.getPoints().add(point);
        }

        if (paint != null){
            polygon.setFill(paint);
        } else {
            polygon.setStroke(Color.BLACK);
        }
        return polygon;
    }

    private double max(double p1, double p2, double p3) {
        return Math.max(Math.max(p1, p2), p3);
    }
    private double min(double p1, double p2, double p3) {
        return Math.min(Math.min(p1, p2), p3);
    }

    @Override
    public String getType() {
        return "TriangleBlock";
    }

    @Override
    public TriangleBlock copy() {
        return new TriangleBlock(points, paint);
    }

    private double[] mirrorPoints(double[] points, boolean isX){
        double[] newPoints = Arrays.copyOf(points, points.length);
        // зеркалирование относительно оси
        boolean initIsX = isX;
        Double oldMax = null;
        Double newMax = null;
        for (int i = 0; i < newPoints.length; i++) {
            if (isX){
                oldMax = max(oldMax, newPoints[i]);
                newPoints[i] = newPoints[i] * -1;
                newMax = max(newMax, newPoints[i]);
            }
            isX = !isX;
        }
        
        // перенос отражения на старое место
        // на сколько переносить?
        double delta = max(oldMax, newMax) - min(oldMax, newMax);
        delta = delta * (oldMax < newMax ? -1 : 1);
                
        isX = initIsX;
        for (int i = 0; i < newPoints.length; i++) {
            if (isX){
                newPoints[i] = newPoints[i] + delta;
            }
            isX = !isX;
        }
        
        return newPoints;
    }

    private Double max(Double one, Double two) {
        return one == null ? two : Math.max(one, two) ;
    }
    private Double min(Double one, Double two) {
        return one == null ? two : Math.min(one, two) ;
    }
    
    public TriangleBlock mirror(boolean isX, boolean isY){
        double[] newPoints = points;
        if (isX){
            newPoints = mirrorPoints(points, true);
        }
        if (isY){
            newPoints = mirrorPoints(newPoints, false);
        }
        return new TriangleBlock(newPoints, paint);
    }
    
//    public Block mirror(double width, double height, double angle, CopyDirection direction){
//        Scale scale = new Scale();
//        switch (direction){
//            case TO_LEFT:
//            case TO_RIGHT:
//                scale.setY(-1);
//                break;
//            case TO_UP:
//            case TO_DOWN:
//                scale.setX(-1);
//                break;
//            case TO_DIAGONAL_LEFT:
//                scale.setX(-1);
//                scale.setY(-1);
//        }
//        return new TriangleBlock(
//                angle,
//                scale,
//                paint);
//    }

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
        transform.transform2DPoints(points, 0, points, 0, points.length/2);
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
        double maxY = max(points[0], points[2], points[4]);
        double minY = min(points[0], points[2], points[4]);
        double maxX = max(points[1], points[3], points[5]);
        double minX = min(points[1], points[3], points[5]);
        double sizeY = maxY - minY;
        double sizeX = maxX - minX;
        return new Point2D(maxX-sizeX/2, maxY-sizeY/2);
    }

    @Override
    public void replaceColor(Paint oldPaint, Paint newPaint) {
        if (paint.equals(oldPaint)){
            paint = newPaint;
        }
    }
}
