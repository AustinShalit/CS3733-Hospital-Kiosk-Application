package edu.wpi.cs3733.d19.teamO;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

public class ProjectModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(EventBus.class).asEagerSingleton();

		bind(AppPreferences.class).asEagerSingleton();
	}
}
