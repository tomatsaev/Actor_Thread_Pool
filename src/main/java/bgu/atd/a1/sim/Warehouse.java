package bgu.atd.a1.sim;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * represents a warehouse that holds a finite amount of computers
 * and their suspended mutexes.
 * releasing and acquiring should be blocking free.
 */
public class Warehouse {

    private Map<Computer, Lock> computerMap = new ConcurrentHashMap<Computer, Lock>();

    public Warehouse(Map<Computer, Lock> computerMap) {
        this.computerMap = computerMap;
    }

    public void addComputer(Computer computer){
        computerMap.put(computer, new ReentrantLock());
    }

    public Computer getComputer(String computerType){
        for(Computer computer : computerMap.keySet()){
            if(computer.computerType.equals(computerType)){
                return computer;
            }
        }
        return null;
    }

    public boolean acquire(String computerType){
        Computer computer = getComputer(computerType);
        if(computer != null)
            return computerMap.get(computer).tryLock();
        return false;
    }

    public void release(String computerType){
        Computer computer = getComputer(computerType);
        if(computer != null)
            computerMap.get(computer).unlock();
    }
}
