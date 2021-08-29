package Controllers;

import Models.Window;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopUpWindowController {
    @FXML private Label labelMessage;
    @FXML public Button closeButton;
    private Stage mainStage;
    private Stage newTaskStage;
    public void transferMessage(String message, Stage mainStage, Stage newTaskStage){
        this.labelMessage.setText(message);
        if(mainStage!=null && newTaskStage!=null){
            this.mainStage = mainStage;
            this.newTaskStage = newTaskStage;
        }
    }
    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        if(this.mainStage!=null)this.mainStage.close();
        if(this.newTaskStage!=null)this.newTaskStage.close();
        if(this.mainStage!=null)this.resetWindow();
    }
    public void resetWindow(){
        Window newWindow = new Window("Kanban", "/Views/MainWindow.fxml", "/styles/style.css", 1036, 552);
        newWindow.showWindow();
    }

}

