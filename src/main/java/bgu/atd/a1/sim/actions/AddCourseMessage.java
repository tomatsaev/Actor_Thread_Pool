package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.List;

public class AddCourseMessage extends Action<Boolean> {

    Integer space;
    List<String> prerequisites;

    public AddCourseMessage(Integer space, List<String> prerequisites) {
        this.space = space;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        coursePrivateState.setAvailableSpots(space);
        coursePrivateState.setPrerequisites(prerequisites);

        complete(true);
    }
}
