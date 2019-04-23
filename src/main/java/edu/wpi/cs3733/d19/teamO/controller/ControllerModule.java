package edu.wpi.cs3733.d19.teamO.controller;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javafx.fxml.FXMLLoader;

import edu.wpi.cs3733.d19.teamO.controller.request.AudioVisualController;
import edu.wpi.cs3733.d19.teamO.controller.request.AudioVisualPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.AudioVisualViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.ExternalTransportationController;
import edu.wpi.cs3733.d19.teamO.controller.request.ExternalTransportationPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.ExternalTransportationViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.FloristRequestController;
import edu.wpi.cs3733.d19.teamO.controller.request.FloristRequestPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.FloristRequestViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.GiftController;
import edu.wpi.cs3733.d19.teamO.controller.request.GiftPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.GiftViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.ITSupportController;
import edu.wpi.cs3733.d19.teamO.controller.request.ITSupportPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.ITSupportViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.InternalTransportationController;
import edu.wpi.cs3733.d19.teamO.controller.request.InternalTransportationPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.InternalTransportationViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.InterpreterController;
import edu.wpi.cs3733.d19.teamO.controller.request.InterpreterPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.InterpreterViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.PatientInfoController;
import edu.wpi.cs3733.d19.teamO.controller.request.PatientInfoPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.PatientInfoViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.PrescriptionRequestPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.PrescriptionViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.ReligiousServiceController;
import edu.wpi.cs3733.d19.teamO.controller.request.ReligiousServicePopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.ReligiousServiceViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.SanitationController;
import edu.wpi.cs3733.d19.teamO.controller.request.SanitationPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.SanitationViewController;
import edu.wpi.cs3733.d19.teamO.controller.request.SupportAnimalController;
import edu.wpi.cs3733.d19.teamO.controller.request.SupportAnimalPopupController;
import edu.wpi.cs3733.d19.teamO.controller.request.SupportAnimalViewController;

@SuppressWarnings({"PMD.CouplingBetweenObjects", "PMD.ExcessiveImports"})
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

    install(new FactoryModuleBuilder().build(LoginController.Factory.class));
    install(new FactoryModuleBuilder().build(NavigationController.Factory.class));
    install(new FactoryModuleBuilder().build(OptionsPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(RequestController.Factory.class));
    install(new FactoryModuleBuilder().build(AddEmployeeController.Factory.class));
    install(new FactoryModuleBuilder().build(UpdateEmployeeController.Factory.class));
    install(new FactoryModuleBuilder().build(EmployeeController.Factory.class));
    install(new FactoryModuleBuilder().build(AboutController.Factory.class));
    install(new FactoryModuleBuilder().build(FireAlarmController.Factory.class));

    install(new FactoryModuleBuilder().build(SmsController.Factory.class));

    install(new FactoryModuleBuilder().build(AudioVisualPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(AudioVisualController.Factory.class));
    install(new FactoryModuleBuilder().build(AudioVisualViewController.Factory.class));

    install(new FactoryModuleBuilder().build(SettingsController.Factory.class));

    install(new FactoryModuleBuilder().build(AudioVisualController.Factory.class));
    install(new FactoryModuleBuilder().build(AudioVisualViewController.Factory.class));

    install(new FactoryModuleBuilder().build(MapEditController.Factory.class));
    install(new FactoryModuleBuilder().build(SchedulingController.Factory.class));
    install(new FactoryModuleBuilder().build(SchedulingViewController.Factory.class));
    install(new FactoryModuleBuilder().build(SchedulingCalendarController.Factory.class));

    install(new FactoryModuleBuilder().build(AudioVisualController.Factory.class));
    install(new FactoryModuleBuilder().build(AudioVisualViewController.Factory.class));

    install(new FactoryModuleBuilder().build(InternalTransportationPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(InternalTransportationController.Factory.class));
    install(new FactoryModuleBuilder().build(InternalTransportationViewController.Factory.class));

    install(new FactoryModuleBuilder().build(PatientInfoPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(PatientInfoController.Factory.class));
    install(new FactoryModuleBuilder().build(PatientInfoViewController.Factory.class));

    install(new FactoryModuleBuilder().build(PrescriptionRequestPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(PrescriptionViewController.Factory.class));

    install(new FactoryModuleBuilder().build(ExternalTransportationPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(ExternalTransportationController.Factory.class));
    install(new FactoryModuleBuilder().build(ExternalTransportationViewController.Factory.class));

    install(new FactoryModuleBuilder().build(GiftPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(GiftController.Factory.class));
    install(new FactoryModuleBuilder().build(GiftViewController.Factory.class));

    install(new FactoryModuleBuilder().build(InterpreterController.Factory.class));
    install(new FactoryModuleBuilder().build(InterpreterPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(InterpreterViewController.Factory.class));

    install(new FactoryModuleBuilder().build(ReligiousServicePopupController.Factory.class));
    install(new FactoryModuleBuilder().build(ReligiousServiceController.Factory.class));
    install(new FactoryModuleBuilder().build(ReligiousServiceViewController.Factory.class));

    install(new FactoryModuleBuilder().build(SupportAnimalPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(SupportAnimalController.Factory.class));
    install(new FactoryModuleBuilder().build(SupportAnimalViewController.Factory.class));

    install(new FactoryModuleBuilder().build(ITSupportPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(ITSupportController.Factory.class));
    install(new FactoryModuleBuilder().build(ITSupportViewController.Factory.class));

    install(new FactoryModuleBuilder().build(FloristRequestPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(FloristRequestController.Factory.class));
    install(new FactoryModuleBuilder().build(FloristRequestViewController.Factory.class));

    install(new FactoryModuleBuilder().build(SanitationController.Factory.class));
    install(new FactoryModuleBuilder().build(SanitationPopupController.Factory.class));
    install(new FactoryModuleBuilder().build(SanitationViewController.Factory.class));

  }
}
