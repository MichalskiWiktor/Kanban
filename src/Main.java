import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try{
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/MainWindow.fxml")));
            root.setId("rootnode");
            primaryStage.initStyle (StageStyle.TRANSPARENT);

            Scene scene = new Scene (root, 1232, 740);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/style.css")).toExternalForm());
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
