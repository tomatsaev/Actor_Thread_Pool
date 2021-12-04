package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.messages.ParticipateMessage;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class ParticipateInCourseAction extends Action<String> {

    String student;
    String course;
    String[] grades;

    public ParticipateInCourseAction(String student, String course, String[] grades) {
        this.student = student;
        this.course = course;
        this.grades = grades;
        setActionName("Participate In Course");
    }

    public String getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }

    public String[] getGrades() {
        return grades;
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        if(coursePrivateState.getAvailableSpots() > 0) {
            List<Action<Boolean>> actions = new ArrayList<>();
            Action participateMassage = new ParticipateMessage(student, course, grades);
            actions.add(participateMassage);
            then(actions, () -> {
                coursePrivateState.setAvailableSpots(coursePrivateState.getAvailableSpots() - 1);
                complete("Student " + student + " is participating course " + course + " successfully.");
                coursePrivateState.addRecord(getActionName());
//                 else
//                    complete("Failed to add student " + student + " to participate course " + course + ".");
            });
            sendMessage(participateMassage, course, new StudentPrivateState());
        }
        else{
            complete("No room available for Student " + student + " to participate course " + course + ".");
        }
    }
}
