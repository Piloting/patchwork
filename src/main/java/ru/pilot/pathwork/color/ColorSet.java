package ru.pilot.pathwork.color;

import java.util.List;
import java.util.Random;

import javafx.scene.paint.Paint;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ColorSet {
    public static final Random rnd = new Random();
    
    private List<Paint> colors;
    
    public Paint getRandom(){
        return colors.get(rnd.nextInt(colors.size()));
    }
}
