package bgu.atd.a1.bank;

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;

import java.util.concurrent.CountDownLatch;

public class bank {
    public static void main(String[] args) throws InterruptedException {
        ActorThreadPool pool = new ActorThreadPool (8);
        Action<String> trans = new Transmission(100, "A","B", "bank2","bank1");
        pool.start();
        pool.submit(trans, "bank1", new BankStates());
        CountDownLatch l = new CountDownLatch(1);
        trans.getResult().subscribe(l::countDown);
        l.await();
        pool.shutdown();
    }
}
