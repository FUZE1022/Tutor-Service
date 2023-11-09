package Controller;

import Model.DataCenter;
import Model.StudentLoggedIn;
import Model.StudentLoggedInQueue;
import Util.GoToNewWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class QueueViewController implements Initializable {
    @FXML
    private Button backBtn;
    @FXML
    private TableView<StudentLoggedIn> queueTv;
    @FXML
    private TableColumn<StudentLoggedIn, String> queueIdTc, queueNameTc, queueCourseTc, queueTopicTc;

    public void goBack(ActionEvent event) throws IOException {
        GoToNewWindow.goToNewWindow(event, "/Views/mainView.fxml", "Tutor Service");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTableView();
    }

    private void populateTableView() {
        queueTv.getItems().clear();

        StudentLoggedInQueue studentLoggedInQueue = DataCenter.getInstance().getStudentLoggedInQueue();
        LinkedList<StudentLoggedIn> studentQueue = studentLoggedInQueue.getStudentQueue();

        queueIdTc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudent().getId()));
        queueNameTc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudent().getName()));
        queueCourseTc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourses()));
        queueTopicTc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTopic()));

        queueTv.getItems().addAll(studentQueue);
    }
}
