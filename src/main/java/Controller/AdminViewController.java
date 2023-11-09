package Controller;

import Model.*;
import Util.BackUp;
import Util.CsvParser;
import Util.Export;
import Util.GoToNewWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminViewController implements Initializable {
    @FXML
    private Button backBtnAdminView, LogOutAllBtn, importFileBtn, exportFileBtn, filterDatesBtn;
    @FXML
    private Label totalTime;
    @FXML
    private DatePicker datePickerBegin, datePickerEnd;
    @FXML
    private TableView<StudentLoggedIn> infoTv;
    @FXML
    private TableColumn<StudentLoggedIn, String> idTc, nameTc, courseTc, insturctorTc, topicTc, timeInTc, timeOutTc,
            loggedInTc, dateTc;
    private FileChooser fileChooser = new FileChooser();
    private File file;
    private StudentHistory studentHistory = DataCenter.getInstance().getStudentLoggedInHistoryLinkedList();
    private LocalDate originalStartDate;
    private LocalDate originalEndDate;
    private LinkedList<StudentLoggedIn> filteredStudentHistory = new LinkedList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("Data"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        populateTableView();
        totalTime.setText("Total time: " + studentHistory.calculateTotalDurationAsString());
        originalStartDate = datePickerBegin.getValue();
        originalEndDate = datePickerEnd.getValue();
    }

    public void populateTableView() {
        infoTv.getItems().clear();
        StudentHistory studentHistoryLinkedList = DataCenter.getInstance().getStudentLoggedInHistoryLinkedList();
        LinkedList<StudentLoggedIn> studentHistory = studentHistoryLinkedList.getStudentHistory();

        idTc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getId()));
        nameTc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getName()));
        courseTc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourses()));
        insturctorTc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstructor()));
        topicTc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTopic()));
        timeInTc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeIn().toString()));
        timeOutTc.setCellValueFactory(cellData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            if (cellData.getValue().getTimeOut() != null) {
                property.setValue(cellData.getValue().getTimeOut().toString());
            } else {
                property.setValue("");
            }
            return property;
        });
        loggedInTc.setCellValueFactory(cellData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            if (cellData.getValue().getLoggedIn()) {
                property.setValue("In");
            } else {
                property.setValue("Out");
            }
            return property;
        });
        dateTc.setCellValueFactory(cellData -> {
            SimpleStringProperty property = new SimpleStringProperty();
            if (cellData.getValue().getDate() != null) {
                property.setValue(cellData.getValue().getDate().toString());
            } else {
                property.setValue("");
            }
            return property;
        });

        infoTv.getItems().addAll(studentHistory);
    }

    public void logOutAll() {
        StudentLoggedInQueue studentLoggedInQueue = DataCenter.getInstance().getStudentLoggedInQueue();
        StudentHistory studentHistory = DataCenter.getInstance().getStudentLoggedInHistoryLinkedList();
        if(!studentLoggedInQueue.isEmpty()) {
            studentLoggedInQueue.logOutAllStudents(studentHistory);
            infoTv.getItems().clear();
            populateTableView();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("All students logged out");
            alert.setContentText("All students logged out successfully");
            alert.showAndWait();
            BackUp.saveData();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("No students logged in");
            alert.setContentText("No students logged in to log out");
            alert.showAndWait();
        }
    }

    public void filterResults(ActionEvent event) {
        LocalDate startDate = datePickerBegin.getValue();
        LocalDate endDate = datePickerEnd.getValue();
        if (startDate == null || endDate == null) {
            showAlert("Please select both start and end dates.");
            return;
        }
        if (startDate.isAfter(endDate)) {
            showAlert("Start date cannot be later than end date.");
            return;
        }
        if (endDate.isBefore(startDate)) {
            showAlert("End date cannot be earlier than start date.");
            return;
        }
        LinkedList<StudentLoggedIn> filteredData = new LinkedList<>();
        //if (!startDate.equals(originalStartDate) || !endDate.equals(originalEndDate)) {
            for (StudentLoggedIn item : studentHistory.getStudentHistory()) {
                LocalDate itemDate = item.getDate();
                if (itemDate != null && (itemDate.isEqual(startDate) || itemDate.isEqual(endDate) || (itemDate.isAfter(startDate) && itemDate.isBefore(endDate)))) {
                    filteredData.add(item);
                }
            }
            infoTv.getItems().setAll(filteredData);
            originalStartDate = startDate;
            originalEndDate = endDate;
            totalTime.setText("Total time for this frame: " + studentHistory.calculateTotalDurationAsString(filteredData));
       // }
        filteredStudentHistory  = filteredData;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Date Range");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void importFile(ActionEvent event) throws IOException {
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            StudentLinkedList newStudentLinkedList = CsvParser.readCsvFile(file.getAbsolutePath());
            if (newStudentLinkedList != null) {
                DataCenter.getInstance().setStudentLinkedList(newStudentLinkedList);
                BackUp.saveData();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("File Imported");
                alert.setContentText("File imported successfully\n" +
                        "Going to main menu.");
                alert.showAndWait();
                GoToNewWindow.goToNewWindow(event, "/Views/mainView.fxml", "Tutor Service");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error Importing File");
                alert.setContentText("Failed to read the CSV file.");
                alert.showAndWait();
            }
        }
    }

    public void exportFile() throws IOException {
        Alert exportAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exportAlert.setTitle("Export Format");
        exportAlert.setHeaderText("Choose the export format:");
        ButtonType cancelButtonType = new ButtonType("Cancel");
        ButtonType txtButtonType = new ButtonType(".txt");
        ButtonType csvButtonType = new ButtonType(".csv");
        exportAlert.getButtonTypes().setAll(cancelButtonType, txtButtonType, csvButtonType);
        Optional<ButtonType> result = exportAlert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == txtButtonType) {
                export(".txt");
            } else if (result.get() == csvButtonType) {
                export(".csv");
            }
        }
    }

    private void export(String fileExtension) throws IOException {
        if(filteredStudentHistory.isEmpty()) {
            showAlert("Filter results before exporting.");
            return;
        }
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "Export/Export_" + timestamp + fileExtension;
        Export.exportCSV(fileName);
    }
    public void backBtnOnAction(ActionEvent event) throws IOException {
        GoToNewWindow.goToNewWindow(event, "/Views/MainView.fxml", "Tutor Service");
    }
}