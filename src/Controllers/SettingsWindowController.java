package Controllers;

import Models.Database;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;

import java.awt.*;

public class SettingsWindowController{
    @FXML private TextField dbName;
    @FXML private TextField dbPort;
    @FXML private TextField dbUser;
    @FXML private PasswordField dbPassword;
    @FXML private RadioButton onBtn;
    @FXML private RadioButton offBtn;


    public void applyChanges(){
        Database.setName(dbName.getText());
        Database.setPort(dbPort.getText());
        Database.setUser(dbUser.getText());
        Database.setPassword(dbPassword.getText());
        if(onBtn.isSelected())Database.setStatus("true");
        else if(offBtn.isSelected())Database.setStatus("false");
    }
}
