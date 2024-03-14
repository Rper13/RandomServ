package Entry;

import API.Service;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.net.Socket;
import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;

public class DashboardController {

    Service ser = Service.getInstance();
    @FXML
    private TextFlow chatFlow;

    @FXML
    private TextFlow logsFlow;

    @FXML
    public void initialize() {

        int ssize = ser.getClients().size();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> {

            if(ssize != ser.getClients().size()) {
                logsFlow.getChildren().removeAll(logsFlow.getChildren());
                for (Socket client : ser.getClients()) {
                    updateLogs(client.toString());
                }
            }

        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }



    public void updateLogs(String log){
        logsFlow.getChildren().add(new Text(log));
    }


    public void updateChat(String msg){
        chatFlow.getChildren().add(new Text(msg));
    }


}