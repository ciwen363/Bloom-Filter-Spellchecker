package com.ciwen.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/*JavaFX 应用程序主类*/
public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/main.fxml");
            if (fxmlUrl == null) {
                throw new IOException("无法找到 /fxml/main.fxml");
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root, 800, 600);

            // 添加CSS样式
            scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

            primaryStage.setTitle("C/C++ Keyword Checker");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}