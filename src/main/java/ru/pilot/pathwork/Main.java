package ru.pilot.pathwork;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class Main extends Application {

    private static Stage pStage;
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        URL sizeFrm = Thread.currentThread().getContextClassLoader().getResource("main.fxml");
        Parent root = FXMLLoader.load(sizeFrm);
        primaryStage.setTitle("Patchwork");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
        setPrimaryStage(primaryStage);
    }

    public static Stage getPrimaryStage() {
        return pStage;
    }

    private void setPrimaryStage(Stage pStage) {
        Main.pStage = pStage;
    }
    
    public void main2(String[] args) {
        launch(args);
    }

    @SneakyThrows
    public static <T> T getById(String name, Class<T> clazz){
        Node node = pStage.getScene().lookup("#" + name);
        if (node == null){
            throw new NoSuchFieldException(name + " " + clazz.getSimpleName());
        }
        
        if (!node.getClass().isInstance(clazz)){
            throw new NoSuchFieldException(name + " " + clazz.getSimpleName());
        }
        return (T) node;
    }
}
