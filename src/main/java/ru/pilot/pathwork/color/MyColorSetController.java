package ru.pilot.pathwork.color;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import ru.pilot.pathwork.FileUtils;
import ru.pilot.pathwork.potholder.PotholderApp;

public class MyColorSetController {

    public Pane mainPane;
    
    private static final List<Paint> paintList = new ArrayList<>(20);

    @FXML
    private void initialize() {
        for (Paint paint : paintList) {
            Rectangle rectangle = createRect(paint);
            mainPane.getChildren().add(rectangle);
        }
    }
    
    public void cancel(ActionEvent actionEvent) {
        paintList.clear();
        ColorPickerController.close(actionEvent);
    }

    public void success(ActionEvent actionEvent) {
        paintList.clear();
        for (Node child : mainPane.getChildren()) {
            if (child instanceof Rectangle){
                paintList.add(((Rectangle)child).getFill());
            }
        }
        ColorPickerController.close(actionEvent);
    }

    public static void setPaints(List<Paint> paints){
        paintList.clear();
        paintList.addAll(paints);
    }
    
    public static List<Paint> getPaints(){
        return paintList;
    }

    public void openFileDialog(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() < 2) {
            return;
        }
        FileChooser fileChooser = FileUtils.getPictFileChooser();
        
        Image image = FileUtils.getImage(fileChooser.showOpenDialog(PotholderApp.getPrimaryStage()));
        if (image == null){
            return;
        }

        Rectangle rectangle = (Rectangle) mouseEvent.getSource();
        rectangle.setFill(ColorPickerController.getImagePattern(image, rectangle.getWidth()));
    }


    public void addBtnClick(ActionEvent actionEvent) {
        FileChooser fileChooser = FileUtils.getPictFileChooser();
        List<File> files = fileChooser.showOpenMultipleDialog(PotholderApp.getPrimaryStage());
        List<Image> images = FileUtils.getImage(files);
        if (images.isEmpty()){
            return;
        }
        
        for (Image image : images) {
            Rectangle rectangle = createRect(ColorPickerController.getImagePattern(image, 200));
            mainPane.getChildren().add(rectangle);
        }
    }
    
    private Rectangle createRect(Paint paint) {
        Rectangle rectangle = new Rectangle(200, 200, paint);
        rectangle.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                openFileDialog(event);
            }
        });

        rectangle.setOnScroll((ScrollEvent event) -> {
            event.consume();
            if (event.getDeltaY() == 0) {
                return;
            }
            double scaleFactor = (event.getDeltaY() > 0) ? 1.1 : 1 / 1.1;
            Rectangle rect = (Rectangle) event.getSource();
            ImagePattern fill = (ImagePattern) rect.getFill();
            ImagePattern imagePattern = ColorPickerController.getImagePattern(fill.getImage(), fill.getWidth() * scaleFactor);
            rect.setFill(imagePattern);
        });
        return rectangle;
    }
}
