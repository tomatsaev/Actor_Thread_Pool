package bgu.atd.a1.sim.actions.messages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class CloseCourseMessage extends Action<Boolean> {
    String course;

    public CloseCourseMessage(String department, String course) {
        this.course = course;
        setActionName("Close Course Message");
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        List<Action<Boolean>> actions = new ArrayList<>();
        for (String ignored : coursePrivateState.getRegStudents()) {
            Action<Boolean> removeGrade = new RemoveCourseGradeMessage(course);
            actions.add(removeGrade);
        }
        then(actions, () -> {
            coursePrivateState.setAvailableSpots(-1);
            complete(true);
            coursePrivateState.addRecord(getActionName());
        });
        for (int i = 0; i < actions.size(); i++) {
            sendMessage(actions.get(i), coursePrivateState.getRegStudents().get(i), new StudentPrivateState());
        }

    }
}
