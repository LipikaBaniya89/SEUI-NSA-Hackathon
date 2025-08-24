package com.example.disasterapi.notify;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.notify.email.enabled", havingValue = "true", matchIfMissing = true)
public class SendGridEmailSender implements EmailSender {
    private final String from;
    private final SendGrid client;

    public SendGridEmailSender(@Value("${app.notify.email.from}") String from,
                               @Value("${app.notify.email.sendgridApiKey:}") String apiKey) {
        this.from = from;
        this.client = new SendGrid(apiKey == null ? "" : apiKey);
    }

    @Override
    public void send(String to, String subject, String body) throws Exception {
        if (to == null || to.isBlank()) return;
        Mail mail = new Mail(new Email(from), subject, new Email(to), new Content("text/plain", body));
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        client.api(request);
    }
}
