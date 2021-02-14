package ru.pilot.block.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import lombok.Data;

@Data
public class PolygonBlock {
    
    @JsonProperty(value = "paint")
    private Paint paint;

    @JsonProperty(value = "points")
    private double[] points;

    public PolygonBlock(double[] points, Paint color){
        this.points = points;
        this.paint = color;
    }
    
    public PolygonBlock(){
        this.paint = Color.GREEN;
        this.points = PolygonType.RECT.getSimplePoints();
    }
    
    public PolygonBlock(PolygonType polygonType){
        this.paint = Color.GREEN;
        this.points = polygonType.getSimplePoints();
    }
    
}
