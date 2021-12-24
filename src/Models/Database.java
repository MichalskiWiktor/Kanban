package Models;

import Controllers.PopUpWindowController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    /*database static */
    private static String name = "kanban", port = "3306", user = "root", password = "", status;

    public static String getName(){
        return name;
    }
    public static String getPassword(){
        return password;
    }
    public static String getPort(){
        return port;
    }
    public static String getStatus(){
        return status;
    }
    public static String getUser(){
        return user;
    }
    public static void setName(String newName){
        name = newName;
    }
    public static void setPassword(String newPassword){
        password = newPassword;
    }
    public static void setPort(String newPort){
        port = newPort;
    }
    public static void setStatus(String newStatus){
        status = newStatus;
    }
    public static void setUser(String newUser){
        user = newUser;
    }

    private final ArrayList<Task> tasks = new ArrayList<>();
    public ArrayList<Task> getTasks(){
        return this.tasks;
    }

    public Database(){
        this.downloadListOfTask();
    }
    public ResultSet runQuery(String query){
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:"+getPort()+"/"+getName()+"", ""+getUser()+"", ""+getPassword()+"");
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
