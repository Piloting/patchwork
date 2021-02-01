package ru.pilot.pathwork;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import ru.pilot.pathwork.color.ColorSetUtils;
import ru.pilot.pathwork.color.ColorSupplier;
import ru.pilot.pathwork.color.PictureColorSupplier;
import ru.pilot.pathwork.color.RandomByColorSetColorSupplier;
import ru.pilot.pathwork.color.RandomByPictureColorSupplier;
import ru.pilot.pathwork.color.RandomColorSupplier;
import ru.pilot.pathwork.kaleidoscope.Kaleidoscope;

public class MainController {

    public ListView colorSetListView;
    public Accordion colorAccordion;
    public TitledPane randomTitledPane;

    public Button byPictureButton;
    public ImageView byPictureImageView;
    public ImageView rotateImageView;
    
    public Button templatePictureButton;
    public ImageView templatePictureImageView;
    
    public Pane patchworkPane;
    private Group group = new Group();

    public Slider blockSizeSlider;
    public Label  hBlockSizeLabel;
    public Label  wBlockSizeLabel;

    public Slider wSlider;
    public Label  wLabel;

    public Slider hSlider;
    public Label  hLabel;
    
    public TextField colorCountTextField;
  
    public Button colorCountUpButton;
    public Button colorCountDownButton;
    
    @FXML
    private void initialize() {
        ColorSetUtils.colorSetViewConfig(colorSetListView);
        openFileConfig();
        configSliders();

        colorAccordion.setExpandedPane(randomTitledPane);

        group.setAutoSizeChildren(true);
        patchworkPane.getChildren().add(group);
        
        drawPatchwork();
    }

    public void drawPatchwork() {
        wLabel.setText((int)wSlider.getValue()+"");
        patchworkPane.setPrefWidth((int)wSlider.getValue());
        
        hLabel.setText((int)hSlider.getValue()+"");
        patchworkPane.setPrefHeight((int)hSlider.getValue());

        ColorSupplier colorSupplier = getColorSupplier();
        Patchwork patchwork = new Patchwork(
                (int)wSlider.getValue(), (int)hSlider.getValue(), 
                (int)blockSizeSlider.getValue(), (int)blockSizeSlider.getValue(),
                colorSupplier
        );
        hBlockSizeLabel.setText(patchwork.getHBlockSize()+"");
        wBlockSizeLabel.setText(patchwork.getWBlockSize()+"");

        patchwork.fillBlockList(group);
    }

    private ColorSupplier getColorSupplier() {
        TitledPane titledPane = colorAccordion.getExpandedPane();

        ColorSupplier colorSupplier;
        if (titledPane == null || titledPane.getId().equals("randomTitledPane")){
            colorSupplier = new RandomColorSupplier(MiscUtils.getInt(colorCountTextField, 5));
        } else if (titledPane.getId().equals("byPictureTitledPane")){
            colorSupplier = new PictureColorSupplier(
                    byPictureImageView.getImage(),
                    MiscUtils.getInt(colorCountTextField, 0),
                    (int)wSlider.getValue(),
                    (int)hSlider.getValue()
            );
        } else if (titledPane.getId().equals("inColorBalanceTitledPane")){
            colorSupplier = new RandomByColorSetColorSupplier(getSelectedInColorBalance());
        } else if (titledPane.getId().equals("ownColorTitledPane")){
            colorSupplier = new RandomColorSupplier(MiscUtils.getInt(colorCountTextField, 5));
        } else {
            colorSupplier = new RandomColorSupplier(MiscUtils.getInt(colorCountTextField, 5));
        }

        if (templatePictureImageView.getImage() != null){
            return new RandomByPictureColorSupplier(
                    templatePictureImageView.getImage(),
                    MiscUtils.getInt(colorCountTextField, 5),
                    (int)wSlider.getValue(),
                    (int)hSlider.getValue(),
                    colorSupplier
            );
        }


        return colorSupplier;
    }


    private List<Paint> getSelectedInColorBalance(){
        ObservableList<String> selectedItems = colorSetListView.getSelectionModel().getSelectedItems();
        List<Paint> colors = new ArrayList<>();
        for (String selectedItem : selectedItems) {
            colors.addAll(ColorSetUtils.getColorSetMap().get(selectedItem).getColors());
        }
        return colors;
    }
    
    public double mouseX, mouseY, deltaX, deltaY, posX, posY;
    
    private void configSliders() {
        blockSizeSlider.valueProperty().addListener((changed, oldValue, newValue) -> drawPatchwork());
        wSlider.valueProperty().addListener((changed, oldValue, newValue) -> drawPatchwork());
        hSlider.valueProperty().addListener((changed, oldValue, newValue) -> drawPatchwork());
        colorCountUpButton.setOnAction(action -> {
            int value = MiscUtils.getInt(colorCountTextField, 5) + 1;
            colorCountTextField.setText(Math.max(value, 1) + "");
            drawPatchwork();
        });
        colorCountDownButton.setOnAction(action -> {
            int value = MiscUtils.getInt(colorCountTextField, 5) - 1;
            colorCountTextField.setText(Math.max(value, 1) + "");
            drawPatchwork();
        });
        
        new ControlUtils().sizeAndMoveEvents(group);

        group.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){
                drawPatchwork();
            }
        });
    }


    private void openFileConfig() {
        FileChooser fileChooser = FileUtils.getPictFileChooser();
        byPictureButton.setOnAction(event -> {
            FileUtils.fillImage(fileChooser.showOpenDialog(Main.getPrimaryStage()), byPictureImageView);
            drawPatchwork(); 
        });
        templatePictureButton.setOnAction(event -> {
            FileUtils.fillImage(fileChooser.showOpenDialog(Main.getPrimaryStage()), templatePictureImageView);
            drawPatchwork();
            Kaleidoscope kaleidoscope = new Kaleidoscope(templatePictureImageView.getImage(), 4);
            rotateImageView.setImage(kaleidoscope.getRotationImage());
        });
    }

    
    public void clearTemplate(){
        templatePictureImageView.setImage(null);
    }
    
    
    
}
