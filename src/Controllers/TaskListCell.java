package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import Models.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskListCell extends ListCell<Task> implements Initializable {
    @FXML private Label id;
    @FXML private Label title;
    @FXML private Label date;
    @FXML private GridPane root;
    private Task model;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setGraphic(root);
    }
    public GridPane getRoot() {
        return root;
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
            this.id.textProperty().set(String.valueOf(item.getId()));
            this.title.textProperty().set(String.valueOf(item.getTitle()));
            this.date.textProperty().set(String.valueOf(item.getDate()));
        }
        this.model = item;
    }
}