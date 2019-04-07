package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.util.Optional;

import com.jfoenix.controls.JFXComboBox;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.util.Callback;
import javafx.util.Duration;

import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public final class DialogHelper {

  private DialogHelper() {
    throw new UnsupportedOperationException("Utility class!");
  }

  /**
   * Show an information alert, and wait for user to close it.
   */
  public static void showInformationAlert(String titleText, String contentText) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(titleText);
    alert.setHeaderText(null);
    alert.setContentText(contentText);

    alert.showAndWait();
  }

  /**
   * Show an error alert, and wait for user to close it.
   */
  public static void showErrorAlert(String titleText, String headerText) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(titleText);
    alert.setHeaderText(headerText);
    alert.setContentText(null);

    alert.showAndWait();
  }

  /**
   * Show a dialog that asks a user a prompt. Reurns the user input as a String, or an empty
   * string if no input.
   */
  public static String textInputDialog(String titleText, String headerText, String promptText) {
    TextInputDialog dialog = new TextInputDialog(null);
    dialog.setTitle(titleText);
    dialog.setHeaderText(headerText);
    dialog.setContentText(promptText);

    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      return result.get();
    }
    return "";
  }

  /**
   * Populate a combobox of nodes.
   */
  public static void populateComboBox(
      Database database, JFXComboBox<Node> comboBox) {
    comboBox.getItems().addAll(database.getAllNodes());
    Callback<ListView<Node>, ListCell<Node>> cellFactory =
        new Callback<ListView<Node>, ListCell<Node>>() {
          @Override
          public ListCell<Node> call(ListView<Node> param) {
            return new ListCell<Node>() {

              @Override
              protected void updateItem(Node item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                  setGraphic(null);
                } else {
                  setText(item.getLongName());
                }
              }
            };
          }
        };
    comboBox.setCellFactory(cellFactory);

    // wait for selection
    comboBox.valueProperty().addListener((observable, oldValue, newValue)
        -> comboBox.setButtonCell(cellFactory.call(null)));
  }

  /**
   * The method bounces the label to the left and right rapidly.
   */
  public static void bounceTextAnimation(Label label) {
    KeyFrame init = new KeyFrame(Duration.ZERO, new KeyValue(label.translateXProperty(),
        0));

    KeyFrame left = new KeyFrame(Duration.seconds(.1), new KeyValue(label.translateXProperty(),
        -10));
    KeyFrame right = new KeyFrame(Duration.seconds(.1), new KeyValue(label.translateXProperty(),
        10));

    Timeline timelineLeft = new Timeline(init, left);
    timelineLeft.setCycleCount(2);

    Timeline timelineRight = new Timeline(init, right);
    timelineRight.setCycleCount(2);

    for (int i = 2; i < 12; i++) {
      if (i % 2 == 0) {
        timelineLeft.play();
      } else if (i % 3 == 0) {
        timelineRight.play();
      }
    }
  }
}

