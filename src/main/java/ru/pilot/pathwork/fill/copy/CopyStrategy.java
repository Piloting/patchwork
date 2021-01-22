package ru.pilot.pathwork.fill.copy;

import ru.pilot.pathwork.fill.copy.CopyDirection;
import ru.pilot.pathwork.patchwork.Block;

public interface CopyStrategy {
    Block[][] copy(Block[][] blocks, CopyDirection direction);
}
