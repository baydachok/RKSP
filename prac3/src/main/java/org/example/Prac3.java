package org.example;

import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Prac3 {
  record UserFriend(int userId, int friendId) {}
  private static final List<UserFriend> userFriends = createRandomFriends();

  public static void main(String[] args) {
    System.out.printf("UserFriends: %s%n", userFriends);

    var userIds = createRandomUserIds();
    System.out.printf("UserIds: %s%n", userIds);

    var userIdStream = Observable.fromIterable(userIds);
    var userFriendStream = userIdStream.flatMap(Prac3::getFriends).toList();

    userFriendStream.subscribe(value -> System.out.printf("Result: %s%n", value));
  }

  private static Observable<UserFriend> getFriends(int userId) {
    return Observable
        .fromIterable(
            userFriends
                .stream()
                .filter(userFriend -> userFriend.userId == userId)
                .toList()
        );
  }

  private static List<Integer> createRandomUserIds() {
    return IntStream
        .range(1, 10 + 1)
        .filter(userId -> ThreadLocalRandom.current().nextInt() % 3 == 0)
        .boxed()
        .toList();
  }

  private static List<UserFriend> createRandomFriends() {
    return IntStream
        .range(1, 10 + 1)
        .boxed()
        .flatMap(userId -> IntStream
          .range(1, 10 + 1)
          .filter(friendId -> friendId != userId)
          .filter(friendId -> ThreadLocalRandom.current().nextInt() % 4 == 0)
          .mapToObj(friendId -> new UserFriend(userId, friendId)))
        .toList();
  }
}
