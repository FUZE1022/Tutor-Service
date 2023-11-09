package Controller;

import Model.DataCenter;
import Model.Student;
import Model.StudentLinkedList;
import Util.GoToNewWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

public class AdminLoginViewController {
    @FXML
    private TextField adminLoginUsernameTf, adminLoginPasswordTf;
    @FXML
    private Button adminLoginEnterBtn, adminLoginBackBtn;
    @FXML
    private Label adminLoginLabel;
    private StudentLinkedList studentLinkedList = DataCenter.getInstance().getStudentLinkedList();

    public void enter(ActionEvent event) throws IOException {
        adminLogin(event);
    }

    private void adminLogin(ActionEvent event) throws IOException {
        if(adminLoginUsernameTf.getText().equalsIgnoreCase("Admin") &&
                adminLoginPasswordTf.getText().equals("SCCC")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText("Where would you like to go?");
            ButtonType cancelButtonType = new ButtonType("Cancel");
            ButtonType adminButtonType = new ButtonType("Admin Dashboard");
            ButtonType tutorButtonType = new ButtonType("Tutor Dashboard");
            alert.getButtonTypes().setAll(cancelButtonType, adminButtonType, tutorButtonType);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == adminButtonType) {
                    GoToNewWindow.goToNewWindow(event, "/Views/AdminView.fxml", "Admin Dashboard");
                } else if (result.get() == tutorButtonType) {
                    GoToNewWindow.goToNewWindow(event, "/Views/TutorView.fxml", "Tutor Dashboard");
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Username or Password");
            alert.setContentText("Please try again");
            alert.showAndWait();
        }
    }

    public void goBack(ActionEvent event) throws IOException{
        GoToNewWindow.goToNewWindow(event, "/Views/MainView.fxml", "Tutor Service");
    }
}
