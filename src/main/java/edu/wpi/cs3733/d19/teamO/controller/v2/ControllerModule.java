package edu.wpi.cs3733.d19.teamO.controller.v2;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javafx.fxml.FXMLLoader;

import edu.wpi.cs3733.d19.teamO.controller.v2.request.ExternalTransportationController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.ExternalTransportationViewController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.GiftController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.GiftViewController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.InternalTransportationController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.InternalTransportationViewController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.InterpreterController;
import edu.wpi.cs3733.d19.teamO.controller.v2.request.InterpreterViewController;

public class ControllerModule extends AbstractModule {
  @Override
  protected void configure() {
    bindListener(Matchers.any(), new TypeListener() {
      @Override
      public <I> void hear(final TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        typeEncounter.register((InjectionListener<I>) i -> {
          if (!i.getClass().isAnnotationPresent(FxmlController.class)) {
            return;
          }
          try {
            FXMLLoader.load(i.getClass().getResource(
                i.getClass().getAnnotation(FxmlController.class).url()),
                null, null,
                c -> i
            );
          } catch (IOException ex) {
            throw new IllegalStateException("Failed to load FXML", ex);
          }
        });
      }
    });

    install(new FactoryModuleBuilder().build(HomeController.Factory.class));
    install(new FactoryModuleBuilder().build(LoginController.Factory.class));
    install(new FactoryModuleBuilder().build(OptionsPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(RequestController.Factory.class));

    install(new FactoryModuleBuilder().build(InternalTransportationController.Factory.class));
    install(new FactoryModuleBuilder().build(InternalTransportationViewController.Factory.class));

    install(new FactoryModuleBuilder().build(ExternalTransportationController.Factory.class));
    install(new FactoryModuleBuilder().build(ExternalTransportationViewController.Factory.class));

    install(new FactoryModuleBuilder().build(GiftController.Factory.class));
    install(new FactoryModuleBuilder().build(GiftViewController.Factory.class));

    install(new FactoryModuleBuilder().build(InterpreterController.Factory.class));
    install(new FactoryModuleBuilder().build(InterpreterViewController.Factory.class));
  }
}
