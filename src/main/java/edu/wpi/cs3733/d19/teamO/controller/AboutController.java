package edu.wpi.cs3733.d19.teamO.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

@FxmlController(url ="About.fxml")
public class AboutController implements Controller {

  @FXML
  private Text aboutPage;
  @FXML
  private Text compSci;
  @FXML
  private Text softEngCourse;
  @FXML
  private Text profWong;
  @FXML
  private Text danD;
  @FXML
  private Text teamMem;
  @FXML
  private Text leadS;
  @FXML
  private Text AssistLeadS;
  @FXML
  private Text softEng;
  @FXML
  private Text projMan;
  @FXML
  private Text scrumMaster;
  @FXML
  private Text testEng;
  @FXML
  private Text prodOwn;
  @FXML
  private Text docAnalyst;
  @FXML
  private Text thankYou;
  @FXML
  private BorderPane root;

  public interface Factory {
    AboutController create();
  }

  @Override
  public Parent getRoot() {
    return root;
  }

}
