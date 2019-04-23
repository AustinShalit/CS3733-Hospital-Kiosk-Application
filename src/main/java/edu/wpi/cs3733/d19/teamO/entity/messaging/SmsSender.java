package edu.wpi.cs3733.d19.teamO.entity.messaging;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

public class SmsSender {

	public static final String ACCOUNT_SID = "AC475c5d6887fa262589699f257ffb9aab";
	public static final String AUTH_TOKEN = "951b87f941fd72d6e14bcc8be1f906ca";

	/**
	 * Function to send given message to given phonenumber.
	 *
	 * @param smsRequest Object
	 */
	public void sendSms(SmsRequest smsRequest) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
		PhoneNumber from = new PhoneNumber("+16175534984");
		String message = smsRequest.getMessage();
		MessageCreator creator = Message.creator(to, from, message);
		creator.create();
	}
}
