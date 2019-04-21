package edu.wpi.cs3733.d19.teamO.controller;

import java.lang.annotation.*;

/**
 * Tag a JavaFX controller that has an associated FXML file. The URL should be in the same package
 * in the resources folder as the class that this is annotating.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface FxmlController {
    /**
     * The location of the fxml file.
     *
     * @return The name of the fxml file to be loaded as the controller.
     */
    String url();
}
