package Controllers;

import Models.Window;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import Models.Task;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TaskListCell extends ListCell<Task> implements Initializable {
    @FXML private Label priority;
    @FXML private Label title;
    @FXML private Label date;
    @FXML private AnchorPane root;
    private Task model;
    @FXML private MenuButton taskEventBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setGraphic(root);
        loadImg();
    }
    public AnchorPane getRoot() {
        return root;
    }
    public void loadImg(){
        Image image = new Image("/data/photos/dotsIcon.png");
        ImageView imageView = new ImageView(image);
        taskEventBtn.setGraphic(imageView);
    }
    public static TaskListCell newInstance() {
        FXMLLoader loader = new FXMLLoader(TaskListCell.class.getResource("/Views/TaskListCell.fxml"));
        try {
            loader.load();
            return loader.getController();
        } catch (IOException ex) {
            return null;
        }
    }
    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);
        getRoot().getChildrenUnmodifiable().forEach(c -> c.setVisible(!empty));
        if (!empty && item != null && !item.equals(this.model)) {
            if(item.getPriority()==1){
                this.priority.textProperty().set("LOW PRIORITY");
                this.priority.setStyle("-fx-text-fill: #155b51;-fx-border-color: #155b51");
            }
            else if(item.getPriority()==2){
                this.priority.textProperty().set("MEDIUM PRIORITY");
                this.priority.setStyle("-fx-text-fill: #b08a31;-fx-border-color: #b08a31");
            }
            else if(item.getPriority()==3){
                this.priority.textProperty().set("HIGH PRIORITY");
                this.priority.setStyle("-fx-text-fill: #8c3715;-fx-border-color: #8c3715");
            }
            this.title.textProperty().set(String.valueOf(item.getTitle()));
            this.date.textProperty().set(String.valueOf(item.getDate()));
        }
        this.model = item;
    }
    public void showTask(){
        Window newWindow = new Window("Details", "/Views/showDetailsWindow.fxml", "/styles/style.css", null, 358, 255);
        newWindow.initWindow();
        ShowDetailsWindowController scene4Controller = newWindow.getLoader().getController();
        scene4Controller.transferData(this.model.getId(), this.model.getTitle(), this.model.getDescription(), this.model.getPriority(), this.model.getDate(), this.model.getStatus());
        newWindow.showWindow();
    }
    public void editTask(){

    }
    public void deleteTask(){

    }
}
