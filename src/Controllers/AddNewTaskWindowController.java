package Controllers;

import Models.Database;
import Models.Window;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDate;

public class AddNewTaskWindowController extends MainWindowController{
    @FXML private TextField newTitle;
    @FXML private ChoiceBox <String> priorityChoiceBox;
    @FXML private DatePicker newDeadline;
    @FXML private TextArea newDescription;
    private final Database database = new Database ();

    @FXML private void initialize(){
        this.loadItemsToChoiceBox();
        this.setDefaultDeadline();
        this.priorityChoiceBox.setValue("Low");
    }
    /*Sets possible choices*/
    private void loadItemsToChoiceBox(){
        this.priorityChoiceBox.getItems().add("Low");
        this.priorityChoiceBox.getItems().add("Medium");
        this.priorityChoiceBox.getItems().add("High");
    }
    /*Sets today's date as a deadline*/
    private void setDefaultDeadline(){
        LocalDate myObj = LocalDate.now();
        this.newDeadline.setValue(myObj);
    }
    /*Checks if inserted title and description are not too long
    and sets default tile and description if there is none*/
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
    /*Inserts data to the database*/
    public void addNewRecordToDatabase(){
        int priorityNumber = switch (this.priorityChoiceBox.getValue()) {
            case "Low" -> 1;
            case "Medium" -> 2;
            case "High" -> 3;
            default -> 0;
        };
        if(this.checkIfAllInsertedDataIsCorrectAndFixIt ()){
            this.database.runQuery("INSERT INTO tasks(title, description, priority, date, status) VALUES('" + this.newTitle.getText() + "', '" + this.newDescription.getText() + "', " +priorityNumber + ", '"+this.newDeadline.getValue()+"', "+ 1 +");");
            this.createPopUpWindow("Task has been added" , true);
        }
        else System.out.println ("error");
    }
    private void createPopUpWindow(String message, boolean closeWindow){
        Window newWindow = new Window("PopUp", "/Views/PopUpWindow.fxml", "/styles/mainStyle.css", "/data/photos/popUpIcon.png", 235, 92);
        newWindow.initWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        if(closeWindow)scene4Controller.transferMessage(message, (Stage) this.newTitle.getScene().getWindow());
        else scene4Controller.transferMessage(message, null);
        newWindow.showWindow();
    }
}
