package ru.pilot.pathwork.fill.sector;

import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.fill.fillBlock.FillStrategy;
import ru.pilot.pathwork.patchwork.Block;

public class NoSectorStrategy implements SectorStrategy {

    private final FillStrategy fillStrategy;
    
    public NoSectorStrategy(FillStrategy fillStrategy){
        this.fillStrategy = fillStrategy;
    }
    
    public Block[][] parting(int countX, int countY, ColorSupplier partColorStrategy){
        Block[][] blocks = fillStrategy.fill(countX, countY);
        
        partColorStrategy.fillColor(blocks);
        
        return blocks;
    }
}
