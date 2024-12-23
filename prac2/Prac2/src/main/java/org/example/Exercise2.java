package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;

public class Exercise2 {

  public static void main(String[] args) {
    String srcFilename = "exercise2/from/test.txt";
    createFileWithSize(srcFilename, 1024 * 1024 * 100);

    printExecInfo(() -> copyFileByFisFos(srcFilename, "exercise2/to/FisFos_copy.txt"), "FileInputStream - FileOutputStream");
    System.gc();
    printExecInfo(() -> copyFileByChannel(srcFilename, "exercise2/to/Channel_copy.txt"), "FileChannel");
    System.gc();
    printExecInfo(() -> copyFileByApacheCommons(srcFilename, "exercise2/to/ApacheIO_copy.txt"), "ApacheIO");
    System.gc();
    printExecInfo(() -> copyFileByFilesClass(srcFilename, "exercise2/to/FilesClass_copy.txt"), "Files");
  }

  public static void createFileWithSize(String filename, long bytesCount) {
    try (RandomAccessFile f = new RandomAccessFile(filename, "rw")) {
      f.setLength(bytesCount);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void copyFileByFisFos(String src, String dst) {
    try (InputStream inputStream = new FileInputStream(src);
         OutputStream outputStream = new FileOutputStream(dst)) {
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, length);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void copyFileByChannel(String src, String dst) {
    long endFreeMemory = 0;
    try(FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dst);
        FileChannel srcFileChannel  = fis.getChannel();
        FileChannel dstFileChannel = fos.getChannel()) {
      srcFileChannel.transferTo(srcFileChannel.position(), srcFileChannel.size(), dstFileChannel);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void copyFileByApacheCommons(String src, String dst) {
    try {
      FileUtils.copyFile(new File(src), new File(dst) );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void copyFileByFilesClass(String src, String dst) {
    try {
      Files.copy(Path.of(src), Path.of(dst), REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void printExecInfo(Runnable runnable, String realizationType) {
    long startTime = System.nanoTime();
    runnable.run();
    long endTime = System.nanoTime();
    long totalFreeMemory = findFreeMemory();

    long durationInMilliseconds = (endTime - startTime) / 1_000_000;
    System.out.printf("%s, время в мс: %d\n", realizationType, durationInMilliseconds);

    System.out.printf("Свободная память в Кб: %d\n\n", totalFreeMemory);
  }

  public static long findFreeMemory() {
    Runtime runtime = Runtime.getRuntime();
    long maxMemory = runtime.maxMemory();
    long allocatedMemory = runtime.totalMemory();
    long freeMemory = runtime.freeMemory();
    return (freeMemory + (maxMemory - allocatedMemory)) / 1024;
  }

}