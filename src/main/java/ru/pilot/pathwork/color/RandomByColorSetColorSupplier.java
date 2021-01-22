package ru.pilot.pathwork.color;

import java.util.List;

import javafx.scene.paint.Color;

public class RandomByColorSetColorSupplier extends RandomColorSupplier {
    public RandomByColorSetColorSupplier(List<Color> colors){
        super(new ColorSet(colors));
    }
}
