package Controllers;

import Models.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShowDetailsWindowController {
    private Task task;
    @FXML private Label idLabel;
    @FXML private Label priorityLabel;
    @FXML private Label statusLabel;
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label dateLabel;
    public void transferData(int id, String title, String description, int priority, String date, int status){
        this.task = new Task(id, priority, status, description, title, date);
        this.loadDataToLabels();
    }
    private void loadDataToLabels(){
        this.idLabel.setText(this.idLabel.getText() + " " + this.task.getId());

        if(this.task.getPriority()==1)this.priorityLabel.setText(this.priorityLabel.getText() + " Low");
        else if(this.task.getPriority()==2)this.priorityLabel.setText(this.priorityLabel.getText() + " Medium");
        else if(this.task.getPriority()==3)this.priorityLabel.setText(this.priorityLabel.getText() + " High");

        if(this.task.getStatus()==1)this.statusLabel.setText(this.statusLabel.getText() + " To do");
        else if(this.task.getStatus()==2)this.statusLabel.setText(this.statusLabel.getText() + " In progress");
        else if(this.task.getStatus()==3)this.statusLabel.setText(this.statusLabel.getText() + " Done");

        this.titleLabel.setText(this.titleLabel.getText() + " " + this.task.getTitle());
        this.descriptionLabel.setText(this.descriptionLabel.getText() + " " + this.task.getDescription());
        this.dateLabel.setText(this.dateLabel.getText() + " " + this.task.getDate());
    }
}
