package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import sun.java2d.pipe.hw.AccelTypedVolatileImage;

public class UnregisterAction extends Action<String> {
    Integer student;
    String course;

    public UnregisterAction(Integer student, String course) {
        this.student = student;
        this.course = course;
        setActionName("Unregister");
    }

    public Integer getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }

    @Override
    protected void start() {

    }
}
