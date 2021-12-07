package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.ArrayList;
import java.util.List;

public class RegisterWithPreferenceMessage extends Action<Boolean> {

    String student;
    String course;
    List<String> prerequisites;

    public RegisterWithPreferenceMessage(String student, String course, List<String> prerequisites) {
        this.student = student;
        this.course = course;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        List<String> coursePrerequisites = coursePrivateState.getPrerequisites();
        if(coursePrivateState.getAvailableSpots() > 0) {
            for (String pre : coursePrerequisites) {
                if (!this.prerequisites.contains(pre)) {
                    complete(false);
                    return;
                }
            }
            coursePrivateState.addStudent(student);

            complete(true);
            coursePrivateState.addRecord(getActionName());
        }
    }

}
