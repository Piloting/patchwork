package ru.pilot.simulate.graphic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.apache.commons.lang3.math.NumberUtils;

public class Controller {

    public static final Random rnd = new Random();
    
    public ColorPicker colorPic;
    public Button genBtn;
    
    public TextField allSizeX, allSizeY;
    public TextField pageSizeY, pageSizeX;
    public TextField blockSizeY, blockSizeX;
    
    public Pane canvas;
    public Rectangle place;
    public Pane patchwork;
    public RadioButton radioRnd;
    public RadioButton radioPic;
    public RadioButton radioCol;
    public ListView colorSetView;

    private Map<String, ColorSet> colorSetMap = new HashMap<>();

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        radioRnd.setToggleGroup(group);
        radioPic.setToggleGroup(group);
        radioCol.setToggleGroup(group);

        colorSetMap.put("1",  new ColorSet(Arrays.asList(Color.web("#3c1d25"), Color.web("#68212f"), Color.web("#a5395d"), Color.web("#e088ae"), Color.web("#cbe81e"))));
        colorSetMap.put("2",  new ColorSet(Arrays.asList(Color.web("#c3547f"), Color.web("#e087b5"), Color.web("#97a7c8"), Color.web("#b7adb6"), Color.web("#edeffc"))));
        colorSetMap.put("3",  new ColorSet(Arrays.asList(Color.web("#507160"), Color.web("#d37a9a"), Color.web("#ecb9e2"), Color.web("#7d7fbc"), Color.web("#7d8d9d"))));
        colorSetMap.put("4",  new ColorSet(Arrays.asList(Color.web("#2f1f3c"), Color.web("#523956"), Color.web("#745567"), Color.web("#b99595"), Color.web("#dac1ba"))));
        colorSetMap.put("5",  new ColorSet(Arrays.asList(Color.web("#111312"), Color.web("#2c475c"), Color.web("#6495b3"), Color.web("#f4f9fc"), Color.web("#997a66"))));
        colorSetMap.put("6",  new ColorSet(Arrays.asList(Color.web("#ffffff"), Color.web("#845f4d"), Color.web("#bf2f6a"), Color.web("#25559d"), Color.web("#d8dd2b"))));
        colorSetMap.put("7",  new ColorSet(Arrays.asList(Color.web("#514249"), Color.web("#7f5a54"), Color.web("#b2968a"), Color.web("#c3c3c3"), Color.web("#ffffff"))));
        colorSetMap.put("8",  new ColorSet(Arrays.asList(Color.web("#883c63"), Color.web("#cc5e7b"), Color.web("#b98ea1"), Color.web("#f4d9be"), Color.web("#868c22"))));
        colorSetMap.put("9",  new ColorSet(Arrays.asList(Color.web("#593366"), Color.web("#a66aa8"), Color.web("#e6c2dc"), Color.web("#d9dbe7"), Color.web("#acb01b"))));
        colorSetMap.put("10", new ColorSet(Arrays.asList(Color.web("#580201"), Color.web("#ab0068"), Color.web("#e00702"), Color.web("#ff6c02"), Color.web("#fec106"))));

