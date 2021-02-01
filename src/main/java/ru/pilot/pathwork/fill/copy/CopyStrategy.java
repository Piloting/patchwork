package ru.pilot.pathwork.fill.copy;

import ru.pilot.pathwork.block.Block;

public interface CopyStrategy {
    Block[][] copy(Block[][] blocks, CopyDirection direction);
}
