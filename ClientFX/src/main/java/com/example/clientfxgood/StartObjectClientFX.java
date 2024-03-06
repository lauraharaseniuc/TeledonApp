package com.example.clientfxgood;

import com.example.networking.objectprotocol.ServicesObjectProxy;
import com.example.services.IService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartObjectClientFX extends Application {
    private Stage primaryStage;
    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    public StartObjectClientFX() {
    }

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();

        try {
            clientProps.load(StartObjectClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException var13) {
            System.err.println("Cannot find client.properties " + var13);
            return;
        }

        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException var12) {
            System.err.println("Wrong port number " + var12.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }

        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);
        IService server = new ServicesObjectProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("com/example/networking/clientfxgood/log-in-view.fxml"));
        Parent root = (Parent)loader.load();
        LogInController ctrl = (LogInController)loader.getController();
        ctrl.initialize(server);

        FXMLLoader cloader = new FXMLLoader(this.getClass().getClassLoader().getResource("com/example/networking/clientfxgood/volunteer-logged-in-view.fxml"));
        Parent croot = (Parent)cloader.load();
        VolunteerSessionController sessionController = (VolunteerSessionController) cloader.getController();
        sessionController.setServer(server);
        ctrl.setSessionController(sessionController);
        ctrl.setParent(croot);

        primaryStage.setTitle("MPP teledon");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}

