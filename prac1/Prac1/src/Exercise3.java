import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Exercise3 {

  public static void main(String[] args) {
    BlockingQueue<File> blockingQueue = new ArrayBlockingQueue<>(5);
    FileProcessor xmlProcessor = new FileProcessor(blockingQueue, FileType.XML);
    FileProcessor jsonProcessor = new FileProcessor(blockingQueue, FileType.JSON);
    FileProcessor xlsProcessor = new FileProcessor(blockingQueue, FileType.XLS);
    FileGenerator fileGenerator = new FileGenerator(blockingQueue);
    new Thread(fileGenerator::generateFile).start();
    new Thread(xmlProcessor::processFile).start();
    new Thread(jsonProcessor::processFile).start();
    new Thread(xlsProcessor::processFile).start();
  }

  private record FileProcessor(BlockingQueue<File> blockingQueue, FileType processorType) {

    public void processFile() {
        while (true) {
          var file = blockingQueue.peek();
          if (file != null && processorType.equals(file.type)) {
            try {
              file = blockingQueue.take();
              Thread.sleep(file.size() * 7L);
              System.out.printf("Processed %s\n", file);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          }
        }
      }

    }

  private record FileGenerator(BlockingQueue<File> blockingQueue) {
    private static final List<FileType> AVAILABLE_FILE_TYPES = List.of(
        FileType.XML,
        FileType.JSON,
        FileType.XLS
    );

    public void generateFile() {
        while (true) {
          try {
            int delay = ThreadLocalRandom.current().nextInt(100, 1000 + 1);
            int size = ThreadLocalRandom.current().nextInt(10, 100 + 1);
            FileType fileType = randomFileType();
            Thread.sleep(delay);
            var file = new File(size, fileType);
            blockingQueue.put(file);
            System.out.printf("Generated %s\n", file);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      }

      private FileType randomFileType() {
        return AVAILABLE_FILE_TYPES.get(
            ThreadLocalRandom.current().nextInt(AVAILABLE_FILE_TYPES.size())
        );
      }

    }

  private record File(int size, FileType type) {
    @Override
    public String toString() {
      return "File{" +
          "size=" + size +
          ", type=" + type +
          '}';
    }
  }

  private enum FileType {
    XML, JSON, XLS
  }

}
