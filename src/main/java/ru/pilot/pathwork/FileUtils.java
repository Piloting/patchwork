package ru.pilot.pathwork;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.apache.commons.collections4.CollectionUtils;

public class FileUtils {
    
    public static FileChooser getPictFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Картинки", "*.bmp", "*.gif", "*.jpg", "*.jpeg", "*.png"));
        return fileChooser;
    }
    
    public static void fillImage(File file, ImageView imageView){
        if (file != null) {
            imageView.setImage(getImage(file));
        }
    }
    
    public static Image getImage(File file){
        if (file != null) {
            String url = "file:///" + file.getAbsolutePath();
            url = url.replaceAll("\\\\", "/");
            return new Image(url);
        }
        return null;
    }
    
    public static List<Image> getImage(List<File> files){
        if (CollectionUtils.isEmpty(files)){
            return Collections.emptyList();
        }
        List<Image> images = new ArrayList<>(files.size());
        for (File file : files) {
            try {
                images.add(getImage(file));
            } catch (Exception e){
                // 
            }
        }
        return images;
    }
    
}
