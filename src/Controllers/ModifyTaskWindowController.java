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
    public void initialize(){
        this.loadItemsToChoiceBox();
    }
    public void loadItemsToChoiceBox(){
        this.priorityChoiceBox.getItems().add("Low");
        this.priorityChoiceBox.getItems().add("Medium");
        this.priorityChoiceBox.getItems().add("High");
    }
    public void loadDataToFields(){
        this.modifiedTitle.setText(this.order.getTitle());
        if(this.order.getPriority()==1)this.priorityChoiceBox.setValue("Low");
        else if(this.order.getPriority()==2)this.priorityChoiceBox.setValue("Medium");
        else if(this.order.getPriority()==3)this.priorityChoiceBox.setValue("High");
        this.modifiedDeadline.setValue(LocalDate.parse(this.order.getDate()));
        this.modifiedDescription.setText(this.order.getDescription());
    }
    public void sendModifiedTaskToDatabase(){
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kanban", "root", "");
            Statement myStat = myConn.createStatement();
            int priorityNumber = switch (this.priorityChoiceBox.getValue()) {
                case "Low" -> 1;
                case "Medium" -> 2;
                case "High" -> 3;
                default -> 0;
            };
            if(this.modifiedTitle.getText().length()==0)this.modifiedTitle.setText("There is a lack of title");
            if(this.modifiedDescription.getText().length()==0)this.modifiedDescription.setText("There is a lack of description");
            myStat.execute("UPDATE orders SET title = '"+this.modifiedTitle.getText()+"', description = '"+this.modifiedDescription.getText()+"', priority = "+priorityNumber+", date = '"+this.modifiedDeadline.getValue()+"' WHERE id="+this.order.getId()+";");
            this.createPopUpWindow("Order has been modified");
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
    }
    public void createPopUpWindow(String message){
        Window newWindow = new Window("PopUp Window", "/Views/PopUpWindow.fxml", "/styles/style.css", 235, 92);
        newWindow.showWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        scene4Controller.transferMessage(message, (Stage) this.modifiedTitle.getScene().getWindow());
    }
}
