package edu.wpi.cs3733.d19.teamO;

import java.util.Timer;

public class IdleTimeout {
    private Timer timer;
    private String name;
    private long period;

    /**
     * @param name
     */
    public IdleTimeout(String name) {
        timer = new Timer();
        period = 30000;
        this.name = name;
    }

    /**
     * @param name
     * @param period
     */
    public IdleTimeout(String name, long period) {
        timer = new Timer();
        this.name = name;
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public Timer getTimer() {
        return timer;
    }

    public long getPeriod() {
        return period;
    }

    @Override
    public String toString() {
        return "IdleTimeout{" +
                "timer=" + timer +
                ", name='" + name + '\'' +
                ", period=" + period +
                '}';
    }
}
