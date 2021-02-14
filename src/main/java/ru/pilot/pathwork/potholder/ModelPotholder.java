package ru.pilot.pathwork.potholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import ru.pilot.pathwork.block.Block;
import ru.pilot.pathwork.fill.Filler;

public class ModelPotholder {
    
    public static final ModelPotholder INSTANCE = new ModelPotholder();
    
    private Pane currentPane = null;
    
    private ModelPotholder(){
        for (ModelType modelType : ModelType.values()) {
            modelMap.put(modelType, new ConcurrentHashMap<>());
        }
    }
    
    private final Map<ModelType, Map<String, Block[][]>> modelMap = new HashMap<>();
    private final Set<Paint> paints = new HashSet<>();
    
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
    
    public void replaceBlock(ModelType modelType, String name, Block block, int i, int j){
        Block[][] blocks = get(modelType, name);
        blocks[i][j] = block;
        savePaint(blocks);
    }
    
    private void savePaint(Block[][] blocks){
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
    
    public void replacePaintBlock(Block block, Paint oldPaint, Paint newPaint){
        for (Map.Entry<String, Block[][]> byName : modelMap.get(ModelType.MAIN).entrySet()) {
            Block[][] blocks = byName.getValue();
            for (Block[] blockLine : blocks) {
                for (Block blockItem : blockLine) {
                    if (blockItem.equals(block) || blockItem.getBlocks().contains(block)){
                        block.replaceColor(oldPaint, newPaint);
                    }
                }
            }
            savePaint(blocks);
        }
        refill();
    }
    
    public Paint getNextPaint(Paint currentPaint){
        if (paints.contains(currentPaint)){
            ArrayList<Paint> paints = new ArrayList<>(this.paints);
            int i = paints.indexOf(currentPaint);
            i++;
            return paints.get(i < paints.size() ? i : i % paints.size());
        } else {
            return paints.iterator().next();
        }
    }

    public void refill(){
        new Filler().fillBlock(getMainGroup(), get(ModelType.MAIN, null));
    }
    
    public void setCurrentPane(Pane currentPane){
        this.currentPane = currentPane;
    }

    private Group getMainGroup() {
        return (Group) currentPane.getChildren().filtered(node -> node.getClass().equals(Group.class)).get(0);
    }
    
}
