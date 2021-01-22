package ru.pilot.pathwork;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class FileUtils {
    
    public static FileChooser getPictFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Картинки", "*.bmp", "*.gif", "*.jpg", "*.jpeg", "*.png"));
        return fileChooser;
    }
    
    public static void fillImage(File file, ImageView imageView){
        if (file != null) {
            String url = "file:///" + file.getAbsolutePath();
            url = url.replaceAll("\\\\", "/");
            Image img = new Image(url);
            imageView.setImage(img);
        }
    }
    
}
