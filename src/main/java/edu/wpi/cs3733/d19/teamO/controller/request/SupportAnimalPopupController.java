package edu.wpi.cs3733.d19.teamO.controller.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import edu.wpi.cs3733.d19.teamO.component.FuzzyWuzzyComboBox;
import edu.wpi.cs3733.d19.teamO.controller.Controller;
import edu.wpi.cs3733.d19.teamO.controller.DialogHelper;
import edu.wpi.cs3733.d19.teamO.controller.FxmlController;
import edu.wpi.cs3733.d19.teamO.entity.Node;
import edu.wpi.cs3733.d19.teamO.entity.SupportAnimalRequest;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.CyclomaticComplexity", "PMD.TooManyFields"})

@FxmlController(url = "SupportAnimalPopup.fxml")
public class SupportAnimalPopupController implements Controller {

  private static final Logger logger =
      Logger.getLogger(SupportAnimalPopupController.class.getName());

  @FXML
  BorderPane root;
  @FXML
  private JFXTextField nametxt;
  @FXML
  private FuzzyWuzzyComboBox locationbox;
  @FXML
  private JFXComboBox<SupportAnimalRequest.SupportAnimalRequestType> categorybox;
  @FXML
  private JFXTextArea descriptiontxt;
  @FXML
  private JFXButton submitbtn;
  @FXML
  private JFXButton button1;
  @FXML
  private JFXButton button2;
  @FXML
  private JFXButton button3;
  @FXML
  private JFXButton button4;
  @FXML
  private JFXButton button5;
  @FXML
  private JFXButton button6;
  @FXML
  private JFXButton button7;
  @FXML
  private JFXButton button8;
  @FXML
  private JFXButton button9;

  @Inject
  private Database db;

  private SupportAnimalRequest.SupportAnimalRequestType supportAnimalRequestType;

  ImageView image1 = new ImageView(new Image(getClass()
      .getResourceAsStream("../request/SupportAnimalSource/Jackson.jpg")));
  ImageView image2 = new ImageView(new Image(getClass()
      .getResourceAsStream("../request/SupportAnimalSource/Chubbs.jpg")));
  ImageView image3 = new ImageView(new Image(getClass()
      .getResourceAsStream("../request/SupportAnimalSource/Dora.jpg")));
  ImageView image4 = new ImageView(new Image(getClass()
      .getResourceAsStream("../request/SupportAnimalSource/Marmalade.jpg")));
  ImageView image5 = new ImageView(new Image(getClass()
      .getResourceAsStream("../request/SupportAnimalSource/Jasper.jpg")));
  ImageView image6 = new ImageView(new Image(getClass()
      .getResourceAsStream("../request/SupportAnimalSource/Lois.jpg")));
  ImageView image7 = new ImageView(new Image(getClass()
      .getResourceAsStream("../request/SupportAnimalSource/Lovey.jpg")));
  ImageView image8 = new ImageView(new Image(getClass()
      .getResourceAsStream("../request/SupportAnimalSource/Mimi.jpg")));
  ImageView image9 = new ImageView(new Image(getClass()
      .getResourceAsStream("../request/SupportAnimalSource/Oliver.jpg")));

  @FXML
  void initialize() {
    locationbox.setNodes(db.getAllNodes());
    locationbox.setupAutoRefresh();
    locationbox.refresh();
    categorybox.getItems().setAll(SupportAnimalRequest.SupportAnimalRequestType.values());

    submitbtn.disableProperty().bind(
        Bindings.isEmpty(nametxt.textProperty())
            .or(Bindings.isNull(locationbox.valueProperty()))
            .or(Bindings.isNull(categorybox.valueProperty()))
    );

    ArrayList<ImageView> imageViewList = new ArrayList<>(Arrays.asList(
        image1, image2, image3,
        image4, image5, image6,
        image7, image8, image9));

    for (ImageView image: imageViewList) {
      image.setFitWidth(160.00);
      image.setFitHeight(160.00);
    }

    button1.setPrefSize(180, 180);
    button2.setPrefSize(180, 180);
    button3.setPrefSize(180, 180);
    button4.setPrefSize(180, 180);
    button5.setPrefSize(180, 180);
    button6.setPrefSize(180, 180);
    button7.setPrefSize(180, 180);
    button8.setPrefSize(180, 180);
    button9.setPrefSize(180, 180);

    button1.setGraphic(image1);
    button2.setGraphic(image2);
    button3.setGraphic(image3);
    button4.setGraphic(image4);
    button5.setGraphic(image5);
    button6.setGraphic(image6);
    button7.setGraphic(image7);
    button8.setGraphic(image8);
    button9.setGraphic(image9);
  }

