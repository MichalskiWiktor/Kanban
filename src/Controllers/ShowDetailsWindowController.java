package Controllers;

import Models.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShowDetailsWindowController {
    private Order order;
    @FXML private Label idLabel;
    @FXML private Label priorityLabel;
    @FXML private Label statusLabel;
    @FXML private Label titleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label dateLabel;
    public void transferData(int id, String title, String description, int priority, String date, int status){
        this.order = new Order(id, priority, status, description, title, date);
        this.loadDataToLabels();
    }
    private void loadDataToLabels(){
        this.idLabel.setText(this.idLabel.getText() + " " + this.order.getId());

        if(this.order.getPriority()==1)this.priorityLabel.setText(this.priorityLabel.getText() + " Low");
        else if(this.order.getPriority()==2)this.priorityLabel.setText(this.priorityLabel.getText() + " Medium");
        else if(this.order.getPriority()==3)this.priorityLabel.setText(this.priorityLabel.getText() + " High");

        if(this.order.getStatus()==1)this.statusLabel.setText(this.statusLabel.getText() + " To do");
        else if(this.order.getStatus()==2)this.statusLabel.setText(this.statusLabel.getText() + " In progress");
        else if(this.order.getStatus()==3)this.statusLabel.setText(this.statusLabel.getText() + " Done");

        this.titleLabel.setText(this.titleLabel.getText() + " " + this.order.getTitle());
        this.descriptionLabel.setText(this.descriptionLabel.getText() + " " + this.order.getDescription());
        this.dateLabel.setText(this.dateLabel.getText() + " " + this.order.getDate());
    }
}
