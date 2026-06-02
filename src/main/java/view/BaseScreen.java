package view;

import Services.AuthService;
import Services.DB;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.layout.HBox;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BaseScreen implements ScreenInterface {

    // initializing the instance variables
    private final GridPane grid = new GridPane();
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label statusLabel = new Label("Please enter your username and password");

    private final Button loginButton = new Button("Login");
    private final Button registerButton = new Button("Register");
    private final Button adminLoginButton = new Button("Admin Login");

    //constructor
    public BaseScreen(){
        setupGrid();
        setupButtons();
    }

    //sets up the main grid for this screen
    private void setupGrid(){
        Insets gridPadding = new Insets(10, 10, 10, 10);
        grid.setPadding(gridPadding);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Label header = new Label("Welcome to Room Booking System");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        Label title = new Label("Sign in to User Account");
        title.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 20));
        Label usernameLabel = new Label("Username: ");
        Label passwordLabel = new Label("Password: ");

        HBox usernameBox = new HBox(22, usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.CENTER);
        usernameField.setPrefWidth(200);
        HBox passwordBox = new HBox(25, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.CENTER);

        passwordField.setPrefWidth(200);
        grid.add(header, 0, 0, 2, 1);
        GridPane.setHalignment(header, HPos.CENTER);
        grid.add(title, 0, 2, 2, 1);
        GridPane.setHalignment(title, HPos.CENTER);
        grid.add(usernameBox, 0, 6,2,1);
        GridPane.setHalignment(usernameBox, HPos.CENTER);
        grid.add(passwordBox, 0, 7,2,1);
        GridPane.setHalignment(passwordBox, HPos.CENTER);
        grid.add(statusLabel, 0, 8,2,1);
        GridPane.setHalignment(statusLabel, HPos.CENTER);
        grid.add(loginButton, 0, 12, 2, 1);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        grid.add(registerButton, 0, 13);
        GridPane.setHalignment(registerButton, HPos.LEFT);
        grid.add(adminLoginButton, 1, 13);
        GridPane.setHalignment(adminLoginButton, HPos.RIGHT);
    }

    // sets up all the buttons of this screen and adds event handlers for them
    private void setupButtons(){
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchUserRegisterScreen();
            }
        });

        adminLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchAdminLoginScreen();
            }
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AuthService authService = new AuthService(DB.path("UserData"));
                String username = usernameField.getText();
                boolean login = authService.login(username,passwordField.getText());
                if (login) {
                    ScreenController.launchUserDashboardScreen(username);
                }
                else{
                    statusLabel.setText("Invalid Username or Password");
                    statusLabel.setStyle("-fx-text-fill: red");
                }

            }
        });
    }

    // returns the grid view of this screen
    public GridPane getView(){
        return grid;
    }

    // helps clear all elements of this screen when switching to another screen
    public void dispose(){
        loginButton.setOnAction(null);
        registerButton.setOnAction(null);
        adminLoginButton.setOnAction(null);

        usernameField.clear();
        passwordField.clear();
        statusLabel.setText("Please enter your username and password");
        statusLabel.setStyle("");

        grid.getChildren().clear();
    }

}
