package ru.pilot.pathwork.fill.fillBlock;

import ru.pilot.pathwork.block.Block;

public interface FillStrategy {
    Block[][] fill(int countX, int countY);
    Block[][] fill(int countX, int countY, boolean onlySymmetry);
}
