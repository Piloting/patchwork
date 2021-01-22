package ru.pilot.pathwork.color;

import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ColorSet {
    public static final Random rnd = new Random();
    
    private List<Color> colors;
    
    public Color getRandom(){
        return colors.get(rnd.nextInt(colors.size()));
    }
}
