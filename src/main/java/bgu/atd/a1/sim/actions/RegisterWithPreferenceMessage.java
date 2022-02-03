package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class RegisterWithPreferenceMessage extends Action<Boolean> {

    String student;
    String course;
    String[] grades;

    public RegisterWithPreferenceMessage(String student, String course, String[] grade) {
        this.student = student;
        this.course = course;
        this.grades = grade;
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> participateMessage = new ParticipateMessage(student, course, grades, coursePrivateState.getPrerequisites());
        actions.add(participateMessage);
        then(actions, () -> {
            if (participateMessage.getResult().get() && coursePrivateState.getAvailableSpots() > 0) {
                coursePrivateState.addStudent(student);
                complete(true);
            } else {
                complete(false);
                if (coursePrivateState.getAvailableSpots() > 0)
                    System.out.println("No room available for Student " + student + " to participate course " + course + " course.");
                else
                    System.out.println("Student " + student + " doesnt meet prerequisites to participate at " + course + " course.");
            }
        });
        sendMessage(participateMessage, student, new StudentPrivateState());
    }
}
