package ru.pilot.pathwork.color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;

public class RandomColorSupplier implements ColorSupplier {

    private static final Random rnd = new Random();
 
    protected ColorSet colorSet;
    
    public RandomColorSupplier(){
        this.colorSet = null;
    }
    public RandomColorSupplier(int colorCount){
        this.colorSet = getColorSet(colorCount);
    }
    public RandomColorSupplier(ColorSet colorSet){
        this.colorSet = colorSet;
    }

    private ColorSet getColorSet(int colorCount) {
        List<Color> list = new ArrayList<>(colorCount);
        ColorSet colorSet = new ColorSet(list);
        for (int i = 0; i < colorCount; i++) {
            list.add(getRgb());
        }
        return colorSet;
    }
    
    protected Color getRgb() {
        return Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
    }

    @Override
    public Color getColor(int h, int w, int hBlock, int wBlock) {
        return colorSet != null ? colorSet.getRandom() : getRgb();
    }
}
