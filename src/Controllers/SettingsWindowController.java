package Controllers;

import Models.Database;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;


public class SettingsWindowController{
    @FXML private TextField dbName;
    @FXML private TextField dbPort;
    @FXML private TextField dbUser;
    @FXML private PasswordField dbPassword;
    @FXML private RadioButton onBtn;
    @FXML private RadioButton offBtn;
    public void initialize(){
        dbName.setText(Database.getName());
        dbPort.setText(Database.getPort());
        dbUser.setText(Database.getUser());
        dbPassword.setText(Database.getPassword());
    }

    public void applyChanges(){
        Database.setName(dbName.getText());
        Database.setPort(dbPort.getText());
        Database.setUser(dbUser.getText());
        Database.setPassword(dbPassword.getText());
        if(onBtn.isSelected()){
            Database.setStatus("true");
            ///it needs to return false when connecting error ecurade in database connection method
            try{
                Database.connectToDatabase();
                Database.downloadListOfTask();
            }catch(NullPointerException e){
                System.out.println("Database connection error");
            }
        }
        else if(offBtn.isSelected())Database.setStatus("false");
    }
}
