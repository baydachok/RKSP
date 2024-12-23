package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Exercise3 {
  public static short findChecksum(String src) {
    try (FileInputStream fileInputStream = new FileInputStream(src);
         FileChannel fileChannel = fileInputStream.getChannel()) {
      ByteBuffer buffer = ByteBuffer.allocate(2);
      short checksum = 0;
      while (fileChannel.read(buffer) != -1) {
        buffer.flip();
        while (buffer.remaining() > 1) {
          checksum ^= buffer.getShort();
        }
        if (buffer.hasRemaining()) {
          checksum ^= buffer.get();
        }
        buffer.clear();
      }
      return checksum;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public static void main(String[] args) {
    String filePath = "exercise3/test.txt";
    short checksum = findChecksum(filePath);
    System.out.printf("Контрольная сумма - %x\n", checksum);
  }
}
