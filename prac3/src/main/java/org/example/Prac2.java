package org.example;

import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Prac2 {

  static List<Integer> createRandomList(int size, int upInclusiveBound) {
    return IntStream
        .range(0, size)
        .map(index -> ThreadLocalRandom.current().nextInt(upInclusiveBound + 1))
        .boxed()
        .toList();
  }

  static void task1() {
    var list = createRandomList(1000, 1000);
    Observable
        .fromIterable(list)
        .filter(value -> value > 500)
        .toList()
        .subscribe(value -> System.out.printf("Task1 result is %s%n", value));
  }

  static void task2() {
    var observable1 = Observable.fromIterable(createRandomList(1000, 65536));
    var observable2 = Observable.fromIterable(createRandomList(1000, 65536));
    Observable
        .concat(observable1, observable2)
        .toList()
        .subscribe(value -> System.out.printf("Task2 result is %s%n", value));
  }

  static void task3() {
    var list = createRandomList(10, 65536);
    Observable
        .fromIterable(list)
        .take(5)
        .toList()
        .subscribe(value -> System.out.printf("Task3 result is %s%n", value));
  }

  public static void main(String[] args) {
    task1();
    task2();
    task3();
  }

}
