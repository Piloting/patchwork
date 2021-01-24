package ru.pilot.pathwork.fill.sector;

import ru.pilot.pathwork.SizeXY;
import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.fill.copy.CopyDirection;
import ru.pilot.pathwork.fill.copy.CopyStrategy;
import ru.pilot.pathwork.fill.copy.MirrorCopyStrategy;
import ru.pilot.pathwork.fill.fillBlock.FillStrategy;
import ru.pilot.pathwork.patchwork.Block;

public class LineSectorStrategy implements SectorStrategy {
    private final FillStrategy fillStrategy;
    private final CopyStrategy copyStrategy = new MirrorCopyStrategy();

    public LineSectorStrategy(FillStrategy fillStrategy) {
        this.fillStrategy = fillStrategy;
    }

    @Override
    public Block[][] parting(int countX, int countY, ColorSupplier partColorStrategy) {
        if (countX < 1 || countY < 1){
            return new Block[0][0];
        }
        
        boolean isVert = countX <= countY;

        Block[][] result = new Block[countY][countX];
        
        if (countX == 1){
            result = fillStrategy.fill(countX, countY);
        }
        
        
        if (isVert){
            // находим центр, округляем в верх для того что бы сгенерить и центральную полосу
            int center = (int) Math.ceil((double) countX / 2);
            boolean centerWithCopy = center == (countX / 2);
            for (int i = 0; i <= center-1; i++) {
                // левая полоса
                Block[][] leftLine = fillStrategy.fill(1, countY, i == center-1);
                partColorStrategy.fillColor(leftLine);
                merge(result, leftLine, new SizeXY(i,0), new SizeXY(i+1,countY));

                if (i == center-1 && !centerWithCopy){
                    continue;
                }
                
                // скопировать зеркально 
                Block[][] rightLine = copyStrategy.copy(leftLine, CopyDirection.TO_RIGHT);
                // и поместить направо, симметрично центра
                merge(result, rightLine, new SizeXY(countX-i-1,0), new SizeXY(countX-i,countY));
            }
        } else {
            // находим центр, округляем в верх для того что бы сгенерить и центральную полосу
            int center = (int) Math.ceil((double) countY / 2);
            boolean centerWithCopy = center == (countY / 2);
            for (int i = 0; i <= center-1; i++) {
                // верхняя полоса
                Block[][] topLine = fillStrategy.fill(countX, 1, i == center-1);
                partColorStrategy.fillColor(topLine);
                merge(result, topLine, new SizeXY(0, i), new SizeXY(countX, i+1));
                if (i == center-1 && !centerWithCopy){
                    continue;
                }

                // скопировать зеркально 
                Block[][] bottomLine = copyStrategy.copy(topLine, CopyDirection.TO_DOWN);
                // и поместить вниз, симметрично центра
                merge(result, bottomLine, new SizeXY(0, countY-i-1), new SizeXY(countX,countY-i));
            }
        }
        
        return result;
    }
}
