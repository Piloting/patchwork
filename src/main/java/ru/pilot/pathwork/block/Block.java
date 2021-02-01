package ru.pilot.pathwork.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import ru.pilot.pathwork.color.ColorSupplier;

public interface Block {
    
    Node getNode(double width, double height);
    String getType();
    Block copy();

    Block mirror(boolean isX, boolean isY);

    List<? extends Block> getBlocks();
    void setPaint(int i, int j, ColorSupplier colorSupplier);
    void setInnerPaint(Paint paint);
    List<Paint> getColors();
    void transform(Transform transform);
    
    Map<String, Block> allBlocks = new HashMap<>();
    List<Block> symmetryBlocks = new ArrayList<>();
    List<Block> activeBlocks = new ArrayList<>();
    
    boolean isCenterSymmetry();
    boolean isReadyMade();

    @SneakyThrows
    static Map<String, Block> getAllBlockMap(){
        if (allBlocks.isEmpty()){
            Reflections reflections = new Reflections("ru.pilot.pathwork.block");
            Set<Class<? extends Block>> classes = reflections.getSubTypesOf(Block.class);
            for (Class<? extends Block> blockClass : classes) {
                Block block = blockClass.newInstance();
                if (block.isReadyMade()){
                    allBlocks.put(blockClass.getSimpleName(), block);
                }
            }
        }
        return allBlocks;
    }
    
    static List<Block> getAllBlocks(){
        return new ArrayList<>(allBlocks.values());
    }
    
    static List<Block> getActiveSymmetryBlocks(){
        if (symmetryBlocks.isEmpty()){
            for (Block block : getActiveBlocks()) {
                if (block.isCenterSymmetry()){
                    symmetryBlocks.add(block);
                }
            }
        }
        return symmetryBlocks;
    }

    static List<Block> getActiveBlocks(){
        if (activeBlocks.isEmpty()){
            activeBlocks.addAll(getAllBlocks());
        }
        return activeBlocks;
    }
    static void addActiveBlocks(Block block){
        activeBlocks.add(block);
    }
    static void removeActiveBlocks(Block block){
        activeBlocks.remove(block);
    }

    void replaceColor(Paint oldPaint, Paint newPaint);
}
