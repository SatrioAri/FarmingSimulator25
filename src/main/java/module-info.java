module com.farmingcmulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.farmingcmulator to javafx.fxml;
    opens com.farmingcmulator.controller to javafx.fxml;
    opens com.farmingcmulator.model to javafx.fxml;

    exports com.farmingcmulator;
    exports com.farmingcmulator.controller;
    exports com.farmingcmulator.model;
    exports com.farmingcmulator.util;
}
