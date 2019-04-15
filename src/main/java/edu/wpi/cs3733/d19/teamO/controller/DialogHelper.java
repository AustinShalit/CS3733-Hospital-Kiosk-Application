package edu.wpi.cs3733.d19.teamO.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.jfoenix.controls.JFXComboBox;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.util.Callback;

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
  public static void populateComboBox(Database database,
                                      JFXComboBox<Node> comboBox) {
    populateComboBox(database, comboBox, Arrays.asList(Node.NodeType.values()));
  }

  /**
   * Populate a combobox of nodes.
   */
  public static void populateComboBox(Database database,
                                      JFXComboBox<Node> comboBox,
                                      Collection<Node.NodeType> nodeTypesToInclude) {
    List<Node> nodesToInclude = new ArrayList<>();
    for (Node curNode : database.getAllNodesByLongName()) {
      if (nodeTypesToInclude.contains(curNode.getNodeType())) {
        nodesToInclude.add(curNode);
      }
    }

    comboBox.getItems().setAll(nodesToInclude);
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
   * Show an error alert, and wait for user to close it.
   *
   * @return true if the user presses ok, false otherwise
   */
  public static boolean showConfirmDialog(String titleText, String headerText, String contentText) {
    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
    dialog.setTitle(titleText);
    dialog.setHeaderText(headerText);
    dialog.setContentText(contentText);

    Optional<ButtonType> result = dialog.showAndWait();
    return result.get() == ButtonType.OK;

  }
}

