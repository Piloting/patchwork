package ru.pilot.pathwork.color;

import java.util.List;

import javafx.scene.paint.Paint;

public class RandomByColorSetColorSupplier extends RandomColorSupplier {
    public RandomByColorSetColorSupplier(List<Paint> colors){
        super(new ColorSet(colors));
    }
}
