package Controllers;

import Models.Window;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Models.Order;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

public class ModifyTaskWindowController extends MainWindowController{
    @FXML private TextField modifiedTitle;
    @FXML private ChoiceBox <String> priorityChoiceBox;
    @FXML private DatePicker modifiedDeadline;
    @FXML private TextArea modifiedDescription;
    private Order order;
    public void transferData(int id, String title, String description, int priority, String date, int status){
        this.order = new Order(id, priority, status, description, title, date);
        this.loadDataToFields();
    }
    @FXML
    private void initialize(){
        this.loadItemsToChoiceBox();
    }
    private void loadItemsToChoiceBox(){
        this.priorityChoiceBox.getItems().add("Low");
        this.priorityChoiceBox.getItems().add("Medium");
        this.priorityChoiceBox.getItems().add("High");
    }
    private void loadDataToFields(){
        this.modifiedTitle.setText(this.order.getTitle());
        if(this.order.getPriority()==1)this.priorityChoiceBox.setValue("Low");
        else if(this.order.getPriority()==2)this.priorityChoiceBox.setValue("Medium");
        else if(this.order.getPriority()==3)this.priorityChoiceBox.setValue("High");
        this.modifiedDeadline.setValue(LocalDate.parse(this.order.getDate()));
        this.modifiedDescription.setText(this.order.getDescription());
    }
    private boolean checkIfAllInsertedDataIsCorrectAndFixIt(){
        if(this.modifiedTitle.getText().length()>50){
            this.createPopUpWindow("Title is too big", false);
            return false;
        }
        else if(this.modifiedDescription.getText().length()>255){
            this.createPopUpWindow("Description is too big", false);
            return false;
        }
        if(this.modifiedTitle.getText().length()==0)this.modifiedTitle.setText("There is a lack of title");
        if(this.modifiedDescription.getText().length()==0)this.modifiedDescription.setText("There is a lack of description");
        return true;
    }
    public void sendModifiedTaskToDatabase(){
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
                myStat.execute("UPDATE orders SET title = '"+this.modifiedTitle.getText()+"', description = '"+this.modifiedDescription.getText()+"', priority = "+priorityNumber+", date = '"+this.modifiedDeadline.getValue()+"' WHERE id="+this.order.getId()+";");
                this.createPopUpWindow("Order has been modified", true);
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
        if(closeWindow)scene4Controller.transferMessage(message, (Stage) this.modifiedDescription.getScene().getWindow());
        else scene4Controller.transferMessage(message, null);
    }
}
