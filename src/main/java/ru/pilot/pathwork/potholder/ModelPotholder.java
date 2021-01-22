package ru.pilot.pathwork.potholder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.pilot.pathwork.patchwork.Block;

public class ModelPotholder {
    
    private final Map<ModelType, Map<String, Block[][]>> modelMap = new HashMap<>();
    
    public ModelPotholder(){
        this.modelMap.put(ModelType.MAIN, new ConcurrentHashMap<>());
        this.modelMap.put(ModelType.EXAMPLE, new ConcurrentHashMap<>());
        this.modelMap.put(ModelType.LIKE, new ConcurrentHashMap<>());
    }
    
    
}
