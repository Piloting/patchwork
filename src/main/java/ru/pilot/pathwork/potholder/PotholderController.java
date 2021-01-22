package ru.pilot.pathwork.potholder;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import ru.pilot.pathwork.ControlUtils;
import ru.pilot.pathwork.MiscUtils;
import ru.pilot.pathwork.SizeXY;
import ru.pilot.pathwork.fill.Filler;
import ru.pilot.pathwork.fill.fillBlock.RandomFillStrategy;
import ru.pilot.pathwork.fill.sector.CrossSectorStrategy;
import ru.pilot.pathwork.fill.sector.NoSectorStrategy;
import ru.pilot.pathwork.fill.sector.SectorStrategy;
import ru.pilot.pathwork.patchwork.Block;

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

    private ModelPotholder model = new ModelPotholder();
    
    public Pane smallSidePane;
    public Pane bigSidePane;
    private final Map<String, Pane> frontPaneIdMap = new HashMap<>(36);
    private final Map<String, Pane> frontBlockIdMap = new HashMap<>(36);

    public Pane blockTypePane;
    
    public Button genButton;
    public Accordion sizeAccordion;
    public TitledPane sizeAccordionPane;
    public Pane simpleBlockPane;
    public RadioButton randomPatternRB;
    public RadioButton sectorPatternRB;
    public RadioButton crossPatternRB;
    
    @FXML
    private void initialize() {
        fillPatterns(Block.getExampleBlocks());
        createTemplate(new SizeXY(8,8), smallSidePane, frontPaneIdMap, bigSidePane, frontBlockIdMap);
        sizeAccordion.setExpandedPane(sizeAccordionPane);
        
        Group group = (Group) bigSidePane.getChildren().filtered(node -> node.getClass().equals(Group.class)).get(0);
        new ControlUtils().sizeAndMoveEvents(group);
        
        groupRadio();
    }

    private void groupRadio() {
        ToggleGroup group = new ToggleGroup();
        randomPatternRB.setToggleGroup(group);
        sectorPatternRB.setToggleGroup(group);
        crossPatternRB.setToggleGroup(group);
    }

    private void fillPatterns(List<Block> templateBlockList) {
        for (Block block : templateBlockList) {
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);
            checkBox.setGraphic(block.getNode(50, 50));
            blockTypePane.getChildren().add(checkBox);
        }
    }

    private void createTemplate(SizeXY blockCount, Pane pane, Map<String, Pane> paneIdMap, Pane bigSidePane, Map<String, Pane> bigPaneIdMap) {
        Group smallGroup = createBlocks(blockCount, pane, paneIdMap, 10, 2, false);
        for (Node child : smallGroup.getChildren()) {
            child.setOnMouseClicked(event -> {
                Pane source = (Pane) event.getSource();
                blockResize(blockCount, paneIdMap, bigSidePane, bigPaneIdMap, source);
            });
        }

        Pane pane33 = (Pane)smallGroup.getChildren().stream().filter(node -> node.getId().equals("3_3")).findFirst().orElse(null);
        blockResize(blockCount, paneIdMap, bigSidePane, bigPaneIdMap, pane33);

        generateSimples(blockCount);
    }

    private void generateSimples(SizeXY blockCount) {
        Filler filler = new Filler();
        for (int i = 0; i < 10; i++) {
            Pane simplePane = new Pane(); 
            simplePane.setPrefWidth(150);
            simplePane.setPrefHeight(150);
            Group blocks = createBlocks(blockCount, simplePane, new HashMap<>(), 0, 0, false);
            filler.fillBlock(blocks);
            simpleBlockPane.getChildren().add(simplePane);
        }
    }

    private void blockResize(SizeXY blockCount, Map<String, Pane> paneIdMap, Pane bigSidePane, Map<String, Pane> bigPaneIdMap, Pane source) {
        paintSmallBlock(source, paneIdMap, blockCount);
        Group bigBlocks = createBlocks(blockCount, bigSidePane, bigPaneIdMap, 0, 0, true);
        addEventChangeBlockType(bigBlocks);
    }

    private void addEventChangeBlockType(Group bigBlocks){
        List<Block> templateBlockList = Block.getExampleBlocks();
        for (Node child : bigBlocks.getChildren()) {
            child.setOnMouseClicked(event -> {
                if (event.getClickCount() < 2){
                    return;
                }
                Pane source = (Pane) event.getSource();
                
                Optional<String> label = source.getChildren().filtered(this::isLabel).stream().map(Node::getId).findFirst();
                String existBlockType = label.orElse(null);
                
                Block block;
                int index = 0;
                if (existBlockType != null){
                    int anInt = MiscUtils.getInt(existBlockType);
                    index = anInt+1 < templateBlockList.size() ? anInt+1 : 0;
                    Block stringBlockEntry = templateBlockList.get(index);
                    block = stringBlockEntry != null ? stringBlockEntry : templateBlockList.iterator().next();
                } else {
                    block = templateBlockList.iterator().next();
                }
                
                fillBlock(source, block, index);
            });
        }
    }

    private boolean isLabel(Node node) {
        return node.getClass().getSimpleName().equals(Label.class.getSimpleName());
    }

    public void getButtonClick(){
        Group group = (Group) bigSidePane.getChildren().filtered(node -> node.getClass().equals(Group.class)).get(0);

        SectorStrategy sectorStrategy = null;
        if (crossPatternRB.isSelected()){
            sectorStrategy = new CrossSectorStrategy(new RandomFillStrategy(), 10);
        } else if (sectorPatternRB.isSelected()){
            
        } else {
            sectorStrategy = new NoSectorStrategy(new RandomFillStrategy());
        }
        
        Filler filler = new Filler(sectorStrategy);
        
        filler.fillBlock(group);
    }
    
    private void fillBlock(Pane pane, Block block, int index) {
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
    
    public void debugWindow() throws IOException {
        // New window (Stage)
        URL sizeFrm = Thread.currentThread().getContextClassLoader().getResource("debugWindow.fxml");
        Parent root = FXMLLoader.load(sizeFrm);
        
        Stage newWindow = new Stage();
        newWindow.setTitle("Отладка");
        newWindow.setScene(new Scene(root, 650, 550));
        
        // Specifies the modality for new window.
        newWindow.initModality(Modality.NONE);
        
        // Specifies the owner Window (parent) for new window
        Stage primaryStage = PotholderApp.getPrimaryStage();
        newWindow.initOwner(primaryStage);
        
        // Set position of second window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);
        
        newWindow.show();
    }
}
