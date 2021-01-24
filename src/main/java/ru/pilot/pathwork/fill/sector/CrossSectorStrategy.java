package ru.pilot.pathwork.fill.sector;

import org.apache.commons.lang3.StringUtils;
import ru.pilot.pathwork.SizeXY;
import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.fill.copy.CopyDirection;
import ru.pilot.pathwork.fill.copy.CopyStrategy;
import ru.pilot.pathwork.fill.copy.MirrorCopyStrategy;
import ru.pilot.pathwork.fill.fillBlock.FillStrategy;
import ru.pilot.pathwork.patchwork.Block;

/**
 *   ------------------------------
 *   .  .  .  .  T  T  .  .  .  . 
 *   .  .  .  .  T  T  .  .  .  . 
 *   .  .  .  .  T  T  .  .  .  . 
 *   .  .  .  .  T  T  .  .  .  . 
 *   L  L  L  L  C  C  R  R  R  R 
 *   L  L  L  L  C  C  R  R  R  R 
 *   .  .  .  .  B  B  .  .  .  . 
 *   .  .  .  .  B  B  .  .  .  . 
 *   .  .  .  .  B  B  .  .  .  . 
 *   .  .  .  .  B  B  .  .  .  . 
 *   ------------------------------
 **/
public class CrossSectorStrategy implements SectorStrategy {

    private final FillStrategy fillStrategy;
    private final LineSectorStrategy lineSectorStrategy;
    private final CopyStrategy copyStrategy = new MirrorCopyStrategy();
    private final double withCrossPercent;
    
    public CrossSectorStrategy(FillStrategy fillStrategy, double withCrossPercent){
        this.fillStrategy = fillStrategy;
        this.withCrossPercent = withCrossPercent;
        this.lineSectorStrategy = new LineSectorStrategy(fillStrategy);
    }
    public Block[][] parting(int countX, int countY, ColorSupplier partColorStrategy){
        return parting(countX, countY, partColorStrategy, withCrossPercent);
    }
    
