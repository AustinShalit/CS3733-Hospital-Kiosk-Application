package edu.wpi.cs3733.d19.teamO.controller;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.component.MapView;

@FxmlController(url = "GuestWindow.fxml")
@SuppressWarnings({"PMD.TooManyFields", "PMD.RedundantFieldInitializer"})

public class GuestViewController implements Controller {


  @FXML
  BorderPane root;
  @FXML
  JFXButton loginbtn;

  @FXML
  MapView map;

  @Inject
  private LoginController.Factory loginFactory;

  private JFXPopup login;

  @FXML
  void initialize() {
    LoginController loginController = loginFactory.create();
    loginController.getLoginFail().textProperty().addListener(
        (observable, oldValue, newValue) -> {
          if ("Login Success".equals(newValue)) {
            login.hide();
          }
        }
    );
    login = new JFXPopup(loginController.root);
    login.setOnAutoHide(
        event -> {
          ColorAdjust reset = new ColorAdjust();
          reset.setBrightness(0);
          root.setEffect(reset);
        }
    );
  }

  @FXML
  void onLoginAction() {
    ColorAdjust colorAdjust = new ColorAdjust();
    colorAdjust.setBrightness(-0.2);
    root.setEffect(colorAdjust);
    login.show(root);
    login.setX(
        (root.getScene().getWindow().getWidth() - login.getWidth()) / 2
    );
    login.setY(
        (root.getScene().getWindow().getHeight() - login.getHeight()) / 2
    );
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    GuestViewController create();
  }
  /*
  @FXML
  @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "UseStringBufferForStringAppends"})
  void onGoButtonAction() throws IOException {

    if (toComboBox.getValue().equals(fromComboBox.getValue())) {
      DialogHelper.showInformationAlert("Must Select Different Start/End Destinations",
          "Please select different start and end destinations to generate a valid path.");
      return;
    }

    IGraphSearchAlgorithm<Node> algorithm = appPreferences.getGraphSearchAlgorithm().getAlgorithm();
    MutableGraph<Node> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
    database.getAllNodes().forEach(graph::addNode);
    database.getAllEdges().forEach(e -> graph.putEdge(e.getStartNode(), e.getEndNode()));

    List<Node> path = algorithm.getPath(ImmutableGraph.copyOf(graph),
        fromComboBox.getValue(),
        toComboBox.getValue());

    ArrayList<String> list = stepByStep.getStepByStep(path);
    String instruction;
    StringBuilder stringBuilder = new StringBuilder();
    for (String s: list) {
      stringBuilder.append(s);
      stringBuilder.append('\n');
    }
    instruction = stringBuilder.toString();
    instructions.setText(instruction);
    map.zoomTo(fromComboBox.getValue());
    map.setPath(path);
    map.drawPath();

  }
*/

}
