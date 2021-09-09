package Models;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class Window {
    private final String title;
    private final String cssFile;
    private final String fxmlFile;
    private final int width;
    private final int height;
    private FXMLLoader loader;
    private Stage stage;
    public Window(String title, String fxmlFile, String cssFile, int width, int height){
        this.title = title;
        this.cssFile = cssFile;
        this.fxmlFile = fxmlFile;
        this.width = width;
        this.height = height;
    }
    public void initWindow(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(this.fxmlFile));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            this.loader = fxmlLoader;
            stage.setTitle(this.title);
            stage.setResizable(false);
            stage.setScene(new Scene(root, this.width, this.height));
            stage.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource(this.cssFile)).toExternalForm());
            this.stage = stage;
        } catch(Exception e){
            System.out.print("New window can not be load!!!");
        }
    }
    public FXMLLoader getLoader(){
        return this.loader;
    }
    public void showWindow(){
        this.stage.showAndWait();
    }
}