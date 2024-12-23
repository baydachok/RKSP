package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Exercise4 {

  private static final Map<Path, List<String>> fileToLines = new HashMap<>();
  private static final Map<Path, Short> fileToChecksum = new HashMap<>();
  private static final String DIRECTORY = "exercise4";

  public static void main(String[] args) throws IOException, InterruptedException {
    firstObserve();

    WatchService watchService = FileSystems.getDefault().newWatchService();
    Path path = Paths.get(DIRECTORY);
    path.register(
        watchService,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_MODIFY,
        StandardWatchEventKinds.ENTRY_DELETE
    );

    WatchKey key;
    while ((key = watchService.take()) != null) {
      for (WatchEvent<?> event : key.pollEvents()) {
        switch (event.kind().name()) {
          case "ENTRY_CREATE" -> onCreate(event);
          case "ENTRY_MODIFY" -> onModify(event);
          case "ENTRY_DELETE" -> onDelete(event);
        }
      }
      key.reset();
    }
  }

  public static void onCreate(WatchEvent<?> event) {
    Path filePath = getPath(event);
    fileToLines.put(filePath, readLinesFromFile(filePath));
    fileToChecksum.put(filePath, Exercise3.findChecksum(filePath.toString()));
    System.out.printf("Создан новый файл: %s\n", event.context());
  }

  public static void onModify(WatchEvent<?> event) {
    detectFileChanges(getPath(event));
  }

  public static void onDelete(WatchEvent<?> event) {
    Path filePath = getPath(event);
    Short checksum = fileToChecksum.get(filePath);
    System.out.printf("Файл %s c чек-суммой %x был удален\n", event.context(), checksum);

    fileToLines.remove(filePath);
    fileToChecksum.remove(filePath);
  }

  private static void detectFileChanges(Path filePath) {
    List<String> newFileContents = readLinesFromFile(filePath);
    List<String> oldFileContents = fileToLines.get(filePath);
    if (oldFileContents != null) {
      List<String> addedLines = newFileContents
          .stream()
          .filter(line -> !oldFileContents.contains(line))
          .toList();
      List<String> deletedLines = oldFileContents
          .stream()
          .filter(line -> !newFileContents.contains(line))
          .toList();
      if (!addedLines.isEmpty()) {
        System.out.println("Добавленные строки в файле " + filePath + ":");
        addedLines.forEach(line -> System.out.println("+ " + line));
      }
      if (!deletedLines.isEmpty()) {
        System.out.println("Удаленные строки из файла " + filePath + ":");
        deletedLines.forEach(line -> System.out.println("- " + line));
      }
    }
    fileToLines.put(filePath, newFileContents);
    fileToChecksum.put(filePath, Exercise3.findChecksum(filePath.toString()));
  }

  private static List<String> readLinesFromFile(Path filePath) {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return lines;
  }

  private static void firstObserve() {
    File dir = new File(DIRECTORY);
    Arrays.stream(Objects.requireNonNull(dir.listFiles()))
        .map(File::toPath)
        .forEach(path -> {
          List<String> lines = readLinesFromFile(path);
          fileToLines.put(path, lines);
          fileToChecksum.put(path, Exercise3.findChecksum(path.toString()));
        });
  }

  private static Path getPath(WatchEvent<?> event) {
    return Path.of("%s/%s".formatted(DIRECTORY, event.context()));
  }

}
