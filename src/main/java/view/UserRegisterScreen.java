package view;

import Services.UserRegisterService;
import entity.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.control.ComboBox;
import Services.UserNameValidator;


public class UserRegisterScreen implements ScreenInterface{

    //Declaring and initializing instance variables
    private final GridPane grid = new GridPane();

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField confirmPasswordField = new PasswordField();
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField phoneNumberField = new TextField();

    private final Button backButton = new Button("< Previous Page");
    private final Button registerButton = new Button("Register");

    private final ComboBox<String> categoryCBox = new ComboBox<>();
    private final Label statusLabel = new Label("Please fill all the fields. Username should be greater than 1 and less than 20 characters.");

    //constructor
    public UserRegisterScreen(){
        initializeComponents();
        setupButtons();
        setupGrid();
    }

    //sets up the grid for the current screen and adds all elements to it
    private void setupGrid(){
        Label header = new Label("Welcome to Room Booking System");
        Label title = new Label("Register as a new user");
        Label usernameLabel = new Label("Username: ");
        Label passwordLabel = new Label("Password: ");
        Label confirmPasswordLabel = new Label("Confirm Password: ");
        Label nameLabel = new Label("Name: ");
        Label emailLabel = new Label("Email: ");
        Label userCategoryLabel = new Label("Category: ");
        Label phoneNumberLabel = new Label("Phone Number: ");

        grid.add(header,0,0,6,1);
        GridPane.setHalignment(header, HPos.CENTER);
        grid.add(title, 0, 1,6,1);
        GridPane.setHalignment(title, HPos.CENTER);
        grid.add(usernameLabel, 0, 3,1,1);
        grid.add(usernameField, 1, 3,1,1);
        grid.add(passwordLabel, 0, 4,1,1);
        grid.add(passwordField, 1, 4,1,1);
        grid.add(confirmPasswordLabel, 2, 4,1,1);
        grid.add(confirmPasswordField, 3, 4,1,1);
        grid.add(nameLabel, 0, 5,1,1);
        grid.add(nameField, 1, 5,1,1);
        grid.add(userCategoryLabel, 2, 5,1,1);
        grid.add(categoryCBox, 3, 5,1,1);
        grid.add(emailLabel, 0, 6,1,1);
        grid.add(emailField, 1, 6,1,1);
        grid.add(phoneNumberLabel, 2, 6,1,1);
        grid.add(phoneNumberField, 3, 6,1,1);
        grid.add(statusLabel, 0, 8,6,2);
        statusLabel.setStyle("");
        GridPane.setHalignment(statusLabel, HPos.CENTER);
        statusLabel.setWrapText(true);
        grid.add(backButton, 0,10);
        GridPane.setHalignment(backButton, HPos.LEFT);
        grid.add(registerButton, 1, 10,3,1);
        GridPane.setHalignment(registerButton, HPos.RIGHT);
    }

    //initializes other components like the multi choice boxes and gird properties
    private void initializeComponents(){
        categoryCBox.getItems().addAll("Faculty","Student", "Staff");
        categoryCBox.setPromptText("Choose your Position");

        Insets gridPadding = new Insets(10,10,10,10);
        grid.setPadding(gridPadding);
        grid.setHgap(20);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);
    }

    //sets up buttons used in this screen and adds event handlers
    private void setupButtons(){
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                statusLabel.setText("");
                statusLabel.setStyle("");
                ScreenController.launchBaseScreen();
            }
        });

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserNameValidator check = new UserNameValidator();
                if (check.isValid(usernameField.getText()) && passwordField.getText().equals(confirmPasswordField.getText()) && categoryCBox.getValue() != null) {
                    User user = new User(usernameField.getText(), passwordField.getText(), nameField.getText(), emailField.getText(), phoneNumberField.getText(), categoryCBox.getValue());
                    try {
                        UserRegisterService.registerUser(user);
                        statusLabel.setText("Registration Successful. Please login.");
                        statusLabel.setStyle("-fx-text-fill: green");
                        registerButton.setDisable(true);
                        backButton.setText("< Return to login");
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

    //returns the grid for the current screen when called
    @Override
    public GridPane getView(){
        return grid;
    }

    //helps clear all the elements of the current screen when switching to another screen
    @Override
    public void dispose(){
        backButton.setOnAction(null);
        registerButton.setOnAction(null);

        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        nameField.clear();
        emailField.clear();
        phoneNumberField.clear();

        categoryCBox.getSelectionModel().clearSelection();

        statusLabel.setText("Please fill all the fields. Username should be greater than 1 and less than 20 characters.");
        statusLabel.setStyle("");

        grid.getChildren().clear();

    }
}
