package ru.pilot.pathwork.color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.apache.commons.collections4.CollectionUtils;

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
        List<Paint> list = new ArrayList<>(colorCount);
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
    public Paint getColor(int h, int w, int hBlock, int wBlock, Set<Paint> exclude) {
        Paint paint;
        if (CollectionUtils.isEmpty(exclude)){
            paint =  colorSet != null ? colorSet.getRandom() : getRgb();
        } else if (colorSet != null) {
            ArrayList<Paint> paints = new ArrayList<>(colorSet.getColors());
            paints.removeAll(exclude);
            paint = new ColorSet(paints).getRandom();
        } else {
            int i = 0;
            do {
                paint = getRgb();
                i++;
            } while (exclude.contains(paint) || i<10);
        }
        return paint;
    }
}
