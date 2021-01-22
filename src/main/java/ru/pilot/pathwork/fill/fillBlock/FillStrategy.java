package ru.pilot.pathwork.fill.fillBlock;

import ru.pilot.pathwork.patchwork.Block;

public interface FillStrategy {
    Block[][] fill(int countX, int countY);
}
