package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

public class AddStudentAction extends Action<String> {
    String department;
    Integer student;

    public AddStudentAction(String department, Integer student) {
        this.department = department;
        this.student = student;
        setActionName("Add Student");
    }

    public String getDepartment() {
        return department;
    }

    public Integer getStudent() {
        return student;
    }

    @Override
    protected void start() {

    }
}