    public Block[][] parting(int countX, int countY, ColorSupplier partColorStrategy, double withCrossPercent){
        if (countX<=0 || countY<=0){
            return new Block[0][0];
        } else if (countX==1 || countY==1){
            Block[][] center = fillStrategy.fill(1, 1, true);
            partColorStrategy.fillColor(center);
            return center;
        }
        
        // вычисление координат частей картинки
        int crossWidthX = getCenter(countX,  withCrossPercent);
        int sideSpaceX = sideSpace(countX, crossWidthX);
        
        int crossWidthY = getCenter(countY,  withCrossPercent);
        int sideSpaceY = sideSpace(countY, crossWidthY);
        
        pseudoPaint(crossWidthX, sideSpaceX, crossWidthY, sideSpaceY);

        // генерация space блоков
        SizeXY leftTopSize = new SizeXY(sideSpaceX, sideSpaceY);
        Block[][] leftTopSpaceBlocks = fillStrategy.fill(leftTopSize.getX(), leftTopSize.getY());
        partColorStrategy.fillColor(leftTopSpaceBlocks);
        Block[][] rightTopSpaceBlock = copyStrategy.copy(leftTopSpaceBlocks, CopyDirection.TO_RIGHT);
        Block[][] leftBottomSpaceBlock = copyStrategy.copy(leftTopSpaceBlocks, CopyDirection.TO_DOWN);
        Block[][] rightBottomSpaceBlock = copyStrategy.copy(leftBottomSpaceBlock, CopyDirection.TO_RIGHT);

        // генерация блоков креста
        Block[][] topCrossBlocks = lineSectorStrategy.parting(crossWidthX, sideSpaceY, partColorStrategy);
        Block[][] bottomCrossBlock = copyStrategy.copy(topCrossBlocks, CopyDirection.TO_DOWN);
        Block[][] leftCrossBlock = lineSectorStrategy.parting(sideSpaceX, crossWidthY, partColorStrategy);
        Block[][] rightCrossBlock = copyStrategy.copy(leftCrossBlock, CopyDirection.TO_RIGHT);

        Block[][] centerBlock = parting(countX - sideSpaceX*2, countY - sideSpaceY*2, partColorStrategy, 0);
        //Block[][] centerBlock = fillStrategy.fill(countX - sideSpaceX*2, countY - sideSpaceY*2, true);
        //partColorStrategy.fillColor(centerBlock);
        
        // объединить
        Block[][] result = new Block[countY][countX];
        // верхняя часть
        merge(result, leftTopSpaceBlocks, new SizeXY(0, 0), new SizeXY(sideSpaceX, sideSpaceY));
        merge(result, topCrossBlocks, new SizeXY(sideSpaceX, 0), new SizeXY(sideSpaceX+crossWidthX, sideSpaceY));
        merge(result, rightTopSpaceBlock, new SizeXY(sideSpaceX+crossWidthX, 0), new SizeXY(sideSpaceX+crossWidthX+sideSpaceX, sideSpaceY));
        
        // средняя часть
        merge(result, leftCrossBlock, new SizeXY(0, sideSpaceY), new SizeXY(sideSpaceX, sideSpaceY+crossWidthY));
        merge(result, centerBlock, new SizeXY(sideSpaceX, sideSpaceY), new SizeXY(sideSpaceX+crossWidthX, sideSpaceY+crossWidthY));
        merge(result, rightCrossBlock, new SizeXY(sideSpaceX+crossWidthX, sideSpaceY), new SizeXY(sideSpaceX+crossWidthX+sideSpaceX, sideSpaceY+crossWidthY));
        
        // нижняя часть
        merge(result, leftBottomSpaceBlock, new SizeXY(0, sideSpaceY+crossWidthY), new SizeXY(sideSpaceX, sideSpaceY+crossWidthY+sideSpaceY));
        merge(result, bottomCrossBlock, new SizeXY(sideSpaceX, sideSpaceY+crossWidthY), new SizeXY(sideSpaceX+crossWidthX, sideSpaceY+crossWidthY+sideSpaceY));
        merge(result, rightBottomSpaceBlock, new SizeXY(sideSpaceX+crossWidthX, sideSpaceY+crossWidthY), new SizeXY(sideSpaceX+crossWidthX+sideSpaceX, sideSpaceY+crossWidthY+sideSpaceY));
        
        return result;
    }

    private void pseudoPaint(int crossWidthX, int sideSpaceX, int crossWidthY, int sideSpaceY) {
        String space = " . ";
        String cross = " X ";
        
        System.out.println(StringUtils.repeat("-", space.length() * (sideSpaceX+crossWidthX+sideSpaceX)));
        pseudoPrint(crossWidthX, sideSpaceX, sideSpaceY, space, cross);
        pseudoPrint(crossWidthX, sideSpaceX, crossWidthY, cross, cross);
        pseudoPrint(crossWidthX, sideSpaceX, sideSpaceY, space, cross);
        System.out.println(StringUtils.repeat("-", space.length() * (sideSpaceX+crossWidthX+sideSpaceX)));
    }

    private void pseudoPrint(int crossWidthX, int sideSpaceX, int sideSpaceY, String space, String cross) {
        for (int i = 0; i < sideSpaceY; i++) {
            for (int j = 0; j < sideSpaceX; j++) {
                System.out.print(space);
            }
            for (int j = 0; j < crossWidthX; j++) {
                System.out.print(cross);
            }
            for (int j = 0; j < sideSpaceX; j++) {
                System.out.print(space);
            }
            System.out.println();
        }
    }

    private int getCenter(int count, double withCrossPercent){
        int crossWidth = (int) (count * withCrossPercent / 100);
        if ((count - crossWidth) % 2 != 0){
            int add = 1;
            if (crossWidth > 1){
                add = -1;
            }
            crossWidth += add;
        }
        return crossWidth;
    }
    private int sideSpace(int count, int crossWidth){
        return (count - crossWidth) / 2;
    }
    
}
