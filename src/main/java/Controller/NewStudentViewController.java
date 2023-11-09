package Controller;

import Model.DataCenter;
import Util.AddToCsvFile;
import Util.GoToNewWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class NewStudentViewController {
    @FXML
    private TextField newStudentAddStudentIdTf, newStudentFirstNameTf, newStudentLastNameTf;
    @FXML
    private Button newStudentBackBtn, randomIdBtn, addAnotherCourseBtn, addStudentBtn;
    @FXML
    private RadioButton cse110Rb, cse118Rb, cse148Rb, cse218Rb, cse222Rb, cse248Rb;
    private String courses = "";

    public void randomizeId() {
        int rand = (int) (Math.random() * (9999999 - 1000000) + 1000000);
        while(DataCenter.getInstance().getStudentLinkedList().searchStudentId("0" + rand).isEmpty()) {
            rand = (int) (Math.random() * (9999999 - 1000000) + 1000000);
        }
        newStudentAddStudentIdTf.setText("0" + rand);
        newStudentAddStudentIdTf.setStyle("-fx-text-inner-color: green;");
    }

    public void IdTfOnTyped() {
        String id = newStudentAddStudentIdTf.getText();
        if(id.length() == 8 && DataCenter.getInstance().getStudentLinkedList().searchStudentId(id).isEmpty() &&
                id.matches("[0-9]+"))
            newStudentAddStudentIdTf.setStyle("-fx-text-inner-color: green;");
        else
            newStudentAddStudentIdTf.setStyle("-fx-text-inner-color: red;");
    }

    public void firstNameTfOnTyped() {
        if(!newStudentFirstNameTf.getText().isEmpty() && newStudentFirstNameTf.getText().matches("[a-zA-Z]+"))
            newStudentFirstNameTf.setStyle("-fx-text-inner-color: green;");
        else
            newStudentFirstNameTf.setStyle("-fx-text-inner-color: red;");
    }

    public void lastNameTfOnTyped() {
        if(!newStudentLastNameTf.getText().isEmpty() && newStudentLastNameTf.getText().matches("[a-zA-Z]+"))
            newStudentLastNameTf.setStyle("-fx-text-inner-color: green;");
        else
            newStudentLastNameTf.setStyle("-fx-text-inner-color: red;");
    }

public void addStudent(ActionEvent event) throws IOException {
    if (!isInputValid()) {
        showValidationErrorAlert();
        return;
    }

    StringBuilder stringBuilder = new StringBuilder();
    if (cse110Rb.isSelected()) stringBuilder.append("CSE110 ");
    if (cse118Rb.isSelected()) stringBuilder.append("CSE118 ");
    if (cse148Rb.isSelected()) stringBuilder.append("CSE148 ");
    if (cse218Rb.isSelected()) stringBuilder.append("CSE218 ");
    if (cse222Rb.isSelected()) stringBuilder.append("CSE222 ");
    if (cse248Rb.isSelected()) stringBuilder.append("CSE248 ");

    String courses = stringBuilder.toString().trim();
    if (!courses.isEmpty()) {
        AddToCsvFile.addStudentToCsvFile("Data/students.csv", newStudentAddStudentIdTf.getText(),
                newStudentFirstNameTf.getText(),
                newStudentLastNameTf.getText(), courses);
        showSuccessAlert();
        clearFields();
        GoToNewWindow.goToNewWindow(event, "/Views/mainView.fxml", "Tutor Service");
    } else {
        showValidationErrorAlert();
    }
}

    private boolean isInputValid() {
        return newStudentAddStudentIdTf.getStyle().equals("-fx-text-inner-color: green;") &&
                newStudentFirstNameTf.getStyle().equals("-fx-text-inner-color: green;") &&
                newStudentLastNameTf.getStyle().equals("-fx-text-inner-color: green;") &&
                (cse110Rb.isSelected() || cse118Rb.isSelected() || cse148Rb.isSelected() ||
                        cse218Rb.isSelected() || cse222Rb.isSelected() || cse248Rb.isSelected());
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("Student has been added successfully.\nPlease go back to the main menu to see the changes.");
        alert.showAndWait();
    }

    private void showValidationErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("An Error has occurred.\nPlease enter a valid student information.");
        alert.showAndWait();
    }

    private void clearFields() {
        newStudentAddStudentIdTf.clear();
        newStudentFirstNameTf.clear();
        newStudentLastNameTf.clear();
        courses = "";
    }

    public void goBack(ActionEvent event) throws IOException {
        GoToNewWindow.goToNewWindow(event, "/Views/mainView.fxml", "Tutor Service");
    }
}
