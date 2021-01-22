package ru.pilot.pathwork.fill.sector;

import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.patchwork.Block;

public interface SectorStrategy {
    Block[][] parting(int countX, int countY, ColorSupplier partColorStrategy);
}
