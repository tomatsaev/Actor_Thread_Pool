package bgu.atd.a1.sim.actions.messages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;

public class ComputerPromise extends Action<Computer> {

    String computerType;

    public ComputerPromise(String computerType) {
        this.computerType = computerType;
    }

    @Override
    protected void start() {
        if(pool.warehouse.acquire(computerType)){
            complete(pool.warehouse.getComputer(computerType));
        }
        else{
            sendMessage(this, actorID, pool.getPrivateState(actorID));
        }
    }
}
