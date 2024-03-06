package com.example.clientfxgood;

import com.example.services.IService;
import com.example.services.ITeledonObserver;
import com.example.services.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mappers.CaseToCaseDTOMapper;
import model.Case;
import model.Volunteer;
import model.Donation;
import model.Donor;
import model.CaseDTO;

import java.util.ArrayList;
import java.util.List;

public class VolunteerSessionController implements ITeledonObserver {
    private IService server;
    private Volunteer volunteerLoggedIn;
    private CaseToCaseDTOMapper caseToCaseDTOMapper = new CaseToCaseDTOMapper();

    @FXML
    private TableView<CaseDTO> casesTable;
    @FXML
    private TableColumn<CaseDTO, String> formattedColumn;
    @FXML
    private TextField donorNameTF;
    @FXML
    private TextField donorAddressTF;
    @FXML
    private TextField donorPhoneTF;
    @FXML
    private TextField amountDonatedTF;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField searchDonorTF;
    @FXML
    private Pane leftTablePane;
    @FXML
    private Label volunteerLabel;
    private TableView<Donor> donorTableView;

    public void setServer (IService server) {
        this.server = server;
    }
    public void initialize(IService service, Volunteer volunteerLoggedIn) {
        this.server =service;
        this.volunteerLoggedIn=volunteerLoggedIn;
        this.volunteerLabel.setText(this.volunteerLabel.getText()+" "+volunteerLoggedIn.getName());
        this.volunteerLabel.setTextFill(Color.GREEN);
        this.initializeCaseTableView();
    }

    public void initializeCaseTableView() {
        try {
            List<Case> cases = (List<Case>) this.server.getAllCases();
            List<Donation> donations = (List<Donation>) this.server.getAllDonations();
            List<CaseDTO> caseDTOList = this.caseToCaseDTOMapper.convert(cases, donations);
            casesTable.getItems().setAll(caseDTOList);
        } catch (ServiceException ex) {
            this.errorLabel.setText(ex.getMessage());
        }
    }

    private void freeLeftTablePaneFromDonorsTable() {
        try {
            this.leftTablePane.getChildren().remove(this.donorTableView);
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
    }

    private void setUpLeftTablePaneForCaseVisualisation() {
        try {
            this.leftTablePane.getChildren().add(this.casesTable);
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
    }

    private void onSelectDonor() {
        Donor selectedDonor = this.donorTableView.getSelectionModel().getSelectedItem();
        this.donorNameTF.setText(selectedDonor.getName());
        this.donorAddressTF.setText(selectedDonor.getAddress());
        this.donorPhoneTF.setText(selectedDonor.getPhoneNumber());
        this.freeLeftTablePaneFromDonorsTable();
        this.setUpLeftTablePaneForCaseVisualisation();
    }

    @FXML
    public void initialize() {
        this.donorTableView = new TableView<>();
        TableColumn<Donor, String> nameColumn = new TableColumn<>();
        TableColumn<Donor, String> addressColumn =  new TableColumn<>();
        TableColumn<Donor, String> phoneColumn =  new TableColumn<>();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        this.donorTableView.getColumns().add(nameColumn);
        this.donorTableView.getColumns().add(addressColumn);
        this.donorTableView.getColumns().add(phoneColumn);
        this.donorTableView.setMinHeight(426);
        this.donorTableView.setMinWidth(427);
        nameColumn.setMinWidth(this.donorTableView.getMinWidth()/3);
        addressColumn.setMinWidth(this.donorTableView.getMinWidth()/3);
        phoneColumn.setMinWidth(this.donorTableView.getMinWidth()/3);
        this.donorTableView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, handler -> {
            this.onSelectDonor();
        });
        this.formattedColumn.setCellValueFactory(new PropertyValueFactory<>("formatted"));
    }

    public void onRegisterDonation(ActionEvent actionEvent) {
        this.errorLabel.setText("");
        String donorName = this.donorNameTF.getText();
        String donorAddress = this.donorAddressTF.getText();
        String donorPhone = this.donorPhoneTF.getText();
        String amountDonated = this.amountDonatedTF.getText();
        if (donorName==null) {
            this.errorLabel.setText("Provide a name first!");
            this.errorLabel.setTextFill(Color.RED);
            return;
        }
        if (donorAddress==null) {
            this.errorLabel.setText("Provide an address first!");
            this.errorLabel.setTextFill(Color.RED);
            return;
        }
        if (donorPhone==null) {
            this.errorLabel.setText("Provide a phone number first!");
            this.errorLabel.setTextFill(Color.RED);
            return;
        }
        if (amountDonated==null) {
            this.errorLabel.setText("Enter the amount donated!");
            this.errorLabel.setTextFill(Color.RED);
            return;
        }
        CaseDTO selectedCase = this.casesTable.getSelectionModel().getSelectedItem();
        if (selectedCase==null) {
            this.errorLabel.setText("Select a case first!");
            this.errorLabel.setTextFill(Color.RED);
            return;
        }
        double amountDonatedAsDouble = Double.parseDouble(amountDonated);
        try {
            this.server.makeDonation(selectedCase, donorName, donorAddress, donorPhone, amountDonatedAsDouble);
        } catch (ServiceException ex) {
            this.errorLabel.setText(ex.getMessage());
        }
    }

    private void freeLeftTablePane() {
        try {
            this.leftTablePane.getChildren().remove(this.casesTable);
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
    }

    private void setUpLeftTablePaneForDonorSearch() {
        try {
            this.leftTablePane.getChildren().add(this.donorTableView);
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
    }
    public void onSearchDonor(ActionEvent actionEvent) {
        this.freeLeftTablePane();
        this.setUpLeftTablePaneForDonorSearch();
        this.errorLabel.setText("");
        String donorName=this.searchDonorTF.getText();
        if (donorName==null) {
            this.errorLabel.setText("Introduceti numele donatorului cautat!");
            this.errorLabel.setTextFill(Color.RED);
            return;
        }
        List<Donor> donors = new ArrayList<>();
        try {
            donors= (List<Donor>) this.server.getAllDonors();
        } catch (ServiceException ex) {
           this.errorLabel.setText(ex.getMessage());
        }
        List<Donor> filteredDonors = new ArrayList<>();
        donors.forEach((donor) -> {
            if (donor.getName().toLowerCase().contains(donorName.toLowerCase())) {
                filteredDonors.add(donor);
            }
        });
        this.donorTableView.getItems().setAll(filteredDonors);
    }

    public void onLogOut(ActionEvent actionEvent) {
        Stage stage= (Stage) this.leftTablePane.getScene().getWindow();
        stage.close();
    }

    public void donationReceived(CaseDTO selectedCase, double amountDonated) {
        //System.out.println(selectedCase.getDescription());
        int caseIndex = this.casesTable.getItems().indexOf(selectedCase);
        selectedCase.addToRaisedSum(amountDonated);
        String newFormat = selectedCase.formatCaseDTO();
        selectedCase.setFormatted(newFormat);
        System.out.println(newFormat);

        this.casesTable.getItems().set(caseIndex,selectedCase);
    }
}
