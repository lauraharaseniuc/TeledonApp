package com.example.clientfxgood;


import com.example.services.IService;
import com.example.services.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Volunteer;

import java.io.IOException;
import java.util.Optional;

public class LogInController {
    private IService server;
    private VolunteerSessionController sessionController ;
    private Parent mainViewParent;

    @FXML
    private TextField usernameTF;
    @FXML
    private PasswordField passwordTF;
    @FXML
    private Label errorLabel;

    public void initialize(IService service) {
        this.server =service;
    }

    public IService getServer() {
        return this.server;
    }

    public void setSessionController(VolunteerSessionController sessionController) {
        this.sessionController = sessionController;
    }

    public void setParent (Parent parent) {
        this.mainViewParent = parent;
    }

    private void openNewSession(Volunteer volunteerLoggedIn) {
        //FXMLLoader loader = new FXMLLoader(StartObjectClientFX.class.getResource("volunteer-logged-in-view.fxml"));
        //Parent root = loader.load();
        //VolunteerSessionController volunteerSessionController = loader.getController();
        this.sessionController.initialize(this.getServer(), volunteerLoggedIn);
        //volunteerSessionController.initialize(getServer(), volunteerLoggedIn);
        //Scene logged_in_scene = new Scene(root);
        //logged_in_scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("css_styles/session_styles.css")).toExternalForm());
        //Stage main_stage=(Stage)this.logInButton.getScene().getWindow();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(this.mainViewParent));
        //newStage.setScene(logged_in_scene);

        newStage.show();

    }

    public void onLogInPressed(ActionEvent actionEvent) throws ServiceException {
        this.errorLabel.setText("");
        String username= this.usernameTF.getText();
        String password = this.passwordTF.getText();
        if (username == null) {
            this.errorLabel.setText("Provide a username!");
            this.errorLabel.setTextFill(Color.RED);
            return;
        }
        if (password == null) {
            this.errorLabel.setText("Provide a password!");
            this.errorLabel.setTextFill(Color.RED);
            return;
        }
        Optional< Volunteer> volunteerLoggedIn = this.server.volunteerLogIn(username, password, sessionController);
        if (volunteerLoggedIn.isEmpty()) {
            this.errorLabel.setText("Invalid combination of username and password!");
            this.errorLabel.setTextFill(Color.RED);
            return;
        }
        else {
            this.openNewSession(volunteerLoggedIn.get());
        }
    }
}
