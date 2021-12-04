package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.messages.UnregisterMessage;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.ArrayList;
import java.util.List;

public class UnregisterAction extends Action<String> {
    String student;
    String course;

    public UnregisterAction(String student, String course) {
        this.student = student;
        this.course = course;
        setActionName("Unregister");
    }

    public String getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        if (coursePrivateState.getRegStudents().contains(student)) {
            List<Action<Boolean>> actions = new ArrayList<>();
            Action unregisterMessage = new UnregisterMessage(course);
            actions.add(unregisterMessage);
            then(actions, () -> {
                coursePrivateState.getRegStudents().remove(student);
                coursePrivateState.setRegistered(coursePrivateState.getRegistered() - 1);
                coursePrivateState.setAvailableSpots(coursePrivateState.getAvailableSpots() + 1);
                coursePrivateState.addRecord(getActionName());
                complete("Unregister student "+student+ " from course "+ course+ " successfully.");
            });
        } else {
            complete("Unregister " + student + " is unnecessary because they are not register.");
        }
    }
}
