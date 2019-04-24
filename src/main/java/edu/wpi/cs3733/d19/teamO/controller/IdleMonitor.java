package edu.wpi.cs3733.d19.teamO.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.util.Duration;

public class IdleMonitor {

  private final Timeline idleTimeline ;

  private final EventHandler<Event> userEventHandler ;

  public IdleMonitor(Duration idleTime, Runnable notifier, boolean startMonitoring) {
    idleTimeline = new Timeline(new KeyFrame(idleTime, e -> notifier.run()));
    idleTimeline.setCycleCount(Animation.INDEFINITE);

    userEventHandler = e -> notIdle() ;

    if (startMonitoring) {
      startMonitoring();
    }
  }

  public IdleMonitor(Duration idleTime, Runnable notifier) {
    this(idleTime, notifier, false);
  }

  public void register(Scene scene, EventType<? extends Event> eventType) {
    scene.addEventHandler(eventType, userEventHandler);
  }

  public void register(Node node, EventType<? extends Event> eventType) {
    node.addEventHandler(eventType, userEventHandler);
  }

  public void unregister(Scene scene, EventType<? extends Event> eventType) {
    scene.removeEventHandler(eventType, userEventHandler);
  }

  public void unregister(Node node, EventType<? extends Event> eventType) {
    node.removeEventHandler(eventType, userEventHandler);
  }

  public void notIdle() {
    if (idleTimeline.getStatus() == Animation.Status.RUNNING) {
      idleTimeline.playFromStart();
    }
  }

  public void startMonitoring() {
    idleTimeline.playFromStart();
  }

  public void stopMonitoring() {
    idleTimeline.stop();
  }
}