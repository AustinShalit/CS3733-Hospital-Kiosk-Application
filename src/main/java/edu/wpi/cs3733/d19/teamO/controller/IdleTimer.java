package edu.wpi.cs3733.d19.teamO.controller;

import static java.lang.Thread.sleep;

public class IdleTimer {

    private static IdleTimer ourInstance = new IdleTimer();

    private static long startTime;
    private static long endTime;

    public static IdleTimer getInstance() {
        return ourInstance;
    }

    private IdleTimer() {
    }

    public static void startIdleTimer() {
        startTime = System.currentTimeMillis();

        }

    public void resetIdleTimer(){
        startTime = System.currentTimeMillis();
    }

    public static long getStartTime() {
        return startTime;
    }
    public static long getEndTime() {
        return endTime;
    }

    public static void setEndTime(long endTime) {
        IdleTimer.endTime = endTime;
    }

    private class IdleUserException extends Exception {
        public IdleUserException() {
        }
    }
}
