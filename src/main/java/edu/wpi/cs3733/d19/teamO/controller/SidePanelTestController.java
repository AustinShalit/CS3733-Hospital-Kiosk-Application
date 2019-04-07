package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SidePanelTestController extends Controller {

  @FXML // fx:id="navMainBorder"
  private BorderPane navMainBorder; // Value injected by FXMLLoader
  @FXML // fx:id="navHamburger"
  private JFXHamburger navHamburger; // Value injected by FXMLLoader
  @FXML // fx:id="navDrawer"
  private JFXDrawer navDrawer; // Value injected by FXMLLoader
  @FXML // fx:id="backButton"
  private JFXButton backButton;

  @FXML
    // This method is called by the FXMLLoader when initialization is complete
  void initialize() throws IOException {
    VBox box = FXMLLoader.load(getClass().getResource("SidePanelPopOut.fxml"));
    navDrawer.setSidePane(box);

    HamburgerBackArrowBasicTransition burgerBack
        = new HamburgerBackArrowBasicTransition(navHamburger);
    burgerBack.setRate(-1);
    navHamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
      burgerBack.setRate(burgerBack.getRate() * -1);
      burgerBack.play();

      if (navDrawer.isOpened()) {
        navDrawer.close();
      } else {
        navDrawer.open();
      }
    });

  }

  @FXML
  void onBackButtonAction() {
    switchScenes("MainWindow.fxml",
        backButton.getScene().getWindow());
  }
}
