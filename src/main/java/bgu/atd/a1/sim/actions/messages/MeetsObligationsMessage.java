package bgu.atd.a1.sim.actions.messages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.List;

public class MeetsObligationsMessage extends Action<Boolean> {

    List<String> conditions;
    Computer computer;

    public MeetsObligationsMessage(List<String> conditions, Computer computer) {
        this.conditions = conditions;
        this.computer = computer;
    }

    @Override
    protected void start() {
        StudentPrivateState studentPrivateState = (StudentPrivateState) pool.getPrivateState(actorID);
        long sig = computer.checkAndSign(conditions, studentPrivateState.getGrades());
        studentPrivateState.setSignature(sig);
    }
}
