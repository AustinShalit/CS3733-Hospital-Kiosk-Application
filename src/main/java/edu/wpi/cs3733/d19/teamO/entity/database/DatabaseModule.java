package edu.wpi.cs3733.d19.teamO.entity.database;

import com.google.inject.AbstractModule;

public class DatabaseModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(DatabaseConnectionFactory.class)
				.to(DatabaseConnectionFactoryEmbedded.class)
				.asEagerSingleton();
		bind(Database.class).asEagerSingleton();
	}
}
