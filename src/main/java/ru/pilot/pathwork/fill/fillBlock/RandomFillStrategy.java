package ru.pilot.pathwork.fill.fillBlock;

import java.util.List;
import java.util.Random;

import ru.pilot.pathwork.block.Block;
import ru.pilot.pathwork.block.RectangleBlock;

public class RandomFillStrategy implements FillStrategy {

    private static final Random rnd = new Random();


    public Block[][] fill(int countX, int countY, boolean onlySymmetry){
        if (countX < 1 || countY < 1){
            return new Block[countX][countY];
        }

        List<Block> activeBlocks = onlySymmetry ? Block.getActiveSymmetryBlocks() : Block.getActiveBlocks();
        if (activeBlocks.isEmpty()){
            activeBlocks.add(new RectangleBlock());
        }

        Block[][] blocks = new Block[countY][countX];
        for (int y = 0; y < countY; y++) {
            for (int x = 0; x <countX; x++) {
                Block block = activeBlocks.get(rnd.nextInt(activeBlocks.size()));
                blocks[y][x] = block.copy();
            }
        }
        return blocks;
    }
    
    public Block[][] fill(int countX, int countY){
        return fill(countX, countY, false);
    }
    
}
