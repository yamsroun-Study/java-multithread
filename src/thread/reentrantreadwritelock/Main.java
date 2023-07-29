package thread.reentrantreadwritelock;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {

    private static final int HIGHEST_PRICE = 1000;
    private static final Random RANDOM = new Random();

    private static InventoryDatabase inventoryDatabase;

    public static void main(String[] args) throws InterruptedException {
        createInventoryDatabase();
        initDataToInventoryDatabase();
        startWriter();
        startReaders();
    }

    private static void createInventoryDatabase() {
        inventoryDatabase = new InventoryDatabase();
    }

    private static void initDataToInventoryDatabase() {
        for (int i = 0; i < 100000; i++) {
            inventoryDatabase.addItem(RANDOM.nextInt(HIGHEST_PRICE));
        }
    }

    private static void startWriter() {
        Thread writer = new Thread(() -> {
            while (true) {
                inventoryDatabase.addItem(RANDOM.nextInt(HIGHEST_PRICE));
                inventoryDatabase.removeItem(RANDOM.nextInt(HIGHEST_PRICE));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        });
        writer.setDaemon(true);
        writer.start();
    }

    private static void startReaders() throws InterruptedException {
        int numberOfReaderThreads = 7;
        ArrayList<Thread> readers = new ArrayList<>();
        for (int readerIndex = 0; readerIndex < numberOfReaderThreads; readerIndex++) {
            Thread reader = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    int upperBoundPrice = RANDOM.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? RANDOM.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getNumberOfItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });
            reader.setDaemon(true);
            readers.add(reader);
        }

        long startReadingTime = System.currentTimeMillis();
        for (Thread reader: readers) {
            reader.start();
        }
        for (Thread reader: readers) {
            reader.join();
        }
        long endReadingTime = System.currentTimeMillis();
        System.out.printf("Reading took %d ms%n", endReadingTime - startReadingTime);
    }

    static class InventoryDatabase {

        private final TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
        //private final ReentrantLock readLock = new ReentrantLock();
        //private final ReentrantLock writeLock = readLock;
        private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Lock readLock = rwLock.readLock();
        private final Lock writeLock = rwLock.writeLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {
            readLock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.floorKey(upperBound);
                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);
                int sum = 0;
                for (int numberOfItemsForPrice: rangeOfPrices.values()) {
                    sum += numberOfItemsForPrice;
                }
                return sum;
            } finally {
                readLock.unlock();
            }
        }

        public void addItem(int price) {
            writeLock.lock();
            try {
                priceToCountMap.merge(price, 1, Integer::sum);
            } finally {
                writeLock.unlock();
            }
        }

        public void removeItem(int price) {
            writeLock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice - 1);
                }
            } finally {
                writeLock.unlock();
            }
        }

    }

}