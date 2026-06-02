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


public class AdminLoginScreen implements ScreenInterface{
    static GridPane grid = new GridPane();

    Label header = new Label("Welcome to Room Booking Manager");
    Label title = new Label("Sign in to Admin Account");
    Label usernameLabel = new Label("Username: ");
    Label passwordLabel = new Label("Password: ");
    Label statusLabel = new Label("Please enter your username and password");

    TextField usernameField = new TextField();
    PasswordField passwordField = new PasswordField();

    Button loginButton = new Button("Login");
    Button returnButton = new Button("Return to user login");

    public AdminLoginScreen(){
        Insets gridPadding = new Insets(10, 10, 10, 10);
        grid.setPadding(gridPadding);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);


        HBox usernameBox = new HBox(22, usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.CENTER_LEFT);
        usernameField.setPrefWidth(200);

        HBox passwordBox = new HBox(25, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        passwordField.setPrefWidth(200);


        grid.add(header, 0, 0, 2, 1);
        header.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        GridPane.setHalignment(header, HPos.CENTER);

        grid.add(title, 0, 2, 2, 1);
        GridPane.setHalignment(title, HPos.CENTER);


        grid.add(usernameBox, 0, 4);

        grid.add(passwordBox, 0, 5);

        grid.add(statusLabel, 0, 6,2,1);
        GridPane.setHalignment(statusLabel, HPos.CENTER);
        statusLabel.setStyle("");

        grid.add(loginButton, 0, 10, 2, 1);
        GridPane.setHalignment(loginButton, HPos.CENTER);

        grid.add(returnButton, 0, 11, 2, 1);
        GridPane.setHalignment(returnButton, HPos.CENTER);



        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // check if password and username correct
                AuthService auth = new AuthService(DB.path("AdminData"));
                String username = usernameField.getText();
                String password = passwordField.getText();
                boolean login = auth.login(username, password);

                if (login) {
                    ScreenController.launchAdminDashboardScreen(); // MAKE ADMIN DASHBOARD
                    statusLabel.setText("");
                }
                else{
                    statusLabel.setText("Invalid Username or Password");
                    statusLabel.setStyle("-fx-text-fill: red");
                }

            }
        });

        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScreenController.launchBaseScreen();
            }
        });



    }

    @Override
    public GridPane getView(){
        return grid;
    }

    @Override
    public void dispose() {
        loginButton.setOnAction(null);
        returnButton.setOnAction(null);
        usernameField.clear();
        passwordField.clear();
        statusLabel.setText("Please enter your username and password");
        statusLabel.setStyle("");
        grid.getChildren().clear();
    }

}
