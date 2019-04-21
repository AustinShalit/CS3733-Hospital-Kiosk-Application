package edu.wpi.cs3733.d19.teamO.controller;

import java.util.Set;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.entity.Employee;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.entity.messaging.SmsRequest;
import edu.wpi.cs3733.d19.teamO.entity.messaging.SmsSender;

@FxmlController(url = "Sms.fxml")
public class SmsController implements Controller{

  @FXML
  BorderPane root;
  @FXML
  private JFXTextField messageText;
  @FXML
  private JFXButton submitbtn;

  @Inject
  private EventBus eventBus;
  @Inject
  private Database db;

  private Set<Employee> info;

  @FXML
  void initialize() {
    info = db.getAllEmployee();
  }

  @FXML
  void submitOnAction() {
    if(!messageText.getText().isEmpty()) {
      for(Employee emp: info) {
        String phoneNumber = emp.getPhone();
        String message = messageText.getText();
        SmsRequest sms = new SmsRequest("+1" + phoneNumber, message);
        SmsSender sender = new SmsSender();
        sender.sendSms(sms);
      }
    } else {
      DialogHelper.showErrorAlert("Error",
          "Please make sure all fields are filled out");
    }
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SmsController create();
  }
}
