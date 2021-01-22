package ru.pilot.pathwork;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.fill.sector.CrossSectorStrategy;
import ru.pilot.pathwork.fill.fillBlock.RandomFillStrategy;

public class TrashTest {
    
    @Test
    public void test(){
        CrossSectorStrategy crossSectorStrategy = new CrossSectorStrategy(new RandomFillStrategy(), 10);
        crossSectorStrategy.parting(5, 5, new ColorSupplier(){});
    }
}
