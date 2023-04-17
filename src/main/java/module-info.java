module com.example.lab6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;


    opens com.example.networking.lab6 to javafx.fxml;
    exports com.example.networking.lab6;
    //exports com.example.clientfx.controllers to javafx.fxml;
    //opens com.example.clientfx.controllers to javafx.fxml;
    //opens model to javafx.base;
}