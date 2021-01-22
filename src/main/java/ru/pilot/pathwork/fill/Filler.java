package ru.pilot.pathwork.fill;


import java.util.Arrays;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.color.RandomByColorSetColorSupplier;
import ru.pilot.pathwork.fill.fillBlock.RandomFillStrategy;
import ru.pilot.pathwork.fill.sector.CrossSectorStrategy;
import ru.pilot.pathwork.fill.sector.SectorStrategy;
import ru.pilot.pathwork.patchwork.Block;

public class Filler {
    
    private final SectorStrategy sectorStrategy;
    private final ColorSupplier colorSupplier;

    public Filler(){
        this.sectorStrategy = new CrossSectorStrategy(new RandomFillStrategy(), 10);
        //new NoSectorStrategy(new RandomFillStrategy()),
        this.colorSupplier = new RandomByColorSetColorSupplier(Arrays.asList(
                new Color(0.73, 1, 1, 1),
                new Color(0.73, 1, 0.82, 1),
                new Color(0.9, 1, 0.73, 1),
                new Color(1, 0.73, 0.73, 1),
                new Color(1, 0.73, 0.95, 1)
        ));
    }
    
    public Filler(SectorStrategy sectorStrategy, ColorSupplier colorSupplier ){
        this.sectorStrategy = sectorStrategy;
        this.colorSupplier = colorSupplier;
    }
    
    public Filler(SectorStrategy sectorStrategy){
        this.sectorStrategy = sectorStrategy != null ? sectorStrategy : new CrossSectorStrategy(new RandomFillStrategy(), 50);
        this.colorSupplier = new RandomByColorSetColorSupplier(Arrays.asList(
                new Color(0.73, 1, 1, 1),
                new Color(0.73, 1, 0.82, 1),
                new Color(0.9, 1, 0.73, 1),
                new Color(1, 0.73, 0.73, 1),
                new Color(1, 0.73, 0.95, 1)
        ));
    }

    public void fillBlock(Group group){
        fillBlock(group,
                sectorStrategy,
                colorSupplier, 
                null);
    }
    
    public void fillBlock(Group group, SectorStrategy sectorStrategy, ColorSupplier partColorStrategy, ColorSupplier fullColorStrategy){
        int countX = 0, countY = 0;
        for (Node child : group.getChildren()) {
            if (child instanceof Pane){
                countY = Math.max(getI(child), countY);
                countX = Math.max(getJ(child), countX);
                ((Pane) child).getChildren().clear();
            }
        }

        countX++; countY++;
        
        Block[][] parting = sectorStrategy.parting(countX, countY, partColorStrategy);

        // напихать на форму
        for (Node child : group.getChildren()) {
            if (child instanceof Pane){
                Pane pane = (Pane) child;
                
                int y = getI(pane);
                int x = getJ(pane);
                
                Block block = parting[y][x];
                pane.getChildren().add(block.getNode(pane.getPrefWidth(), pane.getPrefHeight()));
            }
        }
    }


    private int getI(Node node) {
        String[] ij = StringUtils.split(node.getId(), "_");
        return ij.length == 2 ? getInt(ij[0]) : 0;
    }

    private int getJ(Node node) {
        String[] ij = StringUtils.split(node.getId(), "_");
        return ij.length == 2 ? getInt(ij[1]) : 0;
    }
    
    private int getInt(String text){
        if (NumberUtils.isCreatable(text)) {
            return NumberUtils.createInteger(text);
        }
        return 0;
    }
    
}
