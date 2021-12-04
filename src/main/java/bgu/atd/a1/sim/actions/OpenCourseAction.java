package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.massages.AddCourseMassage;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.ArrayList;
import java.util.List;

public class OpenCourseAction extends Action<String> {

    String department;
    String course;
    Integer space;
    String[] prerequisites;

    public OpenCourseAction(String department, String course, Integer space, String[] prerequisites) {
        this.department = department;
        this.course = course;
        this.space = space;
        this.prerequisites = prerequisites;
        setActionName("Open Course");
    }

    public String[] getPrerequisites() {
        return prerequisites;
    }

    public Integer getSpace() {
        return space;
    }

    public String getCourse() {
        return course;
    }

    public String getDepartment() {
        return department;
    }

    public void start() {
        List<Action<Boolean>> actions = new ArrayList<>();
        Action addCourse = new AddCourseMassage(space, prerequisites);
        actions.add(addCourse);
        then(actions, ()->{
            if(actions.get(0).getResult().get()) {
                complete("Course " + course + " was added to " + department + " department successfully");

            }
            else
                complete("Course " + course + " was NOT added to " + department + " department");
        });
        sendMessage(addCourse, course, new CoursePrivateState());
    }
}
