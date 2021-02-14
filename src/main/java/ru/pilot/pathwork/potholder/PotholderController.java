package ru.pilot.pathwork.potholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import ru.pilot.pathwork.ControlUtils;
import ru.pilot.pathwork.MiscUtils;
import ru.pilot.pathwork.SizeXY;
import ru.pilot.pathwork.color.ColorPickerController;
import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.color.MyColorSetController;
import ru.pilot.pathwork.color.RandomByColorSetColorSupplier;
import ru.pilot.pathwork.fill.Filler;
import ru.pilot.pathwork.fill.fillBlock.RandomFillStrategy;
import ru.pilot.pathwork.fill.sector.CrossSectorStrategy;
import ru.pilot.pathwork.fill.sector.LineSectorStrategy;
import ru.pilot.pathwork.fill.sector.NoSectorStrategy;
import ru.pilot.pathwork.fill.sector.SectorStrategy;
import ru.pilot.pathwork.name.NamingFactory;
import ru.pilot.pathwork.block.Block;

/*
Цвета

Ответы на вопросы
https://coderoad.ru/60721123/Уменьшите-изображение-до-фиксированного-числа-заданных-цветов-в-JAVA
https://qna.habr.com/q/20282

Статьи
https://habr.com/ru/post/136530/
https://habr.com/ru/post/137868/

перевод между color spaces
http://www.easyrgb.com/en/math.php


 */



public class PotholderController {

    private final ModelPotholder model = ModelPotholder.INSTANCE;
    private final String FORM_COLOR_CHOICE = "colorChoice.fxml";
    private final String FORM_MY_COLOR_SET = "myColorSet.fxml";
    
    public Pane smallSidePane;
    public Pane bigSidePane;
    private final Map<String, Pane> miniPaneIdMap = new HashMap<>(36);
    private final Map<String, Pane> frontBlockIdMap = new HashMap<>(36);

    public Pane blockTypePane;
    public Pane myColorPane;
    
    public Button genButton;
    
    public Accordion sizeAccordion;
    public TitledPane sizeAccordionPane;
    
    public Accordion colorAccordion;
    public TitledPane currentColorAccordionPane;
    public TitledPane myColorAccordionPane;
    
    public Pane simpleBlockPane;
    public RadioButton randomPatternRB;
    public RadioButton sectorPatternRB;
    public RadioButton crossPatternRB;
    public RadioButton linePatternRB;
    public Slider crossSizePercentSlider;
    public VBox currentColorVBox;
    
    @FXML
    private void initialize() {
        model.setCurrentPane(bigSidePane);
        fillActiveBlockPane();
        createSizePane(new SizeXY(8,8), smallSidePane, miniPaneIdMap, bigSidePane, frontBlockIdMap);
        generateSimples(new SizeXY(4,4), miniPaneIdMap, frontBlockIdMap);
        sizeAccordion.setExpandedPane(sizeAccordionPane);
        colorAccordion.setExpandedPane(currentColorAccordionPane);
        
        Group group = getMainGroup();
        new ControlUtils().sizeAndMoveEvents(group);
        
        groupRadio();

        structureGenerate();
    }

    private Group getMainGroup() {
        return (Group) bigSidePane.getChildren().filtered(node -> node.getClass().equals(Group.class)).get(0);
    }

    private void groupRadio() {
        ToggleGroup group = new ToggleGroup();
        randomPatternRB.setToggleGroup(group);
        sectorPatternRB.setToggleGroup(group);
        crossPatternRB.setToggleGroup(group);
        linePatternRB.setToggleGroup(group);
    }

