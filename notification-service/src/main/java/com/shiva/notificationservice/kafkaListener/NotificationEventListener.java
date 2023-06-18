package com.shiva.notificationservice.kafkaListener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TrackOpens;
import com.mailjet.client.transactional.TransactionalEmail;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import com.shiva.notificationservice.event.OrderPlacedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotificationEventListener {

    @KafkaListener(topics = "notificationTopic")
    public void handleNotificationEvent(OrderPlacedEvent orderPlacedEvent) {
        // Send the email
        log.info("Recieved notification for order: {}", orderPlacedEvent.getOrderNumber());
        sendEmail(orderPlacedEvent);
    }

    @Value("${mailjetclient.apikey_public}")
    private String apikeyPublic;

    @Value("${mailjetclient.apikey_private}")
    private String apikeyPrivate;

    public void sendEmail(OrderPlacedEvent orderPlacedEvent) {
        ClientOptions options = ClientOptions.builder()
                .apiKey(apikeyPublic)
                .apiSecretKey(apikeyPrivate)
                .build();
        MailjetClient client = new MailjetClient(options);
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(orderPlacedEvent.getEmail_id()))
                .from(new SendContact("shivu.a.1945@gmail.com"))
                .htmlPart("<h2>Order is successfully placed. </h2>"
                        + "<h3><b> Order id: </b>" + orderPlacedEvent.getOrderNumber() + "</h3>"
                        + "<h3><b> Total price: </b> " + orderPlacedEvent.getTotalPrice() + "</h3>")
                .subject("Order Confirmation")
                .trackOpens(TrackOpens.ENABLED)
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message1)
                .build();

        // act
        try {
            SendEmailsResponse response = request.sendWith(client);
            log.info("Email sent to -> {}", orderPlacedEvent.getEmail_id());
        } catch (MailjetException e) {
            log.error("Couldn't send email to -> {}, error -> {}", orderPlacedEvent.getEmail_id(),
                    e.getLocalizedMessage());
        }
    }

}
