package App;

import Model.*;
import Util.BackUp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Views/mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Tutor Service");
        stage.setScene(scene);
        stage.show();

        StudentHistory studentHistory = DataCenter.getInstance().getStudentLoggedInHistoryLinkedList();
        StudentLoggedInQueue studentLoggedInQueue = DataCenter.getInstance().getStudentLoggedInQueue();
        stage.setOnCloseRequest(e -> {
            try{
                if(!studentLoggedInQueue.isEmpty()) {
                    System.out.println("All students logged out.");
                    studentLoggedInQueue.logOutAllStudents(studentHistory);
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            BackUp.saveData();
        });
    }

    public static void main(String[] args) {
        BackUp.loadData("Data/students.csv");
        launch();
    }
}