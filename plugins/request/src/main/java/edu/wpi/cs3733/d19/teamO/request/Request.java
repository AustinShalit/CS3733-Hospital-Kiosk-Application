package edu.wpi.cs3733.d19.teamO.request;

import java.io.IOException;
import javafx.stage.Stage;

public class Request implements IRequest {
  @Override
  public void run(int xcoord, int ycoord, int windowWidth, int windowLength, String cssPath,
                  String destNodeID, String originNodeID) {
    try {
      Stage stage = new Stage();
      Project p = new Project();
      p.setStageMinWidth(windowWidth);
      p.setStageMinHeight(windowLength);
      p.setStageX(xcoord);
      p.setStageY(ycoord);
      p.init();
      p.start(stage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
