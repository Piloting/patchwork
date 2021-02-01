package ru.pilot.pathwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import lombok.Data;
import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.block.Block;
import ru.pilot.pathwork.block.RectangleBlock;

@Data
public class Patchwork {

    public static final Random rnd = new Random();
    
    private final double width;
    private final double height;
    private final int wBlockSize;
    private final int hBlockSize;
    private final int wBlockCount;
    private final int hBlockCount;
    private List<Block> blockList = null;
    private ColorSupplier colorSupplier;


    public Patchwork(double width, double height, double wBlockSize, double hBlockSize, ColorSupplier colorSupplier) {
        if (width < wBlockSize){
            throw new RuntimeException("Ширина блока больше ширины полотна");
        }
        if (height < hBlockSize){
            throw new RuntimeException("Высота блока больше высоты полотна");
        }

        this.wBlockSize = (int) Math.round(wBlockSize);
        this.wBlockCount = (int) Math.round(width/this.wBlockSize);

        this.hBlockSize = (int) Math.round(hBlockSize);
        this.hBlockCount = (int) Math.round(height/this.hBlockSize);

        this.width = width;
        this.height = height;

        this.colorSupplier = colorSupplier;

        createBlockList();
    }
    
    private void createBlockList(){
        blockList = new ArrayList<>(hBlockCount*wBlockCount);
        for (int h = 0; h <= hBlockCount; h=h+1) {
            for (int w = 0; w <= wBlockCount; w=w+1) {
                RectangleBlock rectangle = new RectangleBlock(colorSupplier.getColor(h*hBlockSize, w*wBlockSize, hBlockSize, wBlockSize));
                Node node = rectangle.getNode(wBlockSize, hBlockSize);
                node.setLayoutY(h*hBlockSize);
                node.setLayoutX(w*wBlockSize);
                blockList.add(rectangle);
            }
        }
    }
    
    public void fillBlockList(Group group){
        group.getChildren().clear();
        for (Block block : blockList) {
            group.getChildren().add(block.getNode(wBlockSize, hBlockSize));
        }
    }


    private Color getRgb() {
        return Color.rgb(rndCol(), rndCol(), rndCol());
    }
    private int rndCol(){
        return rnd.nextInt(255);
    }
    
}
