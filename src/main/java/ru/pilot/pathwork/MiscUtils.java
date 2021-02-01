package ru.pilot.pathwork;

import javafx.scene.control.TextField;
import javafx.scene.input.DataFormat;
import org.apache.commons.lang3.math.NumberUtils;

public class MiscUtils {

    private static final DataFormat DRAGGED_PERSON = new DataFormat("application/example");

    public static int getInt(String text){
        if (NumberUtils.isCreatable(text)) {
            return NumberUtils.createInteger(text);
        }
        return 0;
    }
    
    public static int getInt(TextField textField, int defaultValue){
        String text = textField.getText();
        if (NumberUtils.isCreatable(text)){
            Integer integer = NumberUtils.createInteger(text);
            return integer > 0 ? integer : 0;
        } else {
            return defaultValue;
        }
    }
    
    /*public void dad(){
        Pane pane = new Pane();
        pane.setOnDragDetected( event -> {
            Dragboard db = pane.startDragAndDrop( TransferMode.COPY );
            ClipboardContent cc = new ClipboardContent();
            cc.put( DRAGGED_PERSON, pane );
            tableView.getItems().remove( pane );
            db.setContent( cc );
        });

        pane.setOnDragOver( event -> {
            Dragboard db = event.getDragboard();
            event.acceptTransferModes( TransferMode.COPY );
        });

        pane.setOnDragDropped( event -> {
            Dragboard db = event.getDragboard();

            Pane paneLocal = (Pane)event.getDragboard().getContent( DRAGGED_PERSON );

            if ( paneLocal != null ) {
                tableView.getItems().remove( paneLocal );
                int dropIndex = row.getIndex();
                tableView.getItems().add( dropIndex, paneLocal );
            }

            event.setDropCompleted( true );
            event.consume();
        });
    }*/
}
