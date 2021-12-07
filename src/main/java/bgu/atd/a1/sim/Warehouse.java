package bgu.atd.a1.sim;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * represents a warehouse that holds a finite amount of computers
 * and their suspended mutexes.
 * releasing and acquiring should be blocking free.
 */

public class Warehouse {

    private final Map<Computer, AtomicBoolean> computerMap;

    public Warehouse(Map<Computer, AtomicBoolean> computerMap) {
        this.computerMap = computerMap;
    }

    public Computer getComputer(String computerType){
        for(Computer computer : computerMap.keySet()){
            if(computer.computerType.equals(computerType)){
                return computer;
            }
        }
        return null;
    }

    public void acquire(String computerType) {
        Computer computer = getComputer(computerType);
        if(computer != null)
            while (!computerMap.get(computer).compareAndSet(false,true));
    }

    public void release(String computerType){
        Computer computer = getComputer(computerType);
        if(computer != null)
            computerMap.get(computer).set(false);
    }
}

