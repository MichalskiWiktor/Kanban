package Controllers;

import Models.Window;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

public class AddNewTaskWindowController extends MainWindowController{
    @FXML private TextField newTitle;
    @FXML private ChoiceBox <String> priorityChoiceBox;
    @FXML private DatePicker newDeadline;
    @FXML private TextArea newDescription;
    @FXML private void initialize(){
        this.loadItemsToChoiceBox();
        this.setDefaultDeadline();
        this.priorityChoiceBox.setValue("Low");
    }
    private void loadItemsToChoiceBox(){
        this.priorityChoiceBox.getItems().add("Low");
        this.priorityChoiceBox.getItems().add("Medium");
        this.priorityChoiceBox.getItems().add("High");
    }
    private void setDefaultDeadline(){
        LocalDate myObj = LocalDate.now();
        this.newDeadline.setValue(myObj);
    }
    private boolean checkIfAllInsertedDataIsCorrectAndFixIt(){
        if(this.newTitle.getText().length()>50){
            this.createPopUpWindow("Title is too big", false);
            return false;
        }
        else if(this.newDescription.getText().length()>255){
            this.createPopUpWindow("Description is too big", false);
            return false;
        }
        if(this.newTitle.getText().length()==0)this.newTitle.setText("There is a lack of title");
        if(this.newDescription.getText().length()==0)this.newDescription.setText("There is a lack of description");
        return true;
    }
    public void sendNewTaskToDatabase(){
        if(this.checkIfAllInsertedDataIsCorrectAndFixIt()){
            try{
                Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kanban", "root", "");
                Statement myStat = myConn.createStatement();
                int priorityNumber = switch (this.priorityChoiceBox.getValue()) {
                    case "Low" -> 1;
                    case "Medium" -> 2;
                    case "High" -> 3;
                    default -> 0;
                };
                myStat.execute("INSERT INTO orders(title, description, priority, date, status) VALUES('" + this.newTitle.getText() + "', '" + this.newDescription.getText() + "', " +priorityNumber + ", '"+this.newDeadline.getValue()+"', "+ 1 +");");
                this.createPopUpWindow("Order has been added" , true);
            }
            catch(Exception exc){
                exc.printStackTrace();
            }
        }
    }
    private void createPopUpWindow(String message, boolean closeWindow){
        Window newWindow = new Window("PopUp Window", "/Views/PopUpWindow.fxml", "/styles/style.css", 235, 92);
        newWindow.showWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        if(closeWindow)scene4Controller.transferMessage(message, (Stage) this.newTitle.getScene().getWindow());
        else scene4Controller.transferMessage(message, null);
    }
}
