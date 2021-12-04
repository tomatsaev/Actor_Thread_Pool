package bgu.atd.a1.sim.messages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class RemoveCourseGradeMessage extends Action<Boolean> {
    String course;

    public RemoveCourseGradeMessage(String course) {
        this.course = course;
        setActionName("Remove Course Grade Message");
    }

    @Override
    protected void start() {
        StudentPrivateState studentPrivateState = (StudentPrivateState) pool.getPrivateState(actorID);
        if (studentPrivateState.getGrades().remove(course) != null) {
            complete(true);
            System.out.println("student " + actorID + " removed "+ course + " course");
        }
        else throw new IllegalStateException("student " + actorID + " tried removing not existing course "+ course);
    }
}