    private void fillActiveBlockPane() {
        Map<String, Block> allBlockMap = Block.getAllBlockMap();
        
        for (Map.Entry<String, Block> entry : allBlockMap.entrySet()) {
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);
            checkBox.setId(entry.getKey());
            checkBox.setGraphic(entry.getValue().getNode(50, 50));
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue){
                    Block.addActiveBlocks(allBlockMap.get(checkBox.getId()));
                } else {
                    Block.removeActiveBlocks(allBlockMap.get(checkBox.getId()));
                }
            });
            blockTypePane.getChildren().add(checkBox);
        }
    }

    private void createSizePane(SizeXY blockCount, Pane pane, Map<String, Pane> miniPaneIdMap, Pane bigSidePane, Map<String, Pane> bigPaneIdMap) {
        Group smallGroup = createBlocks(blockCount, pane, miniPaneIdMap, 10, 2, false);
        for (Node child : smallGroup.getChildren()) {
            child.setOnMouseClicked(event -> {
                Pane source = (Pane) event.getSource();
                blockResize(blockCount, miniPaneIdMap, bigSidePane, bigPaneIdMap, source);
            });
        }

        Pane pane33 = (Pane)smallGroup.getChildren().stream().filter(node -> node.getId().equals("3_3")).findFirst().orElse(null);
        blockResize(blockCount, miniPaneIdMap, bigSidePane, bigPaneIdMap, pane33);
    }

    private void generateSimples(SizeXY blockCount, Map<String, Pane> miniPaneIdMap, Map<String, Pane> frontBlockIdMap) {
        Filler filler = new Filler();
        for (int i = 0; i < 10; i++) {
            Pane simplePane = new Pane(); 
            simplePane.setPrefWidth(150);
            simplePane.setPrefHeight(150);
            Group groupBlocks = createBlocks(blockCount, simplePane, new HashMap<>(), 0, 0, false);
            Block[][] blocks = filler.fillBlock(groupBlocks);
            model.add(ModelType.EXAMPLE, groupBlocks.getId(), blocks);
            simpleBlockPane.getChildren().add(simplePane);

            groupBlocks.setOnMouseClicked(event -> {
                if (event.getClickCount() < 2) {
                    return;
                }
                
                Block[][] selectBlocks = model.get(ModelType.EXAMPLE, groupBlocks.getId());
                SizeXY localBlockCount = new SizeXY(selectBlocks.length, selectBlocks[0].length);
                
                Pane source = new Pane();
                source.setId((localBlockCount.getY()-1) + "_" + (localBlockCount.getX()-1));
                Group group = blockResize(localBlockCount, miniPaneIdMap, bigSidePane, frontBlockIdMap, source);
                new Filler().fillBlock(group, selectBlocks);
            });
        }
    }

    private Group blockResize(SizeXY blockCount, Map<String, Pane> miniPaneIdMap, Pane bigSidePane, Map<String, Pane> bigPaneIdMap, Pane source) {
        paintSmallBlock(source, miniPaneIdMap, blockCount);
        Group bigBlocks = createBlocks(blockCount, bigSidePane, bigPaneIdMap, 0, 0, true);
        addEventChangeBlockType(bigBlocks);
        return bigBlocks;
    }

    private void addEventChangeBlockType(Group bigBlocks){
        List<Block> activeBlockList = Block.getActiveBlocks();
        for (Node child : bigBlocks.getChildren()) {
            child.setOnMouseClicked(event -> {
                if (MouseButton.PRIMARY != event.getButton()){
                    return;
                }
                
                Pane source = (Pane) event.getSource();
                
                Optional<String> label = source.getChildren().filtered(this::isLabel).stream().map(Node::getId).findFirst();
                String existBlockType = label.orElse(null);
                
                Block block;
                int index = 0;
                if (existBlockType != null){
                    index = MiscUtils.getInt(existBlockType);
                    block = getNext(activeBlockList, index++);
                } else {
                    block = activeBlockList.iterator().next();
                }
                
                if (block == null){
                    return;
                }
                block = block.copy();
                
                Block[][] blocks = new Block[1][1];
                blocks[0][0] = block;
                ColorSupplier colorSupplier = getColorSupplier();
                if (colorSupplier != null) {
                    colorSupplier.fillColor(blocks);  
                } 
                fillBlock(source, block, index);
                
                // замена в модели
                String[] ij = StringUtils.split(source.getId(), "_");
                int curr_i = MiscUtils.getInt(ij[0]);
                int curr_j = MiscUtils.getInt(ij[1]);
                model.replaceBlock(ModelType.MAIN, "MAIN", block, curr_i, curr_j);
                currentColorBoxInit();
            });
        }
    }
    
    private Block getNext(List<Block> blockList, int currentIndex){
        if (CollectionUtils.isEmpty(blockList)){
            return null;
        }
        if (currentIndex >= blockList.size()){
            currentIndex = currentIndex % blockList.size();
        }
        return blockList.get(currentIndex);
    }

    private boolean isLabel(Node node) {
        return node.getClass().getSimpleName().equals(Label.class.getSimpleName());
    }

    public void structureGenerate(){
        Group group = getMainGroup();
        
        SectorStrategy sectorStrategy = null;
        if (crossPatternRB.isSelected()){
            sectorStrategy = new CrossSectorStrategy(new RandomFillStrategy(), crossSizePercentSlider.getValue());
        } else if (sectorPatternRB.isSelected()){
            
        }  else if (linePatternRB.isSelected()){
            sectorStrategy = new LineSectorStrategy(new RandomFillStrategy());
        } else {
            sectorStrategy = new NoSectorStrategy(new RandomFillStrategy());
        }

        ColorSupplier colorSupplier = getColorSupplier();
        Filler filler = new Filler(sectorStrategy, colorSupplier);
        
        Block[][] blocks = filler.fillBlock(group);
        model.add(ModelType.MAIN, "MAIN", blocks);
        currentColorBoxInit();
    }
    

    public void openMyColorSet(){
        List<Paint> paintList = new ArrayList<>(20);
        for (Node child : myColorPane.getChildren()) {
            if (child instanceof Rectangle){
                paintList.add(((Rectangle)child).getFill());
            }
        }

        MyColorSetController.setPaints(paintList);
        ControlUtils.openForm(FORM_MY_COLOR_SET, "Свой набор");
        List<Paint> paints = MyColorSetController.getPaints();
        if (!paints.isEmpty()){
            ObservableList<Node> children = myColorPane.getChildren();
            children.clear();
            double width = myColorPane.getWidth()-50;
            children.add(new Label("Количество: " + paints.size() + " шт."));
            for (Paint paint : paints) {
                Rectangle rectangle = new Rectangle(width, width, paint);
                rectangle.setStroke(Color.BLACK);
                children.add(rectangle);
            }
        }
    }

    private void currentColorBoxInit() {
        ObservableList<Node> children = currentColorVBox.getChildren();
        children.clear();
        double width = currentColorVBox.getWidth()-50;
        Set<Paint> paints = model.getPaints();
        children.add(new Label("Количество: " + paints.size() + " шт."));
        for (Paint paint : paints) {
            Rectangle rectangle = new Rectangle(width, width, paint);
            rectangle.setStroke(Color.BLACK);
            rectangle.setOnMouseClicked(event -> {
                if (event.getClickCount() < 2) {
                    return;
                }
                // передать текущий цвет на форму
                Paint oldPaint = rectangle.getFill();
                ColorPickerController.setCurrentPaint(oldPaint);
                ControlUtils.openForm(FORM_COLOR_CHOICE, "Выбор цвета");
                
                // заьрать выбранный цвет
                Paint newPaint = ColorPickerController.getNewPaint();
                if (newPaint != null){
                    // изменить текущий блок
                    rectangle.setFill(newPaint);
                    // поменять модель
                    model.replacePaint(oldPaint, newPaint);
                    // перекрасть блоки на форме
                    new Filler().fillBlock(getMainGroup(), model.get(ModelType.MAIN, null));
                }
            });
            
            children.add(rectangle);
        }
    }
    
    private void fillBlock(Pane pane, Block block, int index) {
        if (block == null){
            return;
        }
        Label label = new Label();
        label.setId(index+"");
        label.setVisible(false);
        pane.getChildren().clear();
        pane.getChildren().add(label);
        pane.getChildren().add(block.getNode(pane.getPrefWidth(), pane.getPrefHeight()));
    }

    private Group createBlocks(SizeXY blockCount, Pane pane, Map<String, Pane> paneIdMap, double padding, double space, boolean isInit) {
        double width = pane.getPrefWidth();
        double widthBlock = (width - padding * 2) / blockCount.getX();
        double widthSize = widthBlock - 2 * space;

        double height = pane.getPrefHeight();
        double heightBlock = (height - padding * 2) / blockCount.getY();
        double heightSize = heightBlock - 2 * space;
        
        paneIdMap.clear();
        
        Group group = new Group();
        group.setId(NamingFactory.createUniqueName());
        for (int y = 0; y < blockCount.getY(); y++) {
            for (int x = 0; x < blockCount.getX(); x++) {
                Pane block = new Pane();
                block.setPrefWidth(widthSize);
                block.setPrefHeight(heightSize);
                block.setLayoutY(padding + y*(heightSize + 2 * space) + space);
                block.setLayoutX(padding + x*(widthSize + 2 * space) + space);
                block.setStyle("-fx-border-color: dimgrey");
                String id = y + "_" + x;
                block.setId(id);
                
                paneIdMap.put(id, block);
                group.getChildren().add(block);
            }
        }

        pane.getChildren().clear();
        pane.getChildren().add(group);
        return group;
    }

    private void paintSmallBlock(Pane source, Map<String, Pane> paneIdMap, SizeXY blockCount){
        int count = (int) Math.sqrt(paneIdMap.keySet().size());
        String[] ij = StringUtils.split(source.getId(), "_");
        int curr_i = MiscUtils.getInt(ij[0]);
        int curr_j = MiscUtils.getInt(ij[1]);

        blockCount.setX(0);
        blockCount.setY(0);
        
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                Pane block = paneIdMap.get(i + "_" + j);
                if (curr_i >= i && curr_j >= j){
                    block.setStyle("-fx-border-color: chocolate");
                    blockCount.setX(Math.max(blockCount.getX(), j+1));
                    blockCount.setY(Math.max(blockCount.getY(), i+1));
                } else {
                    block.setStyle("-fx-border-color: dimgrey");
                }
            }
        }
    }
    
    
    public void colorGenerate(ActionEvent actionEvent) {
        
    }
    
    private ColorSupplier getColorSupplier() {
        List<Paint> paintList = new ArrayList<>(20);
        if (currentColorAccordionPane.isExpanded()){
            paintList.addAll(model.getPaints());
        } else if (myColorAccordionPane.isExpanded()){
            for (Node child : myColorPane.getChildren()) {
                if (child instanceof Rectangle){
                    paintList.add(((Rectangle)child).getFill());
                }
            }
        }

        if (paintList.isEmpty()){
            return null;
        } else {
            return new RandomByColorSetColorSupplier(paintList);
        }
    }
}
