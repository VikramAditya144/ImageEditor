package application.imageeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ImageEditor extends Application {
    static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(ImageEditor.class.getResource("image-editor-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Image Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void closeWindow() {
        mainStage.close();
    }

    public static void main(String[] args) {
        launch();
    }
}