package edu.wpi.cs3733.d19.teamO;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

import edu.wpi.cs3733.d19.teamO.entity.Floors;

public class ProjectModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(EventBus.class).asEagerSingleton();

    bind(AppPreferences.class).asEagerSingleton();
    bind(Floors.class).asEagerSingleton();
  }
}
