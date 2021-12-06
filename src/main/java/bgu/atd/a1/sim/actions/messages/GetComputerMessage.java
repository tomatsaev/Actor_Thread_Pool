package bgu.atd.a1.sim.actions.messages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;

public class GetComputerMessage extends Action<Computer> {
    String computerType;

    public GetComputerMessage(String computerType) {
        this.computerType = computerType;
    }

    @Override
    protected void start() {
        pool.warehouse.acquire(computerType);
        complete(pool.warehouse.getComputer(computerType));
    }
}
