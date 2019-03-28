package edu.wpi.cs3733d18.onyx_owlmen;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.bean.CsvToBeanBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Project extends Application {

  @Override
  public void start(Stage primaryStage) throws IOException {

    FileChooser fileChooser = new FileChooser();



    List<NodeCSV> nodeCSVList = new CsvToBeanBuilder<NodeCSV>(new FileReader(fileChooser.showOpenDialog(primaryStage).getPath()))
            .withType(NodeCSV.class).build().parse();

    System.out.println("finish Nodes");

    List<EdgeCSV> edgeCSVList = new CsvToBeanBuilder<EdgeCSV>(new FileReader(fileChooser.showOpenDialog(primaryStage).getPath()))
            .withType(EdgeCSV.class).build().parse();

    System.out.println("finish edges");

    DFS_Checker aChecker = new DFS_Checker(nodeCSVList,edgeCSVList);

    aChecker.createAllNodes();

    aChecker.runDFS("FHALL00101");

    boolean result = aChecker.checkDFS();

    System.out.println(result);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
    Pane mainWindow = loader.load();

    primaryStage.setTitle("Project Title");
    primaryStage.setScene(new Scene(mainWindow));
    primaryStage.show();
  }

}
