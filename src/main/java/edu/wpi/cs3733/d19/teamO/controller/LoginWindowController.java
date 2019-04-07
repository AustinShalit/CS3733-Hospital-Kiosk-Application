package edu.wpi.cs3733.d19.teamO.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginWindowController extends Controller {

  @FXML
  private JFXButton loginButton;
  @FXML
  private JFXTextField username;
  @FXML
  private JFXPasswordField password;
  @FXML
  private Label loginFail;

  @FXML
  void initialize() {

  }

  @FXML
  void loginButtonAction() {
    // gets the user input
    /*
    Login login = parseUserLogin();
    if (login == null) {
      Logger logger = Logger.getLogger(SanitationWindowController.class.getName());
      logger.log(Level.WARNING,
          "Unable to parse Username and Password.",
          "Unable to parse User Login info");
      return;
    }

    Set<Login> info = db.getAllLogin();
    boolean check = false;

    // checks every Login info in set
    for (Login l : info) {
      if (l.equals(login)) {
        check = true;
      }
    }


    // TODO: User privilege check

    // if info typed was right, you go to main window screen
    if (check) {
      switchScenes("MainWindow.fxml", loginButton.getScene().getWindow());
    } else {
      loginFail.setText("Incorrect username or password");
      bounceTextAnimation(loginFail);
    }

     */
  }
}
