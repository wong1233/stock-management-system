package com.wong.stockmanagement;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.List;

/**
 * Collects the user's full name before the inventory dashboard is opened.
 */
public class UserInfoDialog extends Dialog<UserInfo> {
    private static final ButtonType CONTINUE_BUTTON_TYPE =
            new ButtonType("Continue", ButtonBar.ButtonData.OK_DONE);

    private final TextField fullNameField = new TextField();
    private final Label userIdPreview = new Label("User ID: guest");
    private UserInfo userInfo;

    public UserInfoDialog() {
        setTitle("Welcome");
        setHeaderText("Welcome to the Stock Management System");
        setResizable(false);

        getDialogPane().getButtonTypes().addAll(
                List.of(CONTINUE_BUTTON_TYPE, ButtonType.CANCEL)
        );
        getDialogPane().setContent(createContent());
        getDialogPane().setPrefWidth(480);

        fullNameField.textProperty().addListener(
                (observable, previousName, currentName) -> updateUserIdPreview(currentName)
        );

        Node continueButton = getDialogPane().lookupButton(CONTINUE_BUTTON_TYPE);
        continueButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                userInfo = readUserInfo();
            } catch (InventoryException exception) {
                userInfo = null;
                showValidationError(exception.getMessage());
                event.consume();
            }
        });

        setResultConverter(buttonType ->
                buttonType == CONTINUE_BUTTON_TYPE ? userInfo : null
        );
    }

    private VBox createContent() {
        Label instruction = new Label(
                "Enter your first name and surname to begin managing inventory."
        );
        instruction.setWrapText(true);
        instruction.setStyle("-fx-text-fill: #475569;");

        Label fullNameLabel = new Label("Full Name");
        fullNameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155;");

        fullNameField.setPromptText("e.g. Wong Ting Kai");

        userIdPreview.setStyle(
                "-fx-background-color: #e2e8f0; -fx-background-radius: 6px; "
                        + "-fx-padding: 9px 12px; -fx-font-weight: bold; -fx-text-fill: #334155;"
        );
        userIdPreview.setMaxWidth(Double.MAX_VALUE);

        VBox content = new VBox(
                10,
                instruction,
                fullNameLabel,
                fullNameField,
                userIdPreview
        );
        content.setPadding(new Insets(8));
        return content;
    }

    private UserInfo readUserInfo() {
        String fullName = fullNameField.getText() == null
                ? ""
                : fullNameField.getText().trim();

        if (fullName.isEmpty()) {
            throw new InventoryException("Full name cannot be empty.");
        }

        UserInfo candidate = new UserInfo(fullName);
        if (!candidate.isValidName()) {
            throw new InventoryException(
                    "Please enter at least a first name and surname."
            );
        }

        return candidate;
    }

    private void updateUserIdPreview(String fullName) {
        UserInfo preview = new UserInfo(fullName == null ? "" : fullName.trim());
        userIdPreview.setText("User ID: " + preview.getUserID());
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid User Information");
        alert.setHeaderText("Please correct your name");
        alert.setContentText(message);

        Window owner = getDialogPane().getScene() == null
                ? null
                : getDialogPane().getScene().getWindow();
        if (owner != null) {
            alert.initOwner(owner);
        }

        alert.showAndWait();
    }
}
