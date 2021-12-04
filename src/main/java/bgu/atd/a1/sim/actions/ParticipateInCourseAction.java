package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.massages.AddCourseMassage;
import bgu.atd.a1.sim.massages.ParticipateMassage;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class ParticipateInCourseAction extends Action<String> {

    String student;
    String course;
    Integer[] grades;

    public ParticipateInCourseAction(String student, String course, Integer[] grades) {
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

    public Integer[] getGrades() {
        return grades;
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        if(coursePrivateState.getAvailableSpots() > 0) {
            List<Action<Boolean>> actions = new ArrayList<>();
            Action participateMassage = new ParticipateMassage(student);
            actions.add(participateMassage);
            then(actions, () -> {
                if (actions.get(0).getResult().get()) {
                    complete("Student " + student + " is participating course " + course + " successfully.");
                    coursePrivateState.setAvailableSpots(coursePrivateState.getAvailableSpots() - 1);
                    coursePrivateState.addRecord(getActionName());
                } else
                    complete("Failed to add student " + student + " to participate course " + course + ".");
            });
            sendMessage(participateMassage, course, new StudentPrivateState());
        }
        else{
            complete("No room available for Student " + student + " to participate course " + course + ".");
        }
    }
}
