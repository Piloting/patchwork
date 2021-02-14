package ru.pilot.pathwork;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import ru.pilot.pathwork.block.Block;
import ru.pilot.pathwork.potholder.ModelPotholder;
import ru.pilot.pathwork.potholder.PotholderApp;

public class ControlUtils {

    private double mouseX, mouseY, deltaX, deltaY, posX, posY;
    
    public void sizeAndMoveEvents(Group group){
        
        group.setOnScroll((ScrollEvent event) -> {
            event.consume();
            if (event.getDeltaY() == 0) {
                return;
            }
            double scaleFactor = (event.getDeltaY() > 0) ? 1.1 : 1 / 1.1;
            group.setScaleX(group.getScaleX() * scaleFactor);
            group.setScaleY(group.getScaleY() * scaleFactor);
        });

        group.setOnMousePressed((MouseEvent event) -> {
            if (MouseButton.MIDDLE != event.getButton()){
                return;
            }
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            group.toFront();
        });
        group.setOnMouseDragged((MouseEvent event) -> {
            if (MouseButton.MIDDLE != event.getButton()){
                return;
            }
            deltaX = event.getSceneX() - mouseX + posX;
            deltaY = event.getSceneY() - mouseY + posY;
            group.setLayoutX(deltaX);
            group.setLayoutY(deltaY);
        });
        group.setOnMouseReleased((MouseEvent event) -> {
            if (MouseButton.MIDDLE != event.getButton()){
                return;
            }
            posX = group.getLayoutX();
            posY = group.getLayoutY();
        });
    }

    @SneakyThrows
    public static void openForm(String fileName, String title) {
        // New window (Stage)
        URL sizeFrm = Thread.currentThread().getContextClassLoader().getResource(fileName);
        Parent root = FXMLLoader.load(sizeFrm);

        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        newWindow.setScene(new Scene(root));

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        Stage primaryStage = PotholderApp.getPrimaryStage();
        newWindow.initOwner(primaryStage);

        // Set position of second window, related to primary window.
        newWindow.setX(primaryStage.getX() + 200);
        newWindow.setY(primaryStage.getY() + 100);

        newWindow.showAndWait();
    }
    
    public static Node addColorReplaceEvent(Block block, Node node){
        node.setOnMouseClicked(event -> {
            if (MouseButton.SECONDARY != event.getButton()){
                return;
            }
            Shape source = (Shape) event.getSource();
            Paint currentPaint = source.getFill();
            Paint nextPaint = ModelPotholder.INSTANCE.getNextPaint(currentPaint);
            ModelPotholder.INSTANCE.replacePaintBlock(block, currentPaint, nextPaint);
        });
        return node;
    }


}
