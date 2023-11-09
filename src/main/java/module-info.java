module cse218.project.project1riccaj {
    requires javafx.controls;
    requires javafx.fxml;

    opens Model;
    exports Model;
    opens App to javafx.fxml;
    exports App;
    exports Controller;
    opens Controller to javafx.fxml;
}