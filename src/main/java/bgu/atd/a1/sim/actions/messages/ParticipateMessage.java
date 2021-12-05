package bgu.atd.a1.sim.actions.messages;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.Objects;

public class ParticipateMessage extends Action<Boolean> {

    String student;
    String course;
    String[] grades;

    public ParticipateMessage(String student, String course, String[] grades) {
        this.student = student;
        this.course = course;
        this.grades = grades;
        setActionName("Participate Message");
    }

    @Override
    protected void start() {
        StudentPrivateState studentPrivateState = (StudentPrivateState) pool.getPrivateState(actorID);
        if(!Objects.equals(grades[0], "-"))
            studentPrivateState.getGrades().put(course, Integer.valueOf(grades[0]));
        else
            studentPrivateState.getGrades().put(course, null);

        studentPrivateState.addRecord(getActionName());
        complete(true);
    }
}
