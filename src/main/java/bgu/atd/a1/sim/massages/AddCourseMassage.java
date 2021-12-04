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
        setActionName("Add Course Massage");
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        coursePrivateState.setAvailableSpots(space);
        coursePrivateState.setPrerequisites(prerequisites);

        complete(true);
        coursePrivateState.addRecord(getActionName());
    }
}
