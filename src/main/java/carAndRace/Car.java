package carAndRace;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;
    private static boolean winnerFound = false;
    private static Lock win = new ReentrantLock();

    private CyclicBarrier cbr;
    private CountDownLatch cdl;

    static {
        CARS_COUNT = 0;
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier cbr, CountDownLatch cdl) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cbr = cbr;
        this.cdl = cdl;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            cbr.await();
            cbr.await();
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }
            checkWin(this);

            cbr.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static synchronized void checkWin(Car car) {
        if (!winnerFound) {
            System.out.println(car.name + ">>> WIN");
            winnerFound = true;
        }
    }
}
