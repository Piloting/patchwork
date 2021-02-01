package ru.pilot.pathwork.color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ColorSetUtils {

    private static final Map<String, ColorSet> colorSetMap = new HashMap<>();
    static {
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
    }
    
    public static Map<String, ColorSet> getColorSetMap(){
        return colorSetMap;
    }
    
    public static Map<String, WritableImage> createColorBoxMap(){
        Map<String, WritableImage> imageMap = new HashMap<>();
        HBox hBox = new HBox();
        for (Map.Entry<String, ColorSet> entry : colorSetMap.entrySet()) {
            for (Paint color : entry.getValue().getColors()) {
                Rectangle r = new Rectangle(45,30, color);
                hBox.getChildren().add(r);
            }
            WritableImage snapshot = hBox.snapshot(new SnapshotParameters(), null);
            hBox.getChildren().clear();
            imageMap.put(entry.getKey(), snapshot);
        }
        return imageMap;
    }

    public static void colorSetViewConfig(ListView colorSetView) {
        colorSetView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Map<String, WritableImage> colorBoxMap = ColorSetUtils.createColorBoxMap();
        ObservableList<String> items = FXCollections.observableArrayList(colorBoxMap.keySet());
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
                    imageView.setImage(colorBoxMap.get(name));
                    setText(name);
                    setGraphic(imageView);
                }
            }
        });
    }
}
