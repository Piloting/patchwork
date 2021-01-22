package ru.pilot.pathwork.kaleidoscope;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Kaleidoscope {
    
    private final Image originalImage;
    private final Image rotationImage;
    private final int multipleCount;
    
    public Kaleidoscope(Image originalImage, int multipleCount){
        this.originalImage = originalImage;
        this.multipleCount = multipleCount;

        double sectorRad = Math.toRadians((double) 360 / multipleCount);

        this.rotationImage = process(originalImage, sectorRad);
    }

    private Image process(Image image, double sectorRad) {
        int size = (int) Math.min(image.getHeight(), image.getWidth());

        WritableImage kaleidoscope = new WritableImage(size, size);
        PixelWriter pixelWriter = kaleidoscope.getPixelWriter();

        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                
                
                // точка в полярных координатах
                double r = getR(x, y);
                double t = getT(x, y);
                
                // поворот 
                t = t + sectorRad;
                
                // перевод обратно в декартовы координаты
                int newX = (int) (r * Math.cos(t));
                int newY = (int) (r * Math.sin(t));
                Color color = pixelReader.getColor(Math.abs(newX), Math.abs(newY));
                //System.out.println("x: " + x + " -> " + newX);
                //System.out.println("y: " + y + " -> " + newY);
                //System.out.println();

                //if (newX >= 0 && newY >= 0){
                    pixelWriter.setColor(x, y, color);
                //}
            }
        }
        
        return kaleidoscope;
    }
    
    private double getR(int x, int y){
        return Math.sqrt(x*x + y*y);
    }
    
    private double getT(int x, int y){
        return Math.atan2(y, x);
    }
    
    public Image getRotationImage(){
        return rotationImage;
    }
    
}
