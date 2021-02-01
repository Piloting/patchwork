package ru.pilot.pathwork.color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.pilot.pathwork.FileUtils;
import ru.pilot.pathwork.potholder.PotholderApp;

public class ColorPickerController {
    
    private static Paint currentPaint = null;
    private static Paint newPaint = null;

    public ColorPicker colorPicker;
    public Button getFile;
    public Rectangle oldPaintRect;
    public Rectangle newPaintRect;
    public Button cancel;
    public Button ok;
    
    @FXML
    private void initialize() {
        if (currentPaint != null){
            oldPaintRect.setFill(currentPaint);
        }
        
        FileChooser fileChooser = FileUtils.getPictFileChooser();
        getFile.setOnAction(event -> {
            Image image = FileUtils.getImage(fileChooser.showOpenDialog(PotholderApp.getPrimaryStage()));
            if (image == null){
                return;
            }
            double rectWidth = newPaintRect.getWidth();

            ImagePattern imagePattern = getImagePattern(image, rectWidth);

            newPaintRect.setFill(imagePattern);
        });

        colorPicker.setOnAction(event -> newPaintRect.setFill(colorPicker.getValue()));
    }

 
    public static ImagePattern getImagePattern(Image image, double rectWidth) {
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        
        ImagePattern imagePattern;
        if (imageWidth == imageHeight){
            imagePattern = new ImagePattern(image, 0, 0, rectWidth, rectWidth, false);
        } else if (imageHeight < imageWidth){
            double h = rectWidth / (imageWidth/imageHeight);
            imagePattern = new ImagePattern(image, 0, 0, rectWidth, h, false);
        } else {
            double w = rectWidth / (imageHeight/imageWidth);
            imagePattern = new ImagePattern(image, 0, 0, w, rectWidth, false);
        }
        return imagePattern;
    }

    public static void setCurrentPaint(Paint paint){
        currentPaint = paint;
        newPaint = null;
    }
    
    public static Paint getNewPaint(){
        return newPaint;
    }
    
    public void cancel(ActionEvent actionEvent) {
        newPaint = null;
        currentPaint = null;
        close(actionEvent);
    }
    public void success(ActionEvent actionEvent) {
        newPaint = newPaintRect.getFill();
        close(actionEvent);
    }

    public static void close(ActionEvent actionEvent) {
        Node  source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
