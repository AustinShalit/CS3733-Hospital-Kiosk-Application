package edu.wpi.cs3733.d19.teamO;

import edu.wpi.cs3733.d19.teamO.controller.PreloaderController;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This will display the progress of various startup routines until the main window appears.
 */
public class ProjectPreloader extends Preloader {

    private Stage preloaderStage;
    private PreloaderController controller;

    @Override
    public void start(Stage stage) throws Exception {
        preloaderStage = stage;

        FXMLLoader loader
                = new FXMLLoader(PreloaderController.class.getResource("Preloader.fxml"));
        loader.load();
        controller = loader.getController();

        Scene scene = new Scene(loader.getRoot());

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof StateNotification) {
            StateNotification notification = (StateNotification) info;
            controller.setStateText(notification.getState());
            controller.setProgress(notification.getProgress());
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            preloaderStage.close();
        }
    }

    /**
     * A notification for the progress of a state in the preloader.
     */
    public static final class StateNotification implements PreloaderNotification {

        private final String state;
        private final double progress;

        /**
         * Creates a new state notification.
         *
         * @param state    the state
         * @param progress the progress of the state, in the range [0, 1]
         */
        public StateNotification(String state, double progress) {
            this.state = state;
            this.progress = progress;
        }

        /**
         * Gets the state.
         */
        public String getState() {
            return state;
        }

        /**
         * Gets the progress.
         */
        public double getProgress() {
            return progress;
        }

        @Override
        public String toString() {
            return String.format("StateNotification(state='%s', progress=%s)", state, progress);
        }
    }
}
