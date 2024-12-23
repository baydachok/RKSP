package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Exercise1 {
  public static void main(String[] args) {
    String fileName = "exercise1/exercise1.txt";
    String text = """
        Текст тестового файла
        Текст тестового файла
        Текст тестового файла""";
    writeFile(fileName, text);
    printFile(fileName);
  }

  private static void writeFile(String fileName, String text) {
    Path filePath = Paths.get(fileName);
    try {
      Files.writeString(filePath, text);
    } catch (IOException ignored) {}
  }

  private static void printFile(String fileName) {
    Path filePath = Paths.get(fileName);
    try {
      List<String> fileLines = Files.readAllLines(filePath);
      for (String line : fileLines) {
        System.out.println(line);
      }
    } catch (IOException ignored) {}
  }
}