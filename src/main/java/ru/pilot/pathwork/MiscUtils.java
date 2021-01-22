package ru.pilot.pathwork;

import javafx.scene.control.TextField;
import org.apache.commons.lang3.math.NumberUtils;

public class MiscUtils {

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
}
