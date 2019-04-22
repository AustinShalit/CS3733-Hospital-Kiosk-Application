package edu.wpi.cs3733.d19.teamO.request;

import java.io.IOException;

import javafx.stage.Stage;

public class Request implements IRequest {
  @Override
  public void run(int xcoord, int ycoord, int windowWidth, int windowLength, String cssPath,
                  String destNodeID, String originNodeID) {
    try {
      Stage stage = new Stage();
      Project project = new Project();
      project.setStageMinWidth(windowWidth);
      project.setStageMinHeight(windowLength);
      project.setStageX(xcoord);
      project.setStageY(ycoord);
      project.init();
      project.start(stage);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
