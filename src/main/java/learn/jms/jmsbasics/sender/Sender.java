package learn.jms.jmsbasics.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import learn.jms.jmsbasics.config.JMSConfig;
import learn.jms.jmsbasics.model.MyMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class Sender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    // @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        System.out.println("I'm sending a message");

        MyMessage message = MyMessage.builder()
                .id(UUID.randomUUID())
                .message("Hey there")
                .build();

        jmsTemplate.convertAndSend(JMSConfig.MY_QUEUE, message);

        System.out.println("message sent");
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {
        MyMessage message = MyMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello, send a reply")
                .build();

        Message receivedMessage = jmsTemplate.sendAndReceive(JMSConfig.MY_SEND_RCV_QUEUE, session -> {
            try {
                Message helloMsg = session.createTextMessage(objectMapper.writeValueAsString(message));
                helloMsg.setStringProperty("_type", "learn.jms.jmsbasics.model.MyMessage");
                System.out.println("sending hello message");
                return helloMsg;
            } catch (JsonProcessingException e) {
                throw new JMSException("error occurred!");
            }
        });

        assert receivedMessage != null;
        System.out.println(receivedMessage.getBody(String.class));
    }

}