        colorSetViewConfig();
    }

    private void colorSetViewConfig() {
        colorSetView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Map<String, WritableImage> imageMap = new HashMap<>(colorSetMap.size());
        HBox hBox = new HBox();
        for (Map.Entry<String, ColorSet> entry : colorSetMap.entrySet()) {
            for (Color color : entry.getValue().getColors()) {
                Rectangle r = new Rectangle(45,30, color);
                hBox.getChildren().add(r);
            }
            WritableImage snapshot = hBox.snapshot(new SnapshotParameters(), null);
            imageMap.put(entry.getKey(), snapshot);
            hBox.getChildren().clear();
        }

        ObservableList<String> items = FXCollections.observableArrayList(imageMap.keySet());
        colorSetView.setItems(items);
        colorSetView.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(imageMap.get(name));
                    setText(name);
                    setGraphic(imageView);
                }
            }
        });
    }

    public void createPatch(ActionEvent event) {
        Integer width = 100;
        if (NumberUtils.isCreatable(allSizeX.getCharacters().toString())){
            width = NumberUtils.createInteger(allSizeX.getCharacters().toString());
        }
        Integer height = 100;
        if (NumberUtils.isCreatable(allSizeY.getCharacters().toString())){
            height = NumberUtils.createInteger(allSizeY.getCharacters().toString());
        }
        
        place.setWidth(width);
        place.setHeight(height);
        place.setFill(colorPic.getValue());

        if (radioRnd.isSelected()){
            generate();
        } else if(radioPic.isSelected()){
            generateByPict();
        } else if (radioCol.isSelected()){
            generateByColorSet();
        }
    }


    private void generateByColorSet() {
        patchwork.getChildren().clear();
        double width = patchwork.getPrefWidth();
        double height = patchwork.getPrefHeight();
        String block = blockSizeX.getCharacters().toString();
        int blockSize = NumberUtils.isCreatable(block) ? NumberUtils.createInteger(block) : 20;

        for (int i = 0; i < height; i=i+blockSize) {
            for (int j = 0; j < width; j=j+blockSize) {
                Rectangle rectangle = new Rectangle(blockSize, blockSize, getRgbFromColorSet());
                rectangle.setLayoutY(i);
                rectangle.setLayoutX(j);
                patchwork.getChildren().add(rectangle);
            }
        }
    }

    private Paint getRgbFromColorSet() {
        ObservableList<String> selectedItems = colorSetView.getSelectionModel().getSelectedItems();
        List<Color> colors = new ArrayList<>();
        for (String selectedItem : selectedItems) {
            colors.addAll(colorSetMap.get(selectedItem).getColors());
        }
        
        return colors.get(rnd.nextInt(colors.size()));
    }


    private void generate() {
        patchwork.getChildren().clear();
        double width = patchwork.getPrefWidth();
        double height = patchwork.getPrefHeight();
        String block = blockSizeX.getCharacters().toString();
        int blockSize = NumberUtils.isCreatable(block) ? NumberUtils.createInteger(block) : 20;
        
        for (int i = 0; i < height; i=i+blockSize) {
            for (int j = 0; j < width; j=j+blockSize) {
                Rectangle rectangle = new Rectangle(blockSize, blockSize, getRgb());
                rectangle.setLayoutY(i);
                rectangle.setLayoutX(j);
                patchwork.getChildren().add(rectangle);
            }
        }
    }


    private Color getRgb() {
        return Color.rgb(rndCol(), rndCol(), rndCol());
    }

    private int rndCol(){
        return rnd.nextInt(255);
    }


    private void generateByPict() {
        patchwork.getChildren().clear();
        double width = patchwork.getPrefWidth();
        double height = patchwork.getPrefHeight();
        String block = blockSizeX.getCharacters().toString();
        int blockSize = NumberUtils.isCreatable(block) ? NumberUtils.createInteger(block) : 20;
                
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
                    patchwork.getChildren().add(rectangle);
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

    public void allSizeAction(ActionEvent actionEvent) {
        if (NumberUtils.isCreatable(allSizeX.getCharacters().toString())){
            place.setWidth(NumberUtils.createInteger(allSizeX.getCharacters().toString()));
        }
        if (NumberUtils.isCreatable(allSizeY.getCharacters().toString())){
            place.setHeight(NumberUtils.createInteger(allSizeY.getCharacters().toString()));
        }
    }
    

    public void pageSizeAction(ActionEvent actionEvent) {
        if (NumberUtils.isCreatable(pageSizeX.getCharacters().toString())){
            patchwork.setPrefWidth(NumberUtils.createInteger(pageSizeX.getCharacters().toString()));
        }
        if (NumberUtils.isCreatable(pageSizeY.getCharacters().toString())){
            patchwork.setPrefHeight(NumberUtils.createInteger(pageSizeY.getCharacters().toString()));
        }

        generateByPict();
    }
}
