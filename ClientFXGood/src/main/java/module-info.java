module com.example.controllers {
    requires javafx.controls;
    requires javafx.fxml;
    requires LAB6.Services.main;
    requires LAB6.Networking.main;
    requires LAB6.Model.main;


    exports com.example.clientfxgood;
    opens com.example.clientfxgood to javafx.fxml;
}