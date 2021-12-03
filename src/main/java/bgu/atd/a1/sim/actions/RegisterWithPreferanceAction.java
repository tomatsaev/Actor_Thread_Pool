package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

public class RegisterWithPreferanceAction extends Action<String> {
    Integer student;
    String[] courses;

    public RegisterWithPreferanceAction(Integer student, String[] courses) {
        this.student = student;
        this.courses = courses;
    }

    @Override
    protected void start() {

    }
}
