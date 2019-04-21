package edu.wpi.cs3733.d19.teamO;

import edu.wpi.cs3733.d19.teamO.util.Registry;
import javafx.collections.ObservableList;

import java.util.Objects;

public class IdleTimeouts extends Registry<IdleTimeout> {

    // TODO replace with DI eg Guice
    private static IdleTimeouts defaultInstance;

    public static final IdleTimeout THIRTY_SECONDS = new IdleTimeout("30 seconds", 30000);
    public static final IdleTimeout FIVE_SECONDS = new IdleTimeout("5 seconds", 5000);
    public static final IdleTimeout INITIAL_TIMEOUT = THIRTY_SECONDS;
    public static final IdleTimeout TEST_TIMEOUT = FIVE_SECONDS;

    /**
     * Creates a new idleTimeout registry.
     *
     * @param initial the initial idleTimeouts
     */
    public IdleTimeouts(IdleTimeout... initial) {
        registerAll(initial);
    }

    /**
     * Gets the default idleTimeouts instance.
     *
     * @return the default instance of idleTimeouts
     */
    public static IdleTimeouts getDefault() {
        synchronized (IdleTimeouts.class) {
            if (defaultInstance == null) {
                defaultInstance = new IdleTimeouts(THIRTY_SECONDS, FIVE_SECONDS);
            }
        }
        return defaultInstance;
    }

    /**
     * Gets the idleTimeout with the given name. If there is no idleTimeout with that name, returns
     * {@link #INITIAL_TIMEOUT} instead.
     *
     * @param name the name of the idleTimeout to get
     */
    public IdleTimeout forName(String name) {
        return getItems().stream()
                .filter(t -> t.getName().equals(name))
                .findFirst()
                .orElse(INITIAL_TIMEOUT);
    }

    @Override
    public void register(IdleTimeout timeout) {
        Objects.requireNonNull(timeout);
        if (isRegistered(timeout)) {
            throw new IllegalArgumentException("IdleTimeout " + timeout + " is already registered");
        }
        addItem(timeout);
    }

    @Override
    public void unregister(IdleTimeout timeout) {
        removeItem(timeout);
    }

    /**
     * Gets an observable list of the registered IdleTimeouts.
     */
    public ObservableList<IdleTimeout> getIdleTimeouts() {
        return getItems();
    }
}
