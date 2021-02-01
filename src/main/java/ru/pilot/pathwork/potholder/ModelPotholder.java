package ru.pilot.pathwork.potholder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.paint.Paint;
import ru.pilot.pathwork.block.Block;

public class ModelPotholder {
    
    private final Map<ModelType, Map<String, Block[][]>> modelMap = new HashMap<>();
    private Set<Paint> paints = new HashSet<>();
    
    public ModelPotholder(){
        for (ModelType modelType : ModelType.values()) {
            modelMap.put(modelType, new ConcurrentHashMap<>());
        }
    }
    
    public void add(ModelType modelType, String name, Block[][] blocks){
        modelMap.get(modelType).put(name, blocks);
        if (modelType == ModelType.MAIN){
            savePaint(blocks);
        }
    }
    
    public Block[][] get(ModelType modelType, String name){
        Map<String, Block[][]> stringMap = modelMap.get(modelType);
        if (name != null) {
            return stringMap.get(name);
        }
        return stringMap.values().iterator().next();
    }
    
    public void savePaint(Block[][] blocks){
        paints.clear();
        for (Block[] blockLine : blocks) {
            for (Block block : blockLine) {
                paints.addAll(block.getColors());
            }
        }
    }
    
    public Set<Paint> getPaints(){
        return paints;
    }
    
    public void replacePaint(Paint oldPaint, Paint newPaint){
        for (Map.Entry<String, Block[][]> byName : modelMap.get(ModelType.MAIN).entrySet()) {
            Block[][] blocks = byName.getValue();
            for (Block[] blockLine : blocks) {
                for (Block block : blockLine) {
                    block.replaceColor(oldPaint, newPaint);
                }
            }
        }
    }
    
}