  @FXML
  void onSubmitButtonAction() {
    SupportAnimalRequest request = parseRequest();
    if (request == null) {
      logger.log(Level.WARNING,
          "Unable to parse support animal Request.",
          "Unable to parse support animal Request.");
      return;
    }

    nametxt.setText(null);
    locationbox.getSelectionModel().clearSelection();
    locationbox.setValue(null);
    categorybox.getSelectionModel().clearSelection();
    categorybox.setValue(null);
    descriptiontxt.setText("");

    if (db.insertSupportAnimalRequest(request)) {
      String message = "Successfully submitted support animal request.";
      DialogHelper.showInformationAlert("Success!", message);
    } else {
      DialogHelper.showErrorAlert("Error.",
          "Unable to submit support animal request.");
    }
  }

  @FXML
  void onAction(MouseEvent event) {
    if (event.getSource() == button1) {
      supportAnimalRequestType = SupportAnimalRequest.SupportAnimalRequestType.Jackson;
    } else if (event.getSource() == button2) {
      supportAnimalRequestType = SupportAnimalRequest.SupportAnimalRequestType.Chubbs;
    } else if (event.getSource() == button3) {
      supportAnimalRequestType = SupportAnimalRequest.SupportAnimalRequestType.Dora;
    } else if (event.getSource() == button4) {
      supportAnimalRequestType = SupportAnimalRequest.SupportAnimalRequestType.Marmalade;
    } else if (event.getSource() == button5) {
      supportAnimalRequestType = SupportAnimalRequest.SupportAnimalRequestType.Jasper;
    } else if (event.getSource() == button6) {
      supportAnimalRequestType = SupportAnimalRequest.SupportAnimalRequestType.Lois;
    } else if (event.getSource() == button7) {
      supportAnimalRequestType = SupportAnimalRequest.SupportAnimalRequestType.Lovey;
    } else if (event.getSource() == button8) {
      supportAnimalRequestType = SupportAnimalRequest.SupportAnimalRequestType.Mimi;
    } else if (event.getSource() == button9) {
      supportAnimalRequestType = SupportAnimalRequest.SupportAnimalRequestType.Oliver;
    }
    if (supportAnimalRequestType != null) {
      categorybox.setValue(supportAnimalRequestType);
    }
  }

