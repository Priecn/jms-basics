package learn.jms.jmsbasics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyMessage implements Serializable {
    static final long serialVersionUID = 3152631445632310254L;
    private UUID id;
    private String message;
}
