package edu.wpi.cs3733.d19.teamO.entity.messaging;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {

  public static final String ACCOUNT_SID = "AC475c5d6887fa262589699f257ffb9aab";
  public static final String AUTH_TOKEN = "951b87f941fd72d6e14bcc8be1f906ca";

  public void sendSms(SmsRequest smsRequest) {
    if(isPhoneNumberValid(smsRequest.getPhoneNumber())) {
      Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
      PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
      PhoneNumber from = new PhoneNumber("+16175534984");
      String message = smsRequest.getMessage();
      Message creator = Message.creator(to, from, message).create();
    } else {
      throw new IllegalArgumentException(
          "Phone Number [" + smsRequest.getPhoneNumber() + "] is not a valid number"
      );
    }
  }

  private boolean isPhoneNumberValid(String phoneNumber) {
    return true;
  }
}
