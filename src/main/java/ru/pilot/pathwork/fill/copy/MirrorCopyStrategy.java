package ru.pilot.pathwork.fill.copy;

import ru.pilot.pathwork.patchwork.Block;

public class MirrorCopyStrategy implements CopyStrategy {
    
    public Block[][] copy(Block[][] blocks, CopyDirection direction){
        int countY = blocks.length;
        if (countY == 0){
            return new Block[0][0];
        }
        int countX = blocks[0].length;
        
        Block[][] newBlocks = direction != CopyDirection.TO_DIAGONAL_LEFT ? new Block[countY][countX] : new Block[countX][countY];

        for (int y = 0; y < countY; y++) {
            for (int x = 0; x < countX; x++) {
                Block block = blocks[y][x];
                switch (direction){
                    case TO_LEFT:
                    case TO_RIGHT:
                        newBlocks[y][countX - x - 1] = block.mirror(true, false);
                        break;
                    case TO_UP:
                    case TO_DOWN:
                        newBlocks[countY - y - 1][x] = block.mirror(false, true);
                        break;
                    case TO_DIAGONAL_LEFT:
                        newBlocks[countY - y - 1][countX - x - 1] = block.mirror(true, true);
                }
            }
        }
        return newBlocks;
    }
}
