package Models;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Window {
    private final String title;
    private final String cssFile;
    private final String fxmlFile;
    private final String iconFile;
    private final int width;
    private final int height;
    private FXMLLoader loader;
    private Stage stage;
    public Window(String title, String fxmlFile, String cssFile, String iconFile, int width, int height){
        this.title = title;
        this.cssFile = cssFile;
        this.fxmlFile = fxmlFile;
        this.iconFile = iconFile;
        this.width = width;
        this.height = height;
    }
    public void initWindow(){
        try{
            /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.fxmlFile));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            this.loader = fxmlLoader;
            stage.setTitle(this.title);
            stage.setResizable(false);
            if(this.iconFile!=null)stage.getIcons().add(new Image(this.iconFile));
            stage.setScene(new Scene(root, this.width, this.height));
            stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource(this.cssFile)).toExternalForm());
            this.stage = stage;*/

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.fxmlFile));
            Parent root = fxmlLoader.load();
            this.loader = fxmlLoader;
            root.setId("rootnode");
            Stage primaryStage = new Stage();
            primaryStage.initStyle (StageStyle.TRANSPARENT);

            Scene scene = new Scene (root, this.width, this.height);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource(this.cssFile)).toExternalForm());
            this.stage = primaryStage;
        } catch(Exception e){
            System.out.print("New window can not be load!!!");
        }
    }
    public FXMLLoader getLoader(){
        return this.loader;
    }
    public void showAndWaitWindow(){
        this.stage.showAndWait();
    }
    public void showWindow(){
        this.stage.show();
    }
}