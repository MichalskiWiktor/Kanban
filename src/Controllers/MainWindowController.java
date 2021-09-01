package Controllers;

import Models.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import Models.Order;
public class MainWindowController{
    @FXML private ListView <Order> toDoList;
    @FXML private ListView <Order> inProgressList;
    @FXML private ListView <Order> doneList;
    @FXML private Button refreshBtn;
    private final ArrayList <Order> orders = new ArrayList<>();

    @FXML public void initialize(){
        this.getDataFromDatabase();
        this.loadDataToLists();
        this.initPhotos();
    }
    public void initPhotos(){
        ImageView imageView = new ImageView(getClass().getResource("/data/photos/refresh.png").toExternalForm());
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        imageView.setY(10);
        this.refreshBtn.setGraphic(imageView);
    }
    public ResultSet connectToDatabase(String query){
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kanban", "root", "");
            Statement myStat = myConn.createStatement();
            if(query.startsWith("UPDATE") || query.startsWith("DELETE"))myStat.executeUpdate(query);
            else return myStat.executeQuery(query);
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    public void databaseConnectionError(){
        this.createPopUpWindow("Database error!!!");
    }
    public void getDataFromDatabase(){
        try{
            ResultSet myRes = this.connectToDatabase("SELECT * FROM orders");
            while(myRes.next())
                this.orders.add(new Order(myRes.getInt("id"), myRes.getInt("priority"), myRes.getInt("status"), myRes.getString("description"), myRes.getString("title"), myRes.getString("date")));
        }
        catch(Exception exc){
            this.databaseConnectionError();
            exc.printStackTrace();
        }
    }
    public void loadDataToLists(){
        this.toDoList.setCellFactory((lv) -> OrderListCell.newInstance());
        this.inProgressList.setCellFactory((lv) -> OrderListCell.newInstance());
        this.doneList.setCellFactory((lv) -> OrderListCell.newInstance());
        ObservableList<Order> ordersToDo = FXCollections.observableArrayList();
        ObservableList<Order> ordersInProgress = FXCollections.observableArrayList();
        ObservableList<Order> ordersDone = FXCollections.observableArrayList();
        for(Order order : this.orders) {
            if (order.getStatus() == 1) {
                ordersToDo.add(order);
                this.toDoList.setItems(ordersToDo);
            } else if (order.getStatus() == 2) {
                ordersInProgress.add(order);
                this.inProgressList.setItems(ordersInProgress);
            } else if (order.getStatus() == 3) {
                ordersDone.add(order);
                this.doneList.setItems(ordersDone);
            }
        }
    }
    @FXML public void handleMouseClickToDoList() {
        this.inProgressList.getSelectionModel().clearSelection();
        this.doneList.getSelectionModel().clearSelection();
    }
    @FXML public void handleMouseClickInProgressList() {
        this.toDoList.getSelectionModel().clearSelection();
        this.doneList.getSelectionModel().clearSelection();
    }
    @FXML public void handleMouseClickDoneList() {
        this.toDoList.getSelectionModel().clearSelection();
        this.inProgressList.getSelectionModel().clearSelection();
    }
    public void refreshLists(){
        this.clearAllListviews();
        this.orders.clear();
        this.getDataFromDatabase();
        this.loadDataToLists();
    }
    public void clearAllListviews(){
        this.toDoList.getItems().clear();
        this.inProgressList.getItems().clear();
        this.doneList.getItems().clear();
    }
    public void addNewTask(){
        Window newWindow = new Window("Add New Task", "/Views/AddNewTaskWindow.fxml", "/styles/style2.css", 342, 353);
        newWindow.showWindow();
    }
    public void showDetailsWindow(){
        Order order = this.findSelectedOrder();
        if(order!=null){
            Window newWindow = new Window("Details", "/Views/showDetailsWindow.fxml", "/styles/style.css", 358, 255);
            newWindow.showWindow();
            ShowDetailsWindowController scene4Controller = newWindow.getLoader().getController();
            scene4Controller.transferData(order.getId(), order.getTitle(), order.getDescription(), order.getPriority(), order.getDate(), order.getStatus());
        }
        else this.createPopUpWindow("You have to pick an element!!");
    }
    public void modifyTask(){
        Order order = this.findSelectedOrder();
        if(order!=null){
            Window newWindow = new Window("Modify Task", "/Views/ModifyTaskWindow.fxml", "/styles/style2.css", 342, 353);
            newWindow.showWindow();
            ModifyTaskWindowController scene4Controller = newWindow.getLoader().getController();
            scene4Controller.transferData(order.getId(), order.getTitle(), order.getDescription(), order.getPriority(), order.getDate(), order.getStatus());
        }
        else this.createPopUpWindow("You have to pick an element!!");
    }
    public void deleteTask(){
        Order order = this.findSelectedOrder();
        if(order!=null){
            try{
                this.connectToDatabase(("DELETE FROM orders WHERE id=" + order.getId()));
                this.refreshLists();
            }catch(Exception exc){
                exc.printStackTrace();
            }
        }
        else this.createPopUpWindow("You have to pick an element!!");
    }
    public void moveTask(){
        Order order = this.findSelectedOrder();
       if(order!=null){
           try{
               this.connectToDatabase(("UPDATE orders SET status = " + (order.getStatus() + 1) + " WHERE id=" + order.getId() + " AND status<>3"));
               this.refreshLists();
           }catch(Exception exc){
               exc.printStackTrace();
           }
       }
       else this.createPopUpWindow("You have to pick an element!!");
    }
    public Order findSelectedOrder(){
        Order order;
        if(this.orders.contains(this.toDoList.getSelectionModel().getSelectedItem()))
            order = this.toDoList.getSelectionModel().getSelectedItem();
        else if(this.orders.contains(this.inProgressList.getSelectionModel().getSelectedItem()))
            order = this.inProgressList.getSelectionModel().getSelectedItem();
        else if(this.orders.contains(this.doneList.getSelectionModel().getSelectedItem()))
            order = this.doneList.getSelectionModel().getSelectedItem();
        else order = null;
        return order;
    }
    public void createPopUpWindow(String message){
        Window newWindow = new Window("PopUp Window", "/Views/PopUpWindow.fxml", "/styles/style.css", 235, 92);
        newWindow.showWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        scene4Controller.transferMessage(message, null);
    }
}