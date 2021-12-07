package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class UnregisterMessage extends Action<Boolean> {
    String course;

    public UnregisterMessage(String course) {
        this.course = course;
        setActionName("Unregister Message");
    }

    @Override
    protected void start() {
        ((StudentPrivateState)pool.getPrivateState(actorID)).getGrades().remove(course);
        pool.getPrivateState(actorID).addRecord(getActionName());
        complete(true);
    }
}
