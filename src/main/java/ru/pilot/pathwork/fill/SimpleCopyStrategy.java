package ru.pilot.pathwork.fill;

import ru.pilot.pathwork.block.Block;

public class SimpleCopyStrategy {
    
    public Block[][] copy(Block[][] blocks){
        int countX = blocks.length;
        int countY = blocks[0].length;
        
        Block[][] newBlocks = new Block[countY][countX];

        for (int y = 0; y < countY; y++) {
            for (int x = 0; x < countX; x++) {
                Block block = blocks[y][x];
                newBlocks[y][x] = block.copy();
            }
        }
        return newBlocks;
    }
}
