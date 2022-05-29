package Controllers;

import Models.Database;
import Models.Window;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddNewTaskWindowController extends MainWindowController{
    @FXML private TextField newTitle;
    @FXML private ChoiceBox <String> priorityChoiceBox;
    @FXML private DatePicker newDeadline;
    @FXML private TextArea newDescription;
    @FXML private Pane topPane;
    @FXML private ImageView closeImg;
    @FXML private ImageView minimizeImg;
    private final Database database = new Database ();
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private void initialize(){
        System.out.println("ok");
        this.loadItemsToChoiceBox();
        this.setDefaultDeadline();
        this.priorityChoiceBox.setValue("Low");
        this.waitEvent();
    }
    public void waitEvent(){
        this.closeImg.setOnMousePressed (event ->{
            Stage stage = (Stage) this.newDescription.getScene().getWindow();
            stage.close();
        });
        this.minimizeImg.setOnMousePressed (event ->{
            Stage stage = (Stage) this.newDescription.getScene().getWindow();
            stage.setIconified(true);
        });
        this.topPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        this.topPane.setOnMouseDragged(event -> {
            Stage stage = (Stage)topPane.getScene ().getWindow ();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
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
            this.createPopUpWindow("Title is too big");
            return false;
        }
        else if(this.newDescription.getText().length()>255){
            this.createPopUpWindow("Description is too big");
            return false;
        }
        if(this.newTitle.getText().length()==0)this.newTitle.setText("There is a lack of title");
        if(this.newDescription.getText().length()==0)this.newDescription.setText("There is a lack of description");
        return true;
    }
    /*Inserts data to the database*/
    public void addNewRecordToDatabase(){
        System.out.println("ok2");
        int priorityNumber = switch (this.priorityChoiceBox.getValue()) {
            case "Low" -> 1;
            case "Medium" -> 2;
            case "High" -> 3;
            default -> 0;
        };
        if(this.checkIfAllInsertedDataIsCorrectAndFixIt ()){
            this.database.runQuery("INSERT INTO tasks(title, description, priority, date, status) VALUES('" + this.newTitle.getText() + "', '" + this.newDescription.getText() + "', " +priorityNumber + ", '"+this.newDeadline.getValue()+"', "+ 1 +");");
            this.createPopUpWindow("Task has been added" );
        }
        else System.out.println ("error");
    }
    private void createPopUpWindow(String message){
        Window newWindow = new Window("PopUp", "/Views/PopUpWindow.fxml", "/styles/mainStyle.css", "/data/photos/popUpIcon.png", 235, 92);
        newWindow.initWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        scene4Controller.transferMessage(message);
        newWindow.showWindow();
    }
}