  @FXML
  void showName(MouseEvent event) {
    if (event.getSource() == button1) {
      button1.setGraphic(null);
      button1.setStyle("-fx-background-color: #011E3C; -fx-opacity: 80; "
          + "-fx-text-alignment: CENTER; -fx-text-fill: white");
      button1.setText("Jackson\n Dog\n Male\n Enjoys playing \n with others");
    } else if (event.getSource() == button2) {
      button2.setGraphic(null);
      button2.setStyle("-fx-background-color: #011E3C; -fx-opacity: 80; "
          + "-fx-text-alignment: CENTER; -fx-text-fill: white");
      button2.setText("Chubbs\n Guinea Pig\n Male\n Likes carrots \n and hay");
    } else if (event.getSource() == button3) {
      button3.setGraphic(null);
      button3.setStyle("-fx-background-color: #011E3C; -fx-opacity: 80; "
          + "-fx-text-alignment: CENTER; -fx-text-fill: white");
      button3.setText("Dora\n Cat\n Female\n Outgoing \n and curious");
    } else if (event.getSource() == button4) {
      button4.setGraphic(null);
      button4.setStyle("-fx-background-color: #011E3C; -fx-opacity: 80; "
          + "-fx-text-alignment: CENTER; -fx-text-fill: white");
      button4.setText("Marmalade\n Cat\n Female\n Enjoys talking \n with people");
    } else if (event.getSource() == button5) {
      button5.setGraphic(null);
      button5.setStyle("-fx-background-color: #011E3C; -fx-opacity: 80; "
          + "-fx-text-alignment: CENTER; -fx-text-fill: white");
      button5.setText("Jasper\n Cat\n Male\n Gentle \n and respectful");
    } else if (event.getSource() == button6) {
      button6.setGraphic(null);
      button6.setStyle("-fx-background-color: #011E3C; -fx-opacity: 80; "
          + "-fx-text-alignment: CENTER; -fx-text-fill: white");
      button6.setText("Lois\n Bird\n Female\n Sage \n and humorous");
    } else if (event.getSource() == button7) {
      button7.setGraphic(null);
      button7.setStyle("-fx-background-color: #011E3C; -fx-opacity: 80; "
          + "-fx-text-alignment: CENTER; -fx-text-fill: white");
      button7.setText("Lovey\n Dog\n Female\n Enjoys playing, \n sweet");
    } else if (event.getSource() == button8) {
      button8.setGraphic(null);
      button8.setStyle("-fx-background-color: #011E3C; -fx-opacity: 80; "
          + "-fx-text-alignment: CENTER; -fx-text-fill: white");
      button8.setText("Mimi\n Dog\n Female\n Adorable \n but shy");
    } else if (event.getSource() == button9) {
      button9.setGraphic(null);
      button9.setStyle("-fx-background-color: #011E3C; -fx-opacity: 80; "
          + "-fx-text-alignment: CENTER; -fx-text-fill: white");
      button9.setText("Oliver\n Guinea Pig\n Male\n Enjoys Running \n around");
    }
  }

  @FXML
  void showGraph(MouseEvent event) {
    if (event.getSource() == button1) {
      button1.setGraphic(image1);
      button1.setText(null);
    } else if (event.getSource() == button2) {
      button2.setGraphic(image2);
      button2.setText(null);
    } else if (event.getSource() == button3) {
      button3.setGraphic(image3);
      button3.setText(null);
    } else if (event.getSource() == button4) {
      button4.setGraphic(image4);
      button4.setText(null);
    } else if (event.getSource() == button5) {
      button5.setGraphic(image5);
      button5.setText(null);
    } else if (event.getSource() == button6) {
      button6.setGraphic(image6);
      button6.setText(null);
    } else if (event.getSource() == button7) {
      button7.setGraphic(image7);
      button7.setText(null);
    } else if (event.getSource() == button8) {
      button8.setGraphic(image8);
      button8.setText(null);
    } else if (event.getSource() == button9) {
      button9.setGraphic(image9);
      button9.setText(null);
    }
  }


  /**
   * Parse input the user has inputted for the request.
   *
   * @return If valid input, A SupportAnimalRequest representing the users input. Otherwise null.
   */
  private SupportAnimalRequest parseRequest() {
    // if input is valid, parse it and return a new Request
    if (!nametxt.getText().isEmpty()
        && Objects.nonNull(locationbox.getNodeValue())
        && Objects.nonNull(categorybox.getValue())) {

      Node node = locationbox.getNodeValue();
      String description = descriptiontxt.getText();
      String name = nametxt.getText();

      return new SupportAnimalRequest(LocalDateTime.now(),
          node,
          categorybox.getValue(),
          description,
          name);
    }

    // otherwise, some input was invalid
    DialogHelper.showErrorAlert("Error.", "Please make sure all fields are filled out.");
    return null;
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  public interface Factory {
    SupportAnimalPopupController create();
  }
}
