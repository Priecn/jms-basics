package learn.jms.jmsbasics.listener;

import learn.jms.jmsbasics.config.JMSConfig;
import learn.jms.jmsbasics.model.MyMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MyListener {

    private final JmsTemplate jmsTemplate;

    // @JmsListener(destination = JMSConfig.MY_QUEUE)
    public void listen(@Payload MyMessage myMessage,
                       @Headers MessageHeaders headers,
                       Message message) {

        System.out.println("I got a message!");
        System.out.println(myMessage);
        System.out.println(headers);
        System.out.println(message);
    }


    @JmsListener(destination = JMSConfig.MY_SEND_RCV_QUEUE)
    public void listenAndReply(@Payload MyMessage myMessage,
                       @Headers MessageHeaders headers,
                       Message message) throws JMSException {

        MyMessage myMessage1 = MyMessage.builder()
                .id(UUID.randomUUID())
                .message("reply message")
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), myMessage1);
    }
}
