package edu.wpi.cs3733.d19.teamO.request.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.controller.event.ChangeMainViewEvent;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;
import edu.wpi.cs3733.d19.teamO.request.Request;

@FxmlController(url = "OrderTable.fxml")
public class OrderTableController implements Controller {

  @Inject
  private EventBus eventBus;
  @Inject
  private OrderTableController.Factory orderTableControllerFactory;
  @Inject
  private DominosHomeController.Factory dominosHomeControllerFactory;
  @Inject
  private Database db;

  @FXML
  private BorderPane root;
  @FXML
  private JFXButton backButton;

  @FXML
  private TableView<Request> orderTable;
  @FXML
  private TableColumn<Request, String> firstNameCol;
  @FXML
  private TableColumn<Request, String> lastNameCol;
  @FXML
  private TableColumn<Request, Integer> idCol;
  @FXML
  private TableColumn<Request, String> locationCol;
  @FXML
  private TableColumn<Request, String> phoneCol;
  @FXML
  private TableColumn<Request, String> timeDateCol;
  @FXML
  private TableColumn<Request, Double> totalCol;
  @FXML
  private TableColumn<Request, String> descriptionCol;

  @FXML
  void backAction() {
    eventBus.post(new ChangeMainViewEvent(dominosHomeControllerFactory.create()));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    OrderTableController create();
  }
}
