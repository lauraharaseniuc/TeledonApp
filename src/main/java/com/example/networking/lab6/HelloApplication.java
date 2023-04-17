package com.example.networking.lab6;

import com.example.lab6.business.Service;
import com.example.lab6.controllers.LogInController;
import com.example.lab6.model.Case;
import com.example.lab6.model.Volunteer;
import com.example.lab6.repository.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties properties= new Properties();
        try {
            properties.load(new FileReader("bd.config"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log-in-view.fxml"));
        Parent root= fxmlLoader.load();
        LogInController logInController= fxmlLoader.getController();

        CaseRepository caseRepository = new CaseRepositoryDB(properties);
        DonationRepository donationRepository = new DonationRepositoryDB(properties);
        DonorRepository donorRepository = new DonorRepositoryDB(properties);
        VolunteerRepository volunteerRepository = new VolunteerRepositoryDB(properties);

        Service service = new Service(caseRepository, volunteerRepository, donationRepository, donorRepository);

        logInController.initialize(service);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}