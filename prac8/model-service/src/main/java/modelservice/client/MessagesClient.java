package modelservice.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "message-service")
public interface MessagesClient {
    @GetMapping("/messages/byModel/{model}")
    List<MessageDto> getMessagesByModel(@PathVariable("model") String model);
}
