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
import ru.pilot.pathwork.block.Block;

public class Filler {

    public static final RandomByColorSetColorSupplier DEFAULT_COLORS = new RandomByColorSetColorSupplier(Arrays.asList(
            new Color(0.73, 1, 1, 1),
            new Color(0.73, 1, 0.82, 1),
            new Color(0.9, 1, 0.73, 1),
            new Color(1, 0.73, 0.73, 1),
            new Color(1, 0.73, 0.95, 1)
    ));
    private final SectorStrategy sectorStrategy;
    private final ColorSupplier colorSupplier;

    public Filler(){
        this.sectorStrategy = new CrossSectorStrategy(new RandomFillStrategy(), 20);
        //new NoSectorStrategy(new RandomFillStrategy()),
        this.colorSupplier = DEFAULT_COLORS;
    }
    
    public Filler(SectorStrategy sectorStrategy, ColorSupplier colorSupplier ){
        this.sectorStrategy = sectorStrategy;
        this.colorSupplier = colorSupplier != null ? colorSupplier : DEFAULT_COLORS;
    }
    
    public Filler(SectorStrategy sectorStrategy){
        this.sectorStrategy = sectorStrategy != null ? sectorStrategy : new CrossSectorStrategy(new RandomFillStrategy(), 20);
        this.colorSupplier = DEFAULT_COLORS;
    }

    public void fillBlock(Group group, Block[][] blocks){
        // почистить
        for (Node child : group.getChildren()) {
            if (child instanceof Pane){
                ((Pane) child).getChildren().clear();
            }
        }
        
        // напихать на форму
        setToPane(group, blocks);
    }
    
    public Block[][] fillBlock(Group group){
        // посчитать сколько панелей
        int countX = 0, countY = 0;
        for (Node child : group.getChildren()) {
            if (child instanceof Pane){
                countY = Math.max(getI(child), countY);
                countX = Math.max(getJ(child), countX);
            }
        }
        countX++; countY++;

        // создание блоков
        Block[][] blocks = createBlock(countX, countY,
                sectorStrategy,
                colorSupplier, 
                null);

        // напихать на форму
        setToPane(group, blocks);

        return blocks;
    }

    private void setToPane(Group group, Block[][] blocks) {
        for (Node child : group.getChildren()) {
            if (child instanceof Pane) {
                Pane pane = (Pane) child;

                int y = getI(pane);
                int x = getJ(pane);

                Block block = blocks[y][x];
                pane.getChildren().add(block.getNode(pane.getPrefWidth(), pane.getPrefHeight()));
            }
        }
    }

    public Block[][] createBlock(int countX, int countY, SectorStrategy sectorStrategy, ColorSupplier partColorStrategy, ColorSupplier fullColorStrategy){
        Block[][] parting = sectorStrategy.parting(
                countX, 
                countY, 
                partColorStrategy);

        if (fullColorStrategy != null){
            fullColorStrategy.fillColor(parting);
        }
        
        return parting;
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
