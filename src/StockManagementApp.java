
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StockManagementApp extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("Stock Management System");
        Label message = new Label("JavaFX Version 2.0");

        Button testButton = new Button("Test JavaFX");
        testButton.setOnAction(event ->
                message.setText("JavaFX is working!")
        );

        VBox root = new VBox(15, title, message, testButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Stock Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}