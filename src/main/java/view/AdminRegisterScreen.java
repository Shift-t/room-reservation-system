package view;

import Services.UserRegisterService;
import javafx.scene.layout.GridPane;
import Services.AdminRegisterService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import Services.AdminUserValidator;
import entity.Admin;


public class AdminRegisterScreen implements ScreenInterface{

    private final GridPane grid = new GridPane();

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField confirmPasswordField = new PasswordField();

    private final Button backButton = new Button("Return to previous page");
    private final Button registerButton = new Button("Register");

    private final Label statusLabel = new Label("Please fill all the fields. Username should be greater than 1 and less than 20 characters.");

    public AdminRegisterScreen() {

        Insets gridPadding = new Insets(10,10,10,10);
        grid.setPadding(gridPadding);
        grid.setHgap(20);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);

        Label header = new Label("Welcome to Room Booking System");
        Label title = new Label("Add an Admin");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label confirmPasswordLabel = new Label("Confirm Password: ");

        grid.add(header, 0, 0, 2, 1);
        grid.add(title, 0, 1, 2, 1);
        grid.add(usernameLabel, 1, 3);
        grid.add(usernameField, 2, 3);
        grid.add(passwordLabel, 1, 5);
        grid.add(passwordField, 2, 5);
        grid.add(confirmPasswordLabel, 1, 6);
        grid.add(confirmPasswordField, 2, 6);
        grid.add(backButton, 1, 10);
        grid.add(registerButton, 2, 10);
        grid.add(statusLabel, 1, 8);

        GridPane.setHalignment(header, HPos.CENTER);
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setHalignment(statusLabel, HPos.CENTER);

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScreenController.launchAdminDashboardScreen();
            }
        });

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AdminUserValidator check = new AdminUserValidator();
                // validation
                if (check.isValid(usernameField.getText()) && passwordField.getText().equals(confirmPasswordField.getText())) {
                    Admin admin = new Admin(usernameField.getText(), passwordField.getText());
                    try {
                        AdminRegisterService.registerAdmin(admin);
                        statusLabel.setText("Registration Successful");
                        statusLabel.setStyle("-fx-text-fill: green");
                        registerButton.setDisable(true);
                    } catch (IllegalArgumentException e) {
                        statusLabel.setText(e.getMessage());
                        statusLabel.setStyle("-fx-text-fill: red");
                    }
                }
                else{
                    statusLabel.setText("Invalid Entries Please Try Again");
                    statusLabel.setStyle("-fx-text-fill: red");
                }

            }
        });


    }

    @Override
    public GridPane getView(){
        return grid;
    }

    @Override
    public void dispose(){
        statusLabel.setText("");
        backButton.setOnAction(null);
        registerButton.setOnAction(null);

        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();

        grid.getChildren().clear();

    }

}
