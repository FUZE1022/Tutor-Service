package Controller;

import Model.*;
import Util.GoToNewWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable{
    @FXML
    private MenuBar mainMenuBar;
    @FXML
    private MenuItem mainFileMi;
    @FXML
    private TextField idTfMainView, firstNameTfMainView, lastNameTfMainView;
    @FXML
    private PasswordField tutorPasswordTfMainVIew;
    @FXML
    private Hyperlink adminHlMain, newStudentHi;
    @FXML
    private Button enterBtnMain, enterPasswordBtnMainView, backBtnMainView, studentQueueBtn;
    @FXML
    private Label tutorLabel1, tutorLabel2, tutorLabel3;
    @FXML
    private Button logoutBtn;
    private StudentLinkedList studentLinkedList = DataCenter.getInstance().getStudentLinkedList();
    private StudentLoggedInQueue studentLoggedInQueue = DataCenter.getInstance().getStudentLoggedInQueue();
    private StudentHistory studentHistory = DataCenter.getInstance().getStudentLoggedInHistoryLinkedList();
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
    private Alert warningAlert = new Alert(Alert.AlertType.WARNING);

    public void enterBtnOnAction(ActionEvent event) throws IOException {
        String idText = idTfMainView.getText().trim();
        String firstNameText = firstNameTfMainView.getText().trim();
        String lastNameText = lastNameTfMainView.getText().trim();
        if (!idTfMainView.isDisabled()) {
            if (!idText.matches("[0-9]+") || idText.length() != 8)
                showInvalidIdAlert();
            else
                processStudent(event);
        } else if (!firstNameTfMainView.isDisabled() && !lastNameTfMainView.isDisabled()) {
            if (!firstNameText.matches("[a-zA-Z]+") || !lastNameText.matches("[a-zA-Z]+"))
                showInvalidNameAlert();
            else
                processStudent(event);
        }
    }

    private void showInvalidIdAlert() {
        alert.setTitle("Error");
        alert.setHeaderText("Invalid ID");
        alert.setContentText("Please enter a valid ID which can only be numbers and must be 8 digits long");
        alert.showAndWait();
    }

    private void showInvalidNameAlert() {
        alert.setTitle("Error");
        alert.setHeaderText("Invalid First or Last Name");
        alert.setContentText("Please enter a valid First or Last Name which can only be letters");
        alert.showAndWait();
    }

    private void processStudent(ActionEvent event) throws IOException {
        if (studentLinkedList.search(getStudentBySearch())) {
            if(studentLoggedInQueue.containsStudent(getStudentBySearch())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Student is already logged in");
                alert.showAndWait();
                return;
            }
            DataCenter.getInstance().setCurrentStudent(getStudentBySearch());
            GoToNewWindow.goToNewWindow(event, "/Views/studentInformationView.fxml", "Student Information");
        } else {
            infoAlertStudentNotFound();
        }
    }

    public void logout(ActionEvent event){
        StudentLoggedIn studentLoggedIn = studentLoggedInQueue.getStudentLoggedIn(getStudentBySearch()).orElse(null);
        if(studentLoggedIn == null){
            infoAlertStudentNotFound();
            return;
        }
        if (studentLoggedInQueue.containsStudent(getStudentBySearch())) {
            if(studentLoggedIn.didTimeStart())
                studentLoggedIn.endTime();
            studentLoggedIn.setLoggedIn(false);
            studentLoggedInQueue.logOutStudent(studentLoggedIn);
            studentHistory.addStudentToHistory(new StudentLoggedIn(studentLoggedIn.getStudent(),
                    studentLoggedIn.getCourses(), studentLoggedIn.getTopic(), studentLoggedIn.getInstructor(), false,
                    studentLoggedIn.getDate(), studentLoggedIn.getTimeIn(), studentLoggedIn.getTimeOut()));
            if(!studentLoggedInQueue.isEmpty()) {
                studentLoggedInQueue.getStudentQueue().get(0).startTime();
            }
            loggedOutInfo();
            clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You are not logged in");
            alert.showAndWait();
        }
    }

    private void clear() {
        idTfMainView.clear();
        firstNameTfMainView.clear();
        lastNameTfMainView.clear();
    }

    private void loggedOutInfo() {
        infoAlert.setTitle("Information");
        infoAlert.setHeaderText("Student has been logged out");
        infoAlert.setContentText("Have a good day!");
        infoAlert.showAndWait();
    }

    private Student getStudentBySearch() {
        String idText = idTfMainView.getText();
        String firstNameText = firstNameTfMainView.getText();
        String lastNameText = lastNameTfMainView.getText();
        Student studentById = studentLinkedList.searchStudentId(idText).orElse(null);
        Student studentByName = studentLinkedList.searchFirstLastName(firstNameText, lastNameText).orElse(null);
        if(studentByName != null)
            return studentByName;
        if(studentById != null){
            return studentById;
        }
        return null;
    }

    public void adminHlOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/AdminLoginView.fxml"));
        Stage stage = new Stage();
        Scene newScene = new Scene(fxmlLoader.load(), 456, 400);
        stage.setTitle("Login");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        stage.setScene(newScene);
        stage.show();
    }

    public void newStudentHlOnAction(ActionEvent event) throws IOException {
        GoToNewWindow.goToNewWindow(event, "/Views/newStudentView.fxml", "New Student");
    }

    public void goToStudentQueue(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/queueView.fxml"));
        Stage stage = new Stage();
        Scene newScene = new Scene(fxmlLoader.load(), 443, 423);
        stage.setTitle("Student Queue");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        stage.setScene(newScene);
        stage.show();
    }

    public void idKeyTyped() {
        String id = idTfMainView.getText();
        if(id.matches("[0-9]+") && id.length() == 8) {
            idTfMainView.setStyle("-fx-text-inner-color: green;");
            enterBtnMain.setDisable(false);
            logoutBtn.setDisable(false);
        } else if(id.isEmpty()){
            idTfMainView.setStyle("-fx-text-inner-color: red;");
            enterBtnMain.setDisable(true);
            logoutBtn.setDisable(true);
            firstNameTfMainView.setDisable(false);
            lastNameTfMainView.setDisable(false);
        } else {
            idTfMainView.setStyle("-fx-text-inner-color: red;");
            enterBtnMain.setDisable(true);
            logoutBtn.setDisable(true);
            firstNameTfMainView.setDisable(true);
            lastNameTfMainView.setDisable(true);
        }
    }

    public void firstNameKeyTyped() {
        firstLastKeyTyped(firstNameTfMainView, lastNameTfMainView);
    }

    public void lastNameKeyTyped(){
        firstLastKeyTyped(lastNameTfMainView, firstNameTfMainView);
    }

    private void firstLastKeyTyped(TextField lastNameTfMainView, TextField firstNameTfMainView) {
        if (lastNameTfMainView.getText().matches("[a-zA-Z]+") && !firstNameTfMainView.getText().isEmpty()
                && firstNameTfMainView.getText().matches("[a-zA-Z]+") && !lastNameTfMainView.getText().isEmpty()) {
            lastNameTfMainView.setStyle("-fx-text-inner-color: green;");
            firstNameTfMainView.setStyle("-fx-text-inner-color: green;");
            enterBtnMain.setDisable(false);
            logoutBtn.setDisable(false);
            idTfMainView.setDisable(true);
        } else if (lastNameTfMainView.getText().isEmpty() || firstNameTfMainView.getText().isEmpty()) {
            lastNameTfMainView.setStyle("-fx-text-inner-color: red;");
            firstNameTfMainView.setStyle("-fx-text-inner-color: red;");
            enterBtnMain.setDisable(true);
            logoutBtn.setDisable(true);
            idTfMainView.setDisable(false);
        } else {
            lastNameTfMainView.setStyle("-fx-text-inner-color: red;");
            firstNameTfMainView.setStyle("-fx-text-inner-color: red;");
            enterBtnMain.setDisable(true);
            logoutBtn.setDisable(true);
            idTfMainView.setDisable(true);
        }
    }

    private void infoAlertStudentNotFound() {
        infoAlert.setTitle("Information");
        infoAlert.setHeaderText("Student was not found");
        infoAlert.setContentText("Please enter another ID or First and Last Name");
        infoAlert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}