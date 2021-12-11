package Models;

import Controllers.PopUpWindowController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    private final ArrayList<Task> tasks = new ArrayList<>();
    public ArrayList<Task> getTasks(){
        return this.tasks;
    }
    ///zamiast task beda tworzone tabele 1 ttabela do projektu
    public Database(){
        this.downloadListOfTask();
    }
    public ResultSet runQuery(String query){
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kanban", "root", "");
            Statement myStat = myConn.createStatement();
            if(query.startsWith("UPDATE") || query.startsWith("DELETE"))myStat.executeUpdate(query);
            else if(query.startsWith("INSERT"))myStat.execute(query);
            else return myStat.executeQuery(query);
            this.tasks.clear ();
            this.downloadListOfTask ();
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    public void databaseConnectionError(){
        this.createPopUpWindow("Database error!!!");
    }
    /*Gets data from database and inserts it into order list*/
    private void downloadListOfTask(){
        try{
            ResultSet myRes = this.runQuery("SELECT * FROM tasks");
            while(myRes.next())
                this.tasks.add(new Task(myRes.getInt("id"), myRes.getInt("priority"), myRes.getInt("status"), myRes.getString("description"), myRes.getString("title"), myRes.getString("date")));
        }
        catch(Exception exc){
            this.databaseConnectionError();
            exc.printStackTrace();
        }
    }
    private void createPopUpWindow(String message){
        Window newWindow = new Window("PopUp Window", "/Views/PopUpWindow.fxml", "/styles/style.css", "/data/photos/popUpIcon.png",  235, 92);
        newWindow.showWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        scene4Controller.transferMessage(message, null);
    }
}
