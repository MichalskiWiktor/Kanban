package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopUpWindowController {
    @FXML private Label labelMessage;
    @FXML public Button closeButton;
    public void transferMessage(String message){
        this.labelMessage.setText(message);
    }
    public void closeWindow(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

