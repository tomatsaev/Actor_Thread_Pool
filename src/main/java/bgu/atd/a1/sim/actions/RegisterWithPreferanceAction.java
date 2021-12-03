package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

public class RegisterWithPreferanceAction extends Action<String> {
    String student;
    String[] courses;

    public RegisterWithPreferanceAction(String student, String[] courses) {
        this.student = student;
        this.courses = courses;
    }

    public String getStudent() {
        return student;
    }

    public String[] getCourses() {
        return courses;
    }

    @Override
    protected void start() {

    }
}
