package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.messages.CloseCourseMessage;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class CloseCourseAction extends Action<String> {
    String department;
    String course;

    public CloseCourseAction(String department, String course) {
        this.department = department;
        this.course = course;
        setActionName("Close Course");
    }

    @Override
    protected void start() {
        DepartmentPrivateState privateState = (DepartmentPrivateState) pool.getPrivateState(actorID);

        if (!privateState.getCourseList().contains(course)) {
            complete("Course " + course + " does NOT exist in " + department + " department");
            return;
        }

        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> closeCourse = new CloseCourseMessage(department, course);
        actions.add(closeCourse);
        then(actions, ()->{
            if(actions.get(0).getResult().get()) {
                complete("Course " + course + " was removed from " + department + " department successfully");
                pool.getPrivateState(actorID).addRecord(getActionName());
            }
            else
                complete("Course " + course + " was NOT removed from " + department + " department");
        });
        sendMessage(closeCourse, course, new CoursePrivateState());

        privateState.getCourseList().remove(course);
    }
}
