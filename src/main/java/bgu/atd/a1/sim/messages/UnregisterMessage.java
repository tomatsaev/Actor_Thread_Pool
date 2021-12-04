package bgu.atd.a1.sim.messages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class UnregisterMessage extends Action<Boolean> {
    String course;

    public UnregisterMessage(String course) {
        this.course = course;
        setActionName("Unregister Massage");
    }

    @Override
    protected void start() {
        ((StudentPrivateState)pool.getPrivateState(actorID)).getGrades().remove(course);
        pool.getPrivateState(actorID).addRecord(getActionName());
        complete(true);
    }
}
