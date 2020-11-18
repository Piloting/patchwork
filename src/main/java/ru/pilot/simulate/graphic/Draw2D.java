package ru.pilot.simulate.graphic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Draw2D extends Application {

    public static final Random rnd = new Random();
    int pWidth = 500;
    int pHeight = 700;
    int blockSize = 50;
    
    @Override
    public void start(Stage stage) {
        Group p = new Group();
        Scene scene = new Scene(p);
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(800);

        Group place = new Group();
        Group pict = new Group();
        Group control = new Group();
        p.getChildren().add(pict);
        p.getChildren().add(control);
        p.getChildren().add(place);
        
        pict(pict);
        control(control, place);
        
        generateByPict(pWidth, pHeight, place, blockSize);

        scene.setOnKeyPressed(e ->{
            if(e.getCode() == KeyCode.SPACE){
                generate(pWidth, pHeight, place, blockSize);
            } else if(e.getCode() == KeyCode.UP){
                blockSize = blockSize + 5;
                generateByPict(pWidth, pHeight, place, blockSize);
            } else if(e.getCode() == KeyCode.DOWN){
                blockSize = blockSize - 5;
                generateByPict(pWidth, pHeight, place, blockSize);
            } else if(e.getCode() == KeyCode.ENTER){
                generateByPict(pWidth, pHeight, place, blockSize);
            } else if(e.getCode() == KeyCode.CONTROL){
                place.setVisible(!place.isVisible());
            }
            System.out.println("blockSize: " + blockSize);
        });

        stage.show();
    }

    private void control(Group control, Group place) {
        Button btn = new Button("Кнопка");
        control.getChildren().add(btn);
        btn.setLayoutX(550);
        btn.setLayoutY(20);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                blockSize = blockSize - 5;
                generateByPict(pWidth, pHeight, place, blockSize);
            }
        });

        Slider slider = new Slider();
        slider.setMin(10);
        slider.setMax(200);
        slider.setValue(50);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(20);
        
        // Adding Listener to value property.
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                blockSize = newValue.intValue();
                generateByPict(pWidth, pHeight, place, blockSize);
            }
        });
        
        VBox box = new VBox();
        box.setMinWidth(300);
        box.setMinHeight(20);
        box.setPadding(new Insets(20));
        box.setSpacing(10);
        box.setLayoutX(550);
        box.setLayoutY(50);
        box.getChildren().add(slider);
        control.getChildren().add(box);
        
    }

    private void pict(Group control) {
        Image image = new Image("screen.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(pHeight);
        imageView.setFitWidth(pWidth);
        control.getChildren().add(imageView);
    }

    private void generateByPict(int width, int height, Group p, int blockSize) {
        p.setVisible(true);
        p.getChildren().clear();
        
        try {
            URL resource = Thread.currentThread().getContextClassLoader().getResource("screen.jpg");
            BufferedImage image = ImageIO.read(resource);

            int iWidth = image.getWidth();
            int iHeight = image.getHeight();

            double wCor = (double) iWidth / width;
            double hCor = (double) iHeight / height;

            int hPrev = 0;
            int wPrev = 0;
            for (int h = 0; h < height; h=h+blockSize) {
                for (int w = 0; w < width; w=w+blockSize) {
                    Paint avgColorByPict = getAvgColorByPict(image,
                            new Double(hPrev * hCor).intValue(), new Double(wPrev * wCor).intValue(),
                            new Double((h+blockSize) * hCor).intValue(), new Double((w+blockSize) * wCor).intValue()
                    );
                    
                    Rectangle rectangle = new Rectangle(
                            blockSize, 
                            blockSize,
                            avgColorByPict);
                    rectangle.setLayoutY(h);
                    rectangle.setLayoutX(w);
                    p.getChildren().add(rectangle);
                    hPrev = h;
                    wPrev = w;
                }
                hPrev = h+blockSize;
                wPrev = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Paint getAvgColorByPict(BufferedImage image, int hPrev, int wPrev, int hNew, int wNew) {
        long redBucket = 0;
        long greenBucket = 0;
        long blueBucket = 0;
        long pixelCount = 0;

        hNew = Math.min(image.getHeight(), hNew);
        wNew = Math.min(image.getWidth(), wNew);
        
        for (int h = hPrev; h < hNew; h++) {
            for (int w = wPrev; w < wNew; w++) {
                //sumColor = sumColor + image.getRGB(w, h);
                //countColor++;
                int color = image.getRGB(w, h);
                redBucket += (color >> 16) & 0xFF; // Color.red
                greenBucket += (color >> 8) & 0xFF; // Color.greed
                blueBucket += (color & 0xFF); // Color.blue

                pixelCount++;
            }
        }
        
        if (pixelCount == 0){
            return Color.rgb(0, 0, 0);
        }
        
        return Color.rgb(
                new Long(redBucket/pixelCount).intValue(), 
                new Long(greenBucket/pixelCount).intValue(), 
                new Long(blueBucket/pixelCount).intValue()
        );
    }

    private void generate(int width, int height, Group p, int blockSize) {
        for (int i = 0; i < height; i=i+blockSize) {
            for (int j = 0; j < width; j=j+blockSize) {
                Rectangle rectangle = new Rectangle(blockSize, blockSize, getRgb());
                rectangle.setLayoutY(i);
                rectangle.setLayoutX(j);
                p.getChildren().add(rectangle);
            }
        }
    }

    private Color getRgb() {
        return Color.rgb(rndCol(), rndCol(), rndCol());
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    private int rndCol(){
        return rnd.nextInt(255);
    }
    
}
