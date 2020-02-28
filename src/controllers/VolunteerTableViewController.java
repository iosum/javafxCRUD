package controllers;

import db.DBConnection;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Translate;
import models.Volunteer;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class VolunteerTableViewController implements Initializable {
    @FXML
    private Button editVolunteerButton;
    @FXML
    private Button deleteVolunteerButton;
    @FXML
    private Button updateVolunteerButton;
    @FXML
    private Button printVolunteerTableButton;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private DatePicker birthdayDatePicker;
    @FXML
    private TableView volunteerTable;
    @FXML
    private TableColumn firstNameColumn;
    @FXML
    private TableColumn lastNameColumn;
    @FXML
    private TableColumn phoneColumn;
    @FXML
    private TableColumn emailColumn;
    @FXML
    private TableColumn birthdayColumn;
    @FXML
    private TableColumn trainingColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadVolunteer();
            //editVolunteerButton.setDisable(true);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method will load volunteers from the mysql db and load them in the tableview object
     */

    public void loadVolunteer() throws SQLException {
        // 1. get the volunteer list
        ObservableList<Volunteer> volunteerObservableList = getVolunteersObservableList();

        // 2. configure the table column to connect to the volunteer list
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, String>("lastName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, String>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, String>("email"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, LocalDate>("birthday"));
        //trainingColumn.setCellValueFactory(new PropertyValueFactory<Volunteer, Boolean>("isTraining"));

        // 3.
        volunteerTable.setItems(volunteerObservableList);
    }

    //DBConnection dbConnection = new DBConnection();

    /**
     * This method will get the collection of volunteers list
     */
    public ObservableList<Volunteer> getVolunteersObservableList() throws SQLException {

        ObservableList<Volunteer> volunteerObservableList = FXCollections.observableArrayList();

        DBConnection dbConnection = new DBConnection();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        String sql = "SELECT * FROM volunteers";

        try {
            connection = dbConnection.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Volunteer volunteer = new Volunteer(resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"),
                        resultSet.getDate("birthday").toLocalDate()
                        /*, resultSet.getBoolean("isTraining")*/);
                volunteerObservableList.add(volunteer);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            dbConnection.close(connection, statement, resultSet);
        }


        return volunteerObservableList;
    }

    public void updateVolunteerButtonPushed(ActionEvent actionEvent) {
    }

    // -------------------edit a volunteer data----------------------

    public void editButtonPushed(ActionEvent event) {
        Volunteer volunteer = (Volunteer) volunteerTable.getSelectionModel().getSelectedItem();
        preloadVolunteerData(volunteer);
    }

    public void preloadVolunteerData(Volunteer volunteer) {
        firstNameTextField.setText(volunteer.getFirstName());
        lastNameTextField.setText(volunteer.getLastName());
        phoneNumberTextField.setText(volunteer.getPhoneNumber());
        emailTextField.setText(volunteer.getEmail());
        birthdayDatePicker.setValue(volunteer.getBirthday());
    }

    //------------------add a new volunteer--------------------

    public void insertButtonPushed(ActionEvent event) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbConnection.getConnection();
            String sql = "INSERT INTO volunteers (firstName, lastName, phoneNumber, email, " +
                    "birthday) " +
                    "VALUES (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            Date dbDate = Date.valueOf(birthdayDatePicker.getValue());

            preparedStatement.setString(1, firstNameTextField.getText());
            preparedStatement.setString(2, lastNameTextField.getText());
            preparedStatement.setString(3, phoneNumberTextField.getText());
            preparedStatement.setString(4, emailTextField.getText());
            preparedStatement.setDate(5, dbDate);
            //preparedStatement.setBoolean(6, true);

            preparedStatement.executeUpdate();

            loadVolunteer();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            dbConnection.close(connection, preparedStatement);
        }
    }

    //------print---------------------
    private void print(Node node) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT,
                Printer.MarginType.DEFAULT);
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job.showPrintDialog(node.getScene().getWindow())) {
            double pagePrintableWidth = pageLayout.getPrintableWidth();
            double pagePrintableHeight = pageLayout.getPrintableHeight();

//            volunteerTable.prefHeightProperty().bind(Bindings.size(volunteerTable.getItems()).multiply(1));
//            volunteerTable.minHeightProperty().bind(volunteerTable.prefHeightProperty());
//            volunteerTable.maxHeightProperty().bind(volunteerTable.prefHeightProperty());

            double scaleX = pagePrintableWidth / node.getBoundsInParent().getWidth();
            //scaling the height using the same scale as the width.
            // This allows the writing and the images to maintain their scale, or not look skewed.
            double scaleY = scaleX;
            double localScale = scaleX;
            //used to figure out the number of pages that will be printed.
            double numberOfPages = Math.ceil((volunteerTable.getPrefHeight() * localScale) / pagePrintableHeight);

            Translate gridTransform = new Translate();
            node.getTransforms().add(gridTransform);

            //now we loop though the image that needs to be printed and we only print a subimage of the full image.
            //for example: In the first loop we only pint the printable image from the top down to the height of a standard piece of paper.
            // Then we print starting from were the last printed page ended down to the height of the next page.
            // This happens until all of the pages are printed.
            // first page prints from 0 height to -11 inches height, Second page prints from -11 inches height to -22 inches height, etc.
            for (int i = 0; i < numberOfPages; i++) {
                gridTransform.setY(-i * (pagePrintableHeight / localScale));
                job.printPage(pageLayout, node);
            }

            //finally end the printing job.
            job.endJob();
        }
    }


    public void printVolunteerTableButtonPushed(ActionEvent actionEvent) {
                printVolunteerTableButton.setVisible(false);
                print(volunteerTable);
    }
}
