package Util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public final class GoToNewWindow {
    private GoToNewWindow() {}
    public static void goToNewWindow(ActionEvent event, String filePath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GoToNewWindow.class.getResource(filePath));
        Stage stage = new Stage();
        Scene newScene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle(title);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        stage.setScene(newScene);
        stage.show();
    }
}
