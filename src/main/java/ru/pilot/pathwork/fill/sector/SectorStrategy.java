package ru.pilot.pathwork.fill.sector;

import ru.pilot.pathwork.SizeXY;
import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.block.Block;

public interface SectorStrategy {
    Block[][] parting(int countX, int countY, ColorSupplier partColorStrategy);

    default void merge(Block[][] result, Block[][] blocks, SizeXY startXY, SizeXY endXY) {
        int countX = endXY.getX() - startXY.getX();
        int countY = endXY.getY() - startXY.getY();

        for (int y = 0; y < countY; y++) {
            for (int x = 0; x < countX; x++) {
                int localY = startXY.getY() + y;
                int localX = startXY.getX() + x;
                result[localY][localX] = blocks[y][x];
            }
        }
    }
}
