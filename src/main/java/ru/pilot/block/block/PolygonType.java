package ru.pilot.block.block;

import java.util.Arrays;

public enum PolygonType {
    
    TRIANGLE (new double[] { 0,0,   0,50,   50,0          }),
    RECT     (new double[] { 0,0,   0,50,   50,50,  50,0  }),
    RHOMBUS  (new double[] { 0,25,  25,50,  25,50,  25,0  }),
    TRAPEZOID(new double[] { 0,0,   0,50,   50,40,  50,10 });

    private final double[] simplePoints;

    PolygonType(double[] points){
        simplePoints = points;
    }
    
    public double[] getSimplePoints(){
        return Arrays.copyOf(simplePoints, simplePoints.length);
    }
    
}
