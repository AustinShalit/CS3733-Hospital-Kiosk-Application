package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.SanitationWindowController;
import edu.wpi.cs3733.d19.teamO.controller.exception.InvalidUserInputException;
import edu.wpi.cs3733.d19.teamO.entity.Login;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@FxmlController(url = "LoginWindow.fxml")
public class LoginController implements Controller {

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton loginButton;
  @FXML
  private JFXTextField username;
  @FXML
  private JFXPasswordField password;
  @FXML
  private Label loginFail;

  @Inject
  private EventBus eventBus;
  @Inject
  private HomeController.Factory homeControllerFactory;
  @Inject
  private Database db;

  @FXML
  void initialize() {
    // Test user login info
    Login user1 = new Login("admin", "wong");
    Login user2 = new Login("teamo", "won");
    db.insertLogin(user2);
    db.insertLogin(user1);
  }

  @FXML
  void loginButtonAction() {
    try {
      // gets the user input
      Login login = parseUserLogin();
      Set<Login> info = db.getAllLogin();
      boolean check = false;
      // checks every Login info in set
      for (Login l : info) {
        if (l.equals(login)) {
          check = true;
        }
      }

      // if info typed was right, you go to main window screen
      if (check) {
        eventBus.post(homeControllerFactory.create());
        //switchScenes("./v2/Main.fxml", loginButton.getScene().getWindow());
      } else {
        loginFail.setText("Incorrect username or password");
        //bounceTextAnimation(loginFail);
      }
    } catch (InvalidUserInputException ex) {
      Logger logger = Logger.getLogger(SanitationWindowController.class.getName());
      logger.log(Level.WARNING, "Unable to parse Username and Password.", ex);
    }
  }

  private Login parseUserLogin() throws InvalidUserInputException {
    // checks if input is valid, parses it and returns a new Login
    if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
      return new Login(username.getText(), password.getText());
    }

    // otherwise
    loginFail.setText("Incorrect username or password");
    //bounceTextAnimation(loginFail);

    throw new InvalidUserInputException("Unable to parse User Login info");
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    LoginController create();
  }
}
