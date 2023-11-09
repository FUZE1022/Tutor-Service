package Controller;

import Model.*;
import Util.GoToNewWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentInformationViewController implements Initializable {
    @FXML
    private Button backBtnStudentInformation, studentInfoEnterBtn;
    @FXML
    private TextField studentInfoIdTf, studentInfoNameTf, studentInfoTopicTf;
    @FXML
    private ListView<String> coursesListView;
    @FXML
    private ChoiceBox<String> studentInfoProfCb;
    @FXML
    private TextArea studentInfoTa;
    @FXML
    private Label courseDescLabel;
    private ObservableList<String> studentList = FXCollections.observableArrayList("Bin Li", "Xingbin Chen",
            "William McAllister");

    private Student currentStudent = DataCenter.getInstance().getCurrentStudent();
    private StudentLoggedInQueue studentLoggedInQueue = DataCenter.getInstance().getStudentLoggedInQueue();
    private StudentHistory studentHistory = DataCenter.getInstance().getStudentLoggedInHistoryLinkedList();

    private String[] courseDescriptions = {
            "Methods and techniques students can adopt to promote their perseverance and success at the" +
                    "College in general and in the Computer Science and Information Technology fields in particular." +
                    "Specific topics include college procedures and resources, academic advisement, time management" +
                    "goal-setting, test and note taking, health issues and other areas related to student success in a" +
                    "computer related fields in college",

            "An introductory programming course for the Computer Science major. Topics include basic" +
                    "computer and programming concepts such as hardware, software, numbering systems, identifiers," +
                    "variables, constants, data types, and operations, standard input and output, selections, loops, " +
                    "functions and methods, single and multidimensional arrays, and objects and classes.",

            "An intermediate programming course for the Computer Science major. Topics include class" +
                    "classes and interfaces, graphical user interface, event-driven programming, binary I/0, and recursion.",

            "An extension of programming methodology to cover data storage and manipulation on complex " +
                    "data sets. Topics include: programming and applications of data structures; stacks, queues, lists, " +
                    "binary trees, heaps, priority queues, balanced trees and graphs. Recursive programming is heavily " +
                    "utilized. Fundamental sorting and searching algorithms are examined along with informal efficiency " +
                    "comparisons. Students expected to be proficient with a professional IDE for coding and debugging.",

            "This course covers fundamentals of computer architecture and organization. Topics include " +
                    "classical von Neumann machine, major functional units, primary memory, representations of numerical " +
                    "(integer and floating point) and non-numerical data, CPU architecture, instruction encoding, fetch, " +
                    "decode, and execute cycle, instruction formats, addressing modes, symbolic assembler, assembly language " +
                    "programming, handling of subprogram calls at assembly level, mapping between high-level language " +
                    "patterns and assembly/machine language, interrupts and I/0 operations, virtual memory management, and " +
                    "data access from a magnetic disk. A number of other programming topics such as C programming language " +
                    "constructs (control and data structures, pointers, arrays and functions) and their relationship to the " +
                    "underlying architecture are introduced.",

            "Development of the basic concepts and techniques learned in CSE148 and CSE218 into" +
                    "practical programming skills that include a systematic approach to program design, coding, testing," +
                    "and debugging. Application of these skills to the construction of robust programs of 1000 to 2000" +
                    "lines of source code. Use of programming environments and tools to aid in the software development" +
                    "process."
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeChoiceBoxes();
        initializeStudentInfo();
        initializeCourseTable();
    }

    private void initializeChoiceBoxes() {
        studentInfoProfCb.setItems(studentList);
        studentInfoProfCb.setValue("Choose a Professor");
    }

    private void initializeStudentInfo() {
        studentInfoIdTf.setText(currentStudent.getId());
        studentInfoNameTf.setText(currentStudent.getFirstName() + " " + currentStudent.getLastName());
    }

    private void initializeCourseTable() {
        coursesListView.getItems().add(currentStudent.getCourseArr()[0]);
        if(currentStudent.getCourseArr().length > 1)
            for(int i = 1; i < currentStudent.getCourseArr().length; i++)
                coursesListView.getItems().add(currentStudent.getCourseArr()[i]);

        coursesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                courseDescLabel.setText("Course Description of " + newValue);
                studentInfoTa.setText(getCourseDescription(newValue));
            }
        });
    }

    private String getCourseDescription(String course) {
        int courseIndex = switch (course) {
            case "CSE110" -> 0;
            case "CSE118" -> 1;
            case "CSE148" -> 2;
            case "CSE218" -> 3;
            case "CSE222" -> 4;
            case "CSE248" -> 5;
            default -> -1;
        };
        return (courseIndex >= 0 && courseIndex < courseDescriptions.length) ? courseDescriptions[courseIndex] : "";
    }

    @FXML
    public void backBtnOnAction(ActionEvent event) throws IOException {
        GoToNewWindow.goToNewWindow(event, "/Views/MainView.fxml", "Tutor Service");
    }

    @FXML
    public void enterBtnOnAction(ActionEvent event) throws IOException {
        String instructor = studentInfoProfCb.getValue();
        String selectedCourse = coursesListView.getSelectionModel().getSelectedItem();
        String topic = studentInfoTopicTf.getText();
        if (isInputValid(instructor, selectedCourse, topic)) {
            handleValidInput();
            GoToNewWindow.goToNewWindow(event, "/Views/MainView.fxml", "Tutor Information");
        } else {
            invalidInputAlert();
        }
    }

    public void invalidInputAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText("Please choose a professor, course, topic.");
        alert.showAndWait();
    }

    private boolean isInputValid(String instructor, String selectedCourse, String topic) {
        return !("Choose a Professor".equals(instructor) || selectedCourse == null || topic.isEmpty());
    }

    private String amountOfStudents() {
        String str;
        if(studentLoggedInQueue.size() == 1)
            str = "You are first in Line!";
        else if(studentLoggedInQueue.size() == 2)
            str = "There is " + (studentLoggedInQueue.size() - 1) + " student in front of you.";
        else
            str = "There are " + (studentLoggedInQueue.size() - 1) + " students in front of you.";
        return str;
    }

    private void handleValidInput() {
        StudentLoggedIn loggedInStudent = new StudentLoggedIn(currentStudent,
                coursesListView.getSelectionModel().getSelectedItem(), studentInfoTopicTf.getText(),
            studentInfoProfCb.getValue(), true);
        studentLoggedInQueue.addStudentToQueue(loggedInStudent, currentStudent);
        loggedInStudent.setLoggedIn(true);
        StudentLoggedIn studentLoggedIn = studentLoggedInQueue.getStudentLoggedIn(currentStudent).orElse(null);
        if(studentLoggedIn != null) {
            studentLoggedIn.startTime();
            studentHistory.addStudentToHistory(new StudentLoggedIn(studentLoggedIn.getStudent(),
                studentLoggedIn.getCourses(), studentLoggedIn.getTopic(), studentLoggedIn.getInstructor(), true,
                    studentLoggedIn.getDate(), studentLoggedIn.getTimeIn(), studentLoggedIn.getTimeOut()));
        }
        loginSuccsesful();
    }

    public void loginSuccsesful() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Information");
        successAlert.setHeaderText("You've logged in successfully");
        successAlert.setContentText(amountOfStudents() + "\nHave a good tutor session! :)");
        successAlert.showAndWait();
    }
}