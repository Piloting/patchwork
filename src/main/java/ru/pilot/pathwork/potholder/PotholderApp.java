package ru.pilot.pathwork.potholder;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class PotholderApp extends Application {

    private static Stage pStage;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL sizeFrm = Thread.currentThread().getContextClassLoader().getResource("potholder1.fxml");
        Parent root = FXMLLoader.load(sizeFrm);
        primaryStage.setTitle("Potholder");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
        setPrimaryStage(primaryStage);
    }


    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        PotholderApp.pStage = pStage;
    }
    public static void main(String[] args) {
        launch(args);
    }
    
    @SneakyThrows
    public static <T> T getById(String name, Class<T> clazz){
        Stage primaryStage = getPrimaryStage();
        Node node = primaryStage.getScene().lookup("#" + name);
        if (node == null){
            throw new NoSuchFieldException(name + " " + clazz.getSimpleName());
        }

        if (!node.getClass().isInstance(clazz)){
            throw new NoSuchFieldException(name + " " + clazz.getSimpleName());
        }
        return (T) node;
    }
}
