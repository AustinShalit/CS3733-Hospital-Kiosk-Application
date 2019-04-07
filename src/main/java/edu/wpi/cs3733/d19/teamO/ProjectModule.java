package edu.wpi.cs3733.d19.teamO;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

import edu.wpi.cs3733.d19.teamO.entity.database.Database;

public class ProjectModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(Database.class).asEagerSingleton();
    bind(EventBus.class).asEagerSingleton();
  }
}
