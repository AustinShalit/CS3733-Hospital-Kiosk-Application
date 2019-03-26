package edu.wpi.cs3733d18.onyx_owlmen.databasePrototype;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

class DatabaseController {
    /**
     * Change the scene to show the root node
     * @param stage The stage whose scene we want to modify
     * @param root The root node whose scene we want to show
     */
    void changeScene(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create a popup window showing the root node
     * @param stage The stage the popup window is created from
     * @param root Create the popup window from this node
     */
    void popupWindow(Stage stage, Parent root) {
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));

        newStage.initModality(Modality.APPLICATION_MODAL); // make it a popup
        newStage.initOwner(stage.getScene().getWindow()); // remind the stage who its initial owner was
        newStage.showAndWait();
    }
}
