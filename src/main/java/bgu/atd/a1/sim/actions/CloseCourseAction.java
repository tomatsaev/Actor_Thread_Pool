package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

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

    }
}
