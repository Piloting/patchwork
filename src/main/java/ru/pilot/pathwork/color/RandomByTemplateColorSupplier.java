package ru.pilot.pathwork.color;


import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class RandomByTemplateColorSupplier extends PictureColorSupplier {
    
    private final Image greyImage;
    private final TreeMap<Double, Color> greyToColorBucket;
    private final ColorSupplier colorSupplier;
    
    public RandomByTemplateColorSupplier(Image originalImage, int colorCount, int wPaneSize, int hPaneSize, ColorSupplier colorSupplier){
        super(originalImage, colorCount, wPaneSize, hPaneSize);
        this.colorSupplier = colorSupplier;
        this.greyToColorBucket = getColorBucket(originalImage, colorCount);
        this.greyImage = toGray(originalImage);
    }

    protected Image getImage(){
        return image;
    }
    
    protected Color getAvgColorByPict(int hStart, int hEnd, int wStart, int wEnd) {
        double redBucket = 0;
        double greenBucket = 0;
        double blueBucket = 0;
        long pixelCount = 0;

        hEnd = Math.min((int)getImage().getHeight(), hEnd);
        wEnd = Math.min((int)getImage().getWidth(), wEnd);
        PixelReader pixelReader = getImage().getPixelReader();

        for (int h = hStart; h <= hEnd; h++) {
            for (int w = wStart; w <= wEnd; w++) {
                int wZoomed = getWZoomed(w);
                int hZoomed = getHZoomed(h);
                Color color = pixelReader.getColor(wZoomed, hZoomed);
                redBucket += color.getRed();
                greenBucket += color.getGreen();
                blueBucket += color.getBlue();
                pixelCount++;
            }
        }

        if (pixelCount == 0){
            return Color.rgb(0, 0, 0);
        }

        double avgRed = redBucket / pixelCount;
        double avgGreen = greenBucket / pixelCount;
        double avgBlue = blueBucket / pixelCount;
        double avgGray = getGray(avgRed, avgGreen, avgBlue);

        return greyToColorBucket.floorEntry(avgGray).getValue();
    }

    private Image toGray(Image image){
        PixelReader pixelReader = image.getPixelReader();
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        
        WritableImage grayImg = new WritableImage(width, height);
        PixelWriter pw = grayImg.getPixelWriter();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                Color color = pixelReader.getColor(w, h);
                double gray = getGray(color);
                Color value = greyToColorBucket.floorEntry(gray).getValue();
                pw.setColor(w, h, value);
            }
        }
        return grayImg;
    }

    private double getGray(double red, double green, double blue) {
        return red * 0.3 + green * 0.59 + blue * 0.11;
    }
    private double getGray(Color color) {
        return getGray(color.getRed(), color.getGreen(), color.getBlue());
    }

    private TreeMap<Double, Color> getColorBucket(Image img, int count){
        PixelReader pixelReader = img.getPixelReader();

        double min = 1;
        double max = 0;
        for (int h = 0; h < img.getHeight(); h++) {
            for (int w = 0; w < img.getWidth(); w++) {
                Color color = pixelReader.getColor(w, h);
                double gray = getGray(color);
                min = Math.min(gray, min);
                max = Math.max(gray, max);
            }
        }

        double step = (max - min) / count;
        TreeMap<Double, Color> colorBucketMap = new TreeMap<>();
        
        TreeSet<Color> colors = new TreeSet<>(Comparator.comparingDouble(this::getGray));
        for (int i = 0; i < count; i++) {
            colors.add(colorSupplier.getColor(0,0,0,0));
        }
        
        int i = 0;
        for (Color color : colors) {
            double startInterval = min + i * step;
            colorBucketMap.put(startInterval, color);
            i++;
        }
        
        return colorBucketMap;
    }
    
    private Color getUniqueColor(Collection<Color> existsColor){
        int i = 0;
        Color color;
        do {
            color = colorSupplier.getColor(0,0,0,0);
            i++;
        } while (i<10 && existsColor.contains(color));
        return color;        
    }
}
