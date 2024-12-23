import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Exercise2 {

  private static final String EXIT_COMMAND = "exit";
  private static Integer requestNumber = 1;

  public static void main(String[] args) {
    int availableProcessorsCount = Runtime.getRuntime().availableProcessors();
    ExecutorService executorService = Executors.newFixedThreadPool(availableProcessorsCount);
    try (Scanner scanner = new Scanner(System.in)) {
      System.out.println("Введите число или 'exit', чтобы выйти");
      while (true) {
        String userInput = scanner.nextLine();
        if (EXIT_COMMAND.equals(userInput)) {
          break;
        }

        try {
          int number = Integer.parseInt(userInput);
          int result = executorService.submit(() -> calculateSquare(number)).get();
          System.out.printf("(%s) - Результат: %d\n", requestNumber, result);
          requestNumber++;
        } catch (NumberFormatException e) {
          System.out.println("Ошибка ввода, введите валидное число");
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
          throw new RuntimeException(e);
        }
      }
    }
    executorService.shutdown();
  }

  private static Integer calculateSquare(int number) {
    int delayInSeconds = ThreadLocalRandom.current().nextInt(5) + 1;
    try {
      Thread.sleep(delayInSeconds * 1000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return number * number;
  }
}
