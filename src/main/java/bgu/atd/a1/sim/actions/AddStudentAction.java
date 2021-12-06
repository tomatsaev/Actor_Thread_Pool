package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

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
        DepartmentPrivateState privateState = (DepartmentPrivateState) pool.getPrivateState(actorID);
        privateState.addRecord(getActionName());
        privateState.getStudentList().add(student);
        complete("Student " + student + " was added to " + department + " department successfully");
        System.out.println("Student " + student + " was added to " + department + " department successfully");
    }
}

