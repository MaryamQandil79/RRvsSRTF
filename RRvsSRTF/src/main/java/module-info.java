module com.scheduler {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.scheduler            to javafx.fxml;
    opens com.scheduler.controller to javafx.fxml;
    opens com.scheduler.model      to javafx.base;

    exports com.scheduler;
    exports com.scheduler.model;
    exports com.scheduler.scheduler;
    exports com.scheduler.controller;
    exports com.scheduler.util;
}
