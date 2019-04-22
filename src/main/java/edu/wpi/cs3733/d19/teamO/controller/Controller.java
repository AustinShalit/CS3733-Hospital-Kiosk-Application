package edu.wpi.cs3733.d19.teamO.controller;

import com.google.inject.Inject;
import javafx.scene.Parent;

/**
 * A controller should always provide a method to get the root node that it is controlling. This
 * allows the controllers root node to be added to other UI components.
 */
public interface Controller {

    /**
     * The root node for composition.
     *
     * @return The root node of the controller.
     */
    Parent getRoot();


}
