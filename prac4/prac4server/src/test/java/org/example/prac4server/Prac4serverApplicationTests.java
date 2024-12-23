package org.example.prac4server;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {Prac4serverApplicationTests.Initializer.class})
@TestPropertySource(properties = {"spring.config.location=classpath:application.properties"})
public class Prac4serverApplicationTests {

  @Container
  public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.2-alpine")
      .withDatabaseName("postgres")
      .withUsername("postgres")
      .withPassword("postgres");

  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      TestPropertyValues.of(
          "CONTAINER.USERNAME=" + postgreSQLContainer.getUsername(),
          "CONTAINER.PASSWORD=" + postgreSQLContainer.getPassword(),
          "CONTAINER.URL=" + postgreSQLContainer.getJdbcUrl()
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }

}
