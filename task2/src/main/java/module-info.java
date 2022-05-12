module sise {
    requires javafx.controls;
    requires javafx.fxml;
    requires jfreechart;
    requires java.datatransfer;
    requires java.desktop;

    opens frontend to javafx.fxml;
    opens backend to javafx.fxml;
    exports frontend;
    exports backend;
    exports backend.layer;
    exports backend.dao;
}