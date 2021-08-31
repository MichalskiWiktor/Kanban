package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopUpWindowController {
    @FXML private Label labelMessage;
    @FXML public Button closeButton;
    private Stage newTaskStage;
    public void transferMessage(String message, Stage newTaskStage){
        this.labelMessage.setText(message);
        if(newTaskStage!=null){
            this.newTaskStage = newTaskStage;
        }
    }
    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        if(this.newTaskStage!=null)this.newTaskStage.close();
    }
}

