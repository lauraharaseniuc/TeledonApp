module com.example.clientfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.prevclient to javafx.fxml;
    exports com.example.prevclient;
}