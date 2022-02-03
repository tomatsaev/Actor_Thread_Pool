package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ParticipateMessage extends Action<Boolean> {

    String student;
    String course;
    String[] grades;
    List<String> prerequisites;

    public ParticipateMessage(String student, String course, String[] grades, List<String> prerequisites) {
        this.student = student;
        this.course = course;
        this.grades = grades;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() {
        StudentPrivateState studentPrivateState = (StudentPrivateState) pool.getPrivateState(actorID);
        if (!studentPrivateState.getCourses().containsAll(prerequisites) || studentPrivateState.getCourses().contains(course)) {
            complete(false);
            return;
        }
        if (!Objects.equals(grades[0], "-"))
            studentPrivateState.getGrades().put(course, Integer.valueOf(grades[0]));
        else
            studentPrivateState.getGrades().put(course, -1);
        complete(true);
    }
}
