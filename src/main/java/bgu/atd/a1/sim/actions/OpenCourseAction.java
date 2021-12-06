package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.messages.AddCourseMessage;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class OpenCourseAction extends Action<String> {

    String department;
    String course;
    Integer space;
    List<String> prerequisites;

    public OpenCourseAction(String department, String course, Integer space, List<String> prerequisites) {
        this.department = department;
        this.course = course;
        this.space = space;
        this.prerequisites = prerequisites;
        setActionName("Open Course");
    }

    public List<String> getPrerequisites() {
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
        DepartmentPrivateState privateState = (DepartmentPrivateState) pool.getPrivateState(actorID);
        List<Action<Boolean>> actions = new ArrayList<>();
        Action addCourse = new AddCourseMessage(space, prerequisites);
        actions.add(addCourse);
        then(actions, ()->{
            if(actions.get(0).getResult().get()) {
                privateState.getCourseList().add(course);
                complete("Course " + course + " was added to " + department + " department successfully");
                System.out.println("Course " + course + " was added to " + department + " department successfully");
                privateState.addRecord(getActionName());
            }
            else {
                complete("Course " + course + " was NOT added to " + department + " department");
                System.out.println("Course " + course + " was NOT added to " + department + " department");
            }
        });
        sendMessage(addCourse, course, new CoursePrivateState());

    }
}
