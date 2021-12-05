package bgu.atd.a1.sim.messages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.ArrayList;
import java.util.List;

public class RegisterWithPreferenceMessage extends Action<Boolean> {
    String course;
    List<String> prerequisites;

    public RegisterWithPreferenceMessage(String course, List<String> prerequisites) {
        this.course = course;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        List<String> coursePrerequisites = coursePrivateState.getPrerequisites();
        if(coursePrivateState.getAvailableSpots() > 0) {
            for (String pre : coursePrerequisites) {
                if (!this.prerequisites.contains(pre))
                    complete(false);
            }
            coursePrivateState.setAvailableSpots(coursePrivateState.getAvailableSpots() - 1);
            complete(true);
            coursePrivateState.addRecord(getActionName());
        }
    }

}
