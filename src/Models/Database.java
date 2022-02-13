package Models;

import Controllers.PopUpWindowController;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    /*Variables*/
    private static String name = "kanban", port = "3306", user = "root", password = "", status;
    private static final ArrayList<Task> tasks = new ArrayList<>();
    private static Connection connection;
    /*Get & Set functions*/
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
    public static ArrayList<Task> getTasks(){
        return tasks;
    }

    public static void connectToDatabase(){
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:"+getPort()+"/"+getName()+"", ""+getUser()+"", ""+getPassword()+"");
            connection = myConn;
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static ResultSet runQuery(String query){
        try{
            Statement myStat = connection.createStatement();
            if(query.startsWith("UPDATE") || query.startsWith("DELETE"))myStat.executeUpdate(query);
            else if(query.startsWith("INSERT"))myStat.execute(query);
            else return myStat.executeQuery(query);
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    public static void databaseConnectionError(){
        createPopUpWindow("Database error!!!");
    }
    /*Gets data from database and inserts it into order list*/
    public static void downloadListOfTask(){
        try{
            tasks.clear();
            ResultSet myRes = runQuery("SELECT * FROM tasks");
            while(myRes.next())
                tasks.add(new Task(myRes.getInt("id"), myRes.getInt("priority"), myRes.getInt("status"), myRes.getString("description"), myRes.getString("title"), myRes.getString("date")));
        }
        catch(Exception exc){
            databaseConnectionError();
            exc.printStackTrace();
        }
    }
    private static void createPopUpWindow(String message){
        Window newWindow = new Window("PopUp Window", "/Views/PopUpWindow.fxml", "/styles/style.css", "/data/photos/popUpIcon.png",  235, 92);
        newWindow.showWindow();
        PopUpWindowController scene4Controller = newWindow.getLoader().getController();
        scene4Controller.transferMessage(message);
    }
}
