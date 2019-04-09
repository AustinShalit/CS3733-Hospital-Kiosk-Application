package edu.wpi.cs3733.d19.teamO.entity.database;

import java.sql.SQLException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class DatabaseExtension implements TestInstancePostProcessor, AfterEachCallback {

  private static final String KEY = DatabaseExtension.class.getName();

  private static ExtensionContext getRoot(ExtensionContext context) {
    return context.getParent().map(DatabaseExtension::getRoot).orElse(context);
  }

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
    getOrCreateInjector(context).injectMembers(testInstance);
  }

  private static Injector getOrCreateInjector(ExtensionContext context) {
    return getRoot(context)
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .getOrComputeIfAbsent(KEY,
            key -> Guice.createInjector((Module) binder
                -> {
              binder.bind(DatabaseConnectionFactory.class)
                  .toInstance(new DatabaseConnectionFactoryMemory(context.getUniqueId()));
              binder.bind(Database.class).asEagerSingleton();
            }),
            Injector.class);
  }

  @Override
  public void afterEach(ExtensionContext context) throws SQLException {
    getRoot(context)
        .getStore(ExtensionContext.Namespace.GLOBAL)
        .get(KEY, Injector.class)
        .getBinding(DatabaseConnectionFactory.class)
        .getProvider()
        .get()
        .drop();
  }
}
