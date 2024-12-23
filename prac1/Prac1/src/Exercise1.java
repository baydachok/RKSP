import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class Exercise1 {

    // Баженов А.В. ИКБО-01-21

    public static List<Integer> generateArray() {
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int randomNumber = random.nextInt(1, 100000 + 1);
            list.add(randomNumber);
        }
        return list;
    }

    public static long findSum(List<Integer> list) throws InterruptedException {
        long sum = 0;
        for (int number : list) {
            sum += number;
            Thread.sleep(1);
        }
        return sum;
    }

    public static long findSumMultithread(List<Integer> list) throws InterruptedException, ExecutionException {
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Callable<Long>> tasks = new ArrayList<>();
        int batchSize = list.size() / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int startIndex = i * batchSize;
            int endIndex = (i + 2) * batchSize > list.size() ? list.size() : (i + 1) * batchSize;
            tasks.add(() -> findSumInRange(list, startIndex, endIndex));
        }
        List<Future<Long>> futures = executorService.invokeAll(tasks);

        long sum = 0;
        for (Future<Long> future : futures) {
            sum += future.get();
            Thread.sleep(1);
        }
        executorService.shutdown();
        return sum;
    }

    private static long findSumInRange(List<Integer> list, int startIndex, int endIndex) throws InterruptedException {
        long sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            sum += list.get(i);
            Thread.sleep(1);
        }
        return sum;
    }

    public static long findSumFork(List<Integer> list) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FindSumTask task = new FindSumTask(list, 0, list.size());
        return forkJoinPool.invoke(task);
    }

    static class FindSumTask extends RecursiveTask<Long> {
        private final List<Integer> list;
        private final int startIndex;
        private final int endIndex;

        FindSumTask(List<Integer> list, int startIndex, int endIndex) {
            this.list = list;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        protected Long compute() {
            if (endIndex - startIndex <= 256) {
              try {
                return findSumInRange(list, startIndex, endIndex);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            }
            int middle = (startIndex + endIndex) / 2;
            FindSumTask leftTask = new FindSumTask(list, startIndex, middle);
            FindSumTask rightTask = new FindSumTask(list, middle, endIndex);

            long result = ForkJoinTask
                .invokeAll(List.of(leftTask, rightTask))
                .stream()
                .mapToLong(ForkJoinTask::join)
                .sum();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return result;
        }

        public static void printResultInfo(long startTime, long endTime, long result, String realizationType) {
            StringBuilder sb = new StringBuilder();
            long durationInMilliseconds = (endTime - startTime) / 1_000_000;
            System.out.println(realizationType + ", время в мс: " + durationInMilliseconds + ". Результат - " + result);

            long maxMemory = Runtime.getRuntime().maxMemory();
            long allocatedMemory = Runtime.getRuntime().totalMemory();
            long freeMemory = Runtime.getRuntime().freeMemory();
            sb.append("Свободная память в Мб: ").append((freeMemory + (maxMemory - allocatedMemory)) / (1024 * 1024));
            System.out.println(sb);
        }


        public static void main(String[] args) throws InterruptedException, ExecutionException {
            List<Integer> testList = generateArray();

            long startTime = System.nanoTime();
            long result = findSum(testList);
            long endTime = System.nanoTime();
            printResultInfo(startTime, endTime, result, "Последовательная реализация");

            startTime = System.nanoTime();
            result = findSumMultithread(testList);
            endTime = System.nanoTime();
            printResultInfo(startTime, endTime, result, "Многопоточная реализация");

            startTime = System.nanoTime();
            result = findSumFork(testList);
            endTime = System.nanoTime();
            printResultInfo(startTime, endTime, result, "ForkJoinPool реализация");
        }
    }
}