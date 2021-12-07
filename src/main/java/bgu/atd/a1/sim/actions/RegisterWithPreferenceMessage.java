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
        if(coursePrivateState.getAvailableSpots() > 0) {
            List<Action<Boolean>> actions = new ArrayList<>();
            Action<Boolean> participateMessage = new ParticipateMessage(student, course, grades, coursePrivateState.getPrerequisites());
            actions.add(participateMessage);
            coursePrivateState.addStudent(student);
            then(actions, () -> {
                if (participateMessage.getResult().get()) {
                    complete(true);
                    System.out.println("Student " + student + " is participating course " + course + " successfully, with " + coursePrivateState.getAvailableSpots() + " spots left");
                }
                else {
                    complete(false);
                    System.out.println("No room available for Student " + student + " to participate at " + course + " course.");
                }
            });
            sendMessage(participateMessage, student, new StudentPrivateState());
        }
        else{
            complete(false);
            System.out.println("No room available for Student " + student + " to participate course " + course + " course.");
        }
    }
}
