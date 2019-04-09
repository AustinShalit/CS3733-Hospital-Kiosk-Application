package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DatabaseExtension implements BeforeEachCallback, AfterEachCallback {

  private static String cleanId(final String id) {
    return id.replaceAll("[^A-Za-z0-9]", "");
  }

  private static Injector createInjector(ExtensionContext context) {
    return Guice.createInjector((Module) binder -> {
      binder.bind(DatabaseConnectionFactory.class)
          .toInstance(new DatabaseConnectionFactoryMemory(cleanId(context.getUniqueId())));
      binder.bind(Database.class).asEagerSingleton();
    });
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    createInjector(context).injectMembers(context.getTestInstance().get());
  }

  @Override
  public void afterEach(ExtensionContext context) throws SQLException {
    new DatabaseConnectionFactoryMemory(cleanId(context.getUniqueId())).drop();
  }
}
