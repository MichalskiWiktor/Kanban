package Models;

public class Task {

    private final int id, priority, status;
    private final String title, description, date;

    public Task(int id, int priority, int status, String description, String title, String date) {
        this.id = id;
        this.priority = priority;
        this.status = status;
        this.title = title;
        this.description = description;
        this.date = date;
    }
    public int getId(){
        return this.id;
    }
    public int getPriority(){
        return this.priority;
    }
    public int getStatus(){
        return this.status;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDescription(){
        return this.description;
    }
    public String getDate(){
        return this.date;
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + this.id + " , title=" + this.title + ", date=" + this.date + '}';
    }

}
