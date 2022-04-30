module sise {
    requires javafx.controls;
    requires javafx.fxml;

    opens frontend to javafx.fxml;
    opens backend to javafx.fxml;
    exports frontend;
    exports backend;
    exports backend.layer;
    exports backend.dao;
}