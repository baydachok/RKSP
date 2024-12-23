package org.example;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.concurrent.ThreadLocalRandom;

public class Prac4 {

  record File(String fileType, int fileSize) {}

  static class FileGenerator {
    public Observable<File> generateFile() {
      return Observable
          .fromCallable(() -> {
            try {
              String[] fileTypes = {"XML", "JSON", "XLS"};
              String fileType = fileTypes[ThreadLocalRandom.current().nextInt(3)];
              int fileSize = ThreadLocalRandom.current().nextInt(10, 100 + 1);
              Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1000 + 1));
              File file = new File(fileType, fileSize);
              System.out.printf("File %s generated%n", file);
              return file;
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
            return new File("", 1);
          })
          .repeat()
          .subscribeOn(Schedulers.io())
          .observeOn(Schedulers.io());
    }
  }

  static class FileQueue {
    private final Observable<File> fileObservable;

    public FileQueue(int capacity) {
      this.fileObservable = new FileGenerator()
          .generateFile()
          .replay(capacity)
          .autoConnect(3);
    }
// Разработка клиент-серверного приложения по авто-настройке AI-чатботов
    // opennnn
    public Observable<File> getFileObservable() {
      return fileObservable;
    }
  }

  static class FileProcessor {
    private final String supportedFileType;

    public FileProcessor(String supportedFileType) {
      this.supportedFileType = supportedFileType;
    }

    public Completable processFiles(Observable<File> fileObservable) {
      return fileObservable
          .filter(file -> file.fileType().equals(supportedFileType))
          .flatMapCompletable(file -> {
            long processingTime = file.fileSize() * 7L;
            return Completable
                .fromAction(() -> {
                  Thread.sleep(processingTime);
                  System.out.println("Processed " +
                      supportedFileType + " file with size " + file.fileSize());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
          })
          .onErrorComplete();
    }
  }

  public static void main(String[] args) {
    int queueCapacity = 5;
    FileQueue fileQueue = new FileQueue(queueCapacity);
    String[] supportedFileTypes = {"XML", "JSON", "XLS"};
    for (String fileType : supportedFileTypes) {
      new FileProcessor(fileType)
          .processFiles(fileQueue.getFileObservable())
          .subscribe(
              () -> {},
              throwable -> System.err.println("Error processing file: " + throwable)
          );
    }
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
