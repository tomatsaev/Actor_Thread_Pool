package bgu.atd.a1.sim.actions.messages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.List;

public class MeetsObligationsMessage extends Action<Boolean> {

    List<String> conditions;
    String computerType;

    public MeetsObligationsMessage(List<String> conditions, String computerType) {
        this.conditions = conditions;
        this.computerType = computerType;
        setActionName("Meet Obligations Message");
    }

    @Override
    protected void start() {
        StudentPrivateState studentPrivateState = (StudentPrivateState) pool.getPrivateState(actorID);
        long sig = pool.warehouse.getComputer(computerType).checkAndSign(conditions, studentPrivateState.getGrades());
        studentPrivateState.setSignature(sig);
    }
}
