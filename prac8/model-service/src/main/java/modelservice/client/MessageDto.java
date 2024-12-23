package modelservice.client;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MessageDto {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private String model;
}
