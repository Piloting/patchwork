package ru.pilot.pathwork;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

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
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            group.toFront();
        });
        group.setOnMouseDragged((MouseEvent event) -> {
            deltaX = event.getSceneX() - mouseX + posX;
            deltaY = event.getSceneY() - mouseY + posY;
            group.setLayoutX(deltaX);
            group.setLayoutY(deltaY);
        });
        group.setOnMouseReleased((MouseEvent event) -> {
            posX = group.getLayoutX();
            posY = group.getLayoutY();
        });
    }
    
}
