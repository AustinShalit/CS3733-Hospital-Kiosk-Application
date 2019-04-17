package edu.wpi.cs3733.d19.teamO.request.controller;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.request.Request;
import edu.wpi.cs3733.d19.teamO.request.controller.request.ChangeMainViewEvent;

@FxmlController(url = "OrderTable.fxml")
public class OrderTableController implements Controller{

  @FXML
  private BorderPane root;
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

  @Inject
  private EventBus eventBus;
  @Inject
  private DominosHomeController.Factory domniosHomeControllerFactory;

  @FXML
  void backAction() {
    eventBus.post(new ChangeMainViewEvent(domniosHomeControllerFactory.create()));
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    OrderTableController create();
  }

}
