package bgu.atd.a1.sim.massages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.List;

public class AddCourseMassage extends Action<Boolean> {

    Integer space;
    List<String> prerequisites;

    public AddCourseMassage(Integer space, List<String> prerequisites) {
        this.space = space;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() {
        complete(true);
        pool.getPrivateState(actorID).addRecord(getActionName());
        ((CoursePrivateState)pool.getPrivateState(actorID)).setAvailableSpots(space);
        ((CoursePrivateState)pool.getPrivateState(actorID)).setPrerequisites(prerequisites);
    }
}
