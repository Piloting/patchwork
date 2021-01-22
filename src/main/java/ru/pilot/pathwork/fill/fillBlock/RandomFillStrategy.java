package ru.pilot.pathwork.fill.fillBlock;

import java.util.List;
import java.util.Random;

import ru.pilot.pathwork.patchwork.Block;

public class RandomFillStrategy implements FillStrategy {

    private static final Random rnd = new Random();
    
    public Block[][] fill(int countX, int countY){
        if (countX < 1 || countY < 1){
            return new Block[countX][countY];
        }
        
        List<Block> exampleBlocks = Block.getExampleBlocks();

        Block[][] blocks = new Block[countY][countX];
        for (int y = 0; y < countY; y++) {
            for (int x = 0; x <countX; x++) {
                Block block = exampleBlocks.get(rnd.nextInt(exampleBlocks.size()));
                blocks[y][x] = block.copy();
            }
        }
        return blocks;
    }
    
}
