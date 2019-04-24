package edu.wpi.cs3733.d19.teamO.entity.messaging;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import edu.wpi.cs3733.d19.teamO.entity.Node;

public class SmsRemind implements Runnable {

  private LocalDateTime start;
  private String phoneNumber;
  private Node roomNode;
  private String personName;

  public SmsRemind(LocalDateTime start, String phoneNumber, Node roomNode, String personName) {
    this.start = start;
    this.phoneNumber = phoneNumber;
    this.roomNode = roomNode;
    this.personName = personName;
  }

  @Override
  public void run() {
    while(true) {
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime tempDateTime = LocalDateTime.from(now);
      long years = tempDateTime.until(start, ChronoUnit.YEARS);
      tempDateTime = tempDateTime.plusYears(years);

      long months = tempDateTime.until(start, ChronoUnit.MONTHS);
      tempDateTime = tempDateTime.plusMonths(months);

      long days = tempDateTime.until(start, ChronoUnit.DAYS);
      tempDateTime = tempDateTime.plusDays(days);


      long hours = tempDateTime.until(start, ChronoUnit.HOURS);
      tempDateTime = tempDateTime.plusHours(hours);

      long minutes = tempDateTime.until(start, ChronoUnit.MINUTES);
      tempDateTime = tempDateTime.plusMinutes(minutes);

      long seconds = tempDateTime.until(start, ChronoUnit.SECONDS);

      if (years == 0 && months == 0 && days == 0 && hours == 0 && minutes <= 2) {
        SmsRequest sms = new SmsRequest("+1" + phoneNumber,
            "Hey " + personName + ", this is a reminder that your reservation for " + roomNode.getLongName() + " is starting in 3 minutes");
        SmsSender sender = new SmsSender();
        sender.sendSms(sms);
        return;
      }
    }

  }
}
