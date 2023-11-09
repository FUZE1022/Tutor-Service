package Controller;

import Model.*;
import Util.GoToNewWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class TutorViewController implements Initializable {
    @FXML
    private Button backBtnTutorView, tutorSessionStartBtn, tutorSessionEndBtn, tutorSessionMoveBtn,
            tutorSessionRemoveBtn;
    @FXML
    private TableView<StudentLoggedIn> queueTv;
    @FXML
    private TableColumn<StudentLoggedIn, String> queueIdTc, queueNameTc, queueCourseTc, queueTopicTc;
    @FXML
    private ListView<String> tutorSessionLv;
    private StudentLoggedInQueue studentLoggedInQueue = DataCenter.getInstance().getStudentLoggedInQueue();
    private StudentHistory studentHistory = DataCenter.getInstance().getStudentLoggedInHistoryLinkedList();
    private boolean inSession = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTableView();
    }

    public void move() {
        StudentLoggedIn selectedStudent = queueTv.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            if (!tutorSessionLv.getItems().contains(selectedStudent.getStudent().getName())) {
                tutorSessionLv.getItems().add(selectedStudent.getStudent().getName());
            } else {
                errorCanNotPutDups();
            }
        } else {
            warningNoStudentSelected();
        }
    }

    public void remove() {
        String selectedStudent = tutorSessionLv.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            tutorSessionLv.getItems().remove(selectedStudent);
        } else {
            warningNoStudentSelected();
        }
    }

    public void startTutorSession() {
        if(inSession) {
            sessionAlreadyStarted();
            return;
        }
        ObservableList<String> students = tutorSessionLv.getItems();
        if (!students.isEmpty()) {
            for (String studentName : students) {
                StudentLoggedIn studentLoggedIn = studentLoggedInQueue.getStudentLoggedIn(studentName).orElse(null);
                if(studentLoggedIn != null) {
                    studentLoggedIn.startTime();
                }
            }
            sessionHasStarted();
            inSession = true;
            tutorSessionLv.setDisable(true);
            tutorSessionMoveBtn.setDisable(true);
            tutorSessionRemoveBtn.setDisable(true);
        } else {
            noStudentsInSession();
        }
    }

    public void endTutorSession() {
        if(!inSession) {
            sessionNotStarted();
            return;
        }
        ObservableList<String> students = tutorSessionLv.getItems();
        if (!students.isEmpty()) {
            for (String studentName : students) {
                StudentLoggedIn studentLoggedIn = studentLoggedInQueue.getStudentLoggedIn(studentName).orElse(null);
                if(studentLoggedIn != null) {
                    studentLoggedIn.endTime();
                    studentLoggedInQueue.logOutStudent(studentLoggedIn);
                    studentHistory.addStudentToHistory(new StudentLoggedIn(studentLoggedIn.getStudent(),
                            studentLoggedIn.getCourses(), studentLoggedIn.getTopic(), studentLoggedIn.getInstructor(), false,
                            studentLoggedIn.getDate(), studentLoggedIn.getTimeIn(), studentLoggedIn.getTimeOut()));
                }
            }
            sessionHasEnded();
            inSession = false;
            tutorSessionLv.getItems().clear();
            tutorSessionLv.setDisable(false);
            tutorSessionMoveBtn.setDisable(false);
            tutorSessionRemoveBtn.setDisable(false);
            queueTv.getItems().clear();
            populateTableView();
        } else {
            noStudentsInSession();
        }
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

    public void backBtnOnAction(ActionEvent event) throws IOException {
        GoToNewWindow.goToNewWindow(event, "/Views/MainView.fxml", "Tutor Service");
    }

    private void errorCanNotPutDups() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Warning");
        alert.setHeaderText("Can not put duplicates");
        alert.setContentText("Please try again and click another student");
        alert.showAndWait();
    }

    private void warningNoStudentSelected() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("No student selected");
        alert.setContentText("Please try again and click a student");
        alert.showAndWait();
    }

    private void noStudentsInSession() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("No students in Session");
        alert.setContentText("Please add students to the session");
        alert.showAndWait();
    }

    private void sessionNotStarted() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Session not started");
        alert.setContentText("Please start the session");
        alert.showAndWait();
    }

    private void sessionAlreadyStarted() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Tutor Session already Started");
        alert.setContentText("Please end the session");
        alert.showAndWait();
    }

    private void sessionHasStarted() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Tutor Session has Started");
        alert.setContentText("Enjoy your session");
        alert.showAndWait();
    }

    private void sessionHasEnded() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Tutor Session has Ended");
        alert.setContentText("Have a good day");
        alert.showAndWait();
    }
}