package com.example.disasterapi.notify;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
//@ConditionalOnProperty(name = "app.notify.sms.enabled", havingValue = "true")
public class TwilioSmsSender implements SmsSender {
  private final String from;

  public TwilioSmsSender(@Value("${app.notify.sms.accountSid:}") String sid,
                         @Value("${app.notify.sms.authToken:}") String token,
                         @Value("${app.notify.sms.from}") String from) {
    this.from = from;
    if (sid != null && !sid.isBlank()) {
      Twilio.init(sid, token == null ? "" : token);
    }
  }

  @Override
  public void send(String to, String body) throws Exception {
    if (to == null || to.isBlank()) return;
    Message.creator(new PhoneNumber(to), new PhoneNumber(from), body).create();
  }
}
