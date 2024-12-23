package org.example.prac4client.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;
import reactor.util.retry.Retry;

@Configuration
public class ClientConfiguration {

  @Bean
  public RSocketRequester getRSocketRequester(RSocketRequester.Builder builder) {
    return builder
        .rsocketStrategies(
            RSocketStrategies.builder()
                .encoder(new Jackson2JsonEncoder())
                .decoder(new Jackson2JsonDecoder())
                .build()
        )
        .rsocketConnector(connector -> connector
            .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
        .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
        .tcp("localhost", 5200);
  }

}
