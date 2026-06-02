package view;

import entity.Room;
import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.time.LocalDate;

public class ScreenController extends Application {
    private static ScreenInterface currentScreen;
    public static StackPane rootPane = new StackPane();

    //this is where the application starts and the main scene is built
    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Room Booking System");
        primaryStage.setScene(new javafx.scene.Scene(rootPane, 800, 450));
        primaryStage.setMaximized(true); //ensures full screen on startup
        primaryStage.show();
        currentScreen = new BaseScreen();
        rootPane.getChildren().add(currentScreen.getView());
    }

    //below methods launch the respective screens when called
    protected static void launchUserRegisterScreen(){
        cleanUp();
        currentScreen = new UserRegisterScreen();
        rootPane.getChildren().setAll(currentScreen.getView());
    }

    protected static void launchUserDashboardScreen(String username){
        cleanUp();
        currentScreen = new UserDashboard(username);
        rootPane.getChildren().setAll(currentScreen.getView());
    }

    protected static void launchBaseScreen(){
        cleanUp();
        currentScreen = new BaseScreen();
        rootPane.getChildren().setAll(currentScreen.getView());
    }

    protected static void launchAdminLoginScreen(){
        cleanUp();
        currentScreen = new AdminLoginScreen();
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchAdminRegisterScreen(){
        cleanUp();
        currentScreen = new AdminRegisterScreen();
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchEditRoomsScreen(){
        cleanUp();
        currentScreen = new EditRoomsScreen();
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchAddRoomScreen(){
        cleanUp();
        currentScreen = new AddRoomScreen();
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchEditARoomScreen(){
        cleanUp();
        currentScreen = new EditARoomScreen();
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchRemoveRoomScreen(){
        cleanUp();
        currentScreen = new RemoveRoomScreen();
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchRequestHistoryScreen(){
        cleanUp();
        currentScreen = new RequestHistoryScreen();
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchAdminDashboardScreen(){
        cleanUp();
        currentScreen = new AdminDashboard();
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchConfirmRequestScreen(String username, Room room, LocalDate date, String timeSlot, String reason){
        cleanUp();
        currentScreen = new ConfirmRequestScreen(username, room, date, timeSlot, reason);
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchActiveRequestsScreen(){
        cleanUp();
        currentScreen = new ActiveRequestsScreen();
        rootPane.getChildren().add(currentScreen.getView());
    }

    protected static void launchUserHistoryScreen(String username){
        cleanUp();
        currentScreen = new UserHistory(username);
        rootPane.getChildren().add(currentScreen.getView());
    }

    //this ensures that when switching a screen the previous screen is completely cleaned to avoud any overlapping
    protected static void cleanUp(){
        if (currentScreen != null){
            currentScreen.dispose();
        }
        rootPane.getChildren().clear();
    }

}
