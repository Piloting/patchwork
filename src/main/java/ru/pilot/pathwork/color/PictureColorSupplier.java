package ru.pilot.pathwork.color;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class PictureColorSupplier implements ColorSupplier {
    
    protected final Image image;
    protected int colorCount; // todo
    protected final double wZoom;
    protected final double hZoom;

    public PictureColorSupplier(Image image, int colorCount, int wPaneSize, int hPaneSize){
        this.image = image;
        this.colorCount = colorCount;
        this.wZoom = image.getWidth() / wPaneSize;
        this.hZoom = image.getHeight() / hPaneSize;
    }

    @Override
    public Color getColor(int h, int w, int hBlock, int wBlock) {
        return getAvgColorByPict(h, h+hBlock, w, w+wBlock);
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
        
        return new Color(
                redBucket/pixelCount,
                greenBucket/pixelCount,
                blueBucket/pixelCount,
                1
        );
    }
    
    protected Image getImage(){
        return image;
    }
    
    protected int getWZoomed(int w){
        return (int) Math.min(w * wZoom, getImage().getWidth()-1);
    }
    protected int getHZoomed(int h){
        return (int) Math.min(h * hZoom, getImage().getHeight()-1);
    }
}
