package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

public class AddStudentAction extends Action<String> {
    String department;
    String student;

    public AddStudentAction(String department, String student) {
        this.department = department;
        this.student = student;
        setActionName("Add Student");
    }

    public String getDepartment() {
        return department;
    }

    public String getStudent() {
        return student;
    }

    @Override
    protected void start() {

    }
}
