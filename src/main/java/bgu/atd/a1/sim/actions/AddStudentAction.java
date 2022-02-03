package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

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
        privateState.getStudentList().add(student);
        List<AddStudentMessage> actions = new ArrayList<>();
        AddStudentMessage action = new AddStudentMessage();
        actions.add(action);
        then(actions, () -> {
            privateState.addRecord(getActionName());
            complete("Student " + student + " was added to " + department + " department successfully");
            System.out.println("Student " + student + " was added to " + department + " department successfully");
        });
        sendMessage(action, student, new StudentPrivateState()); // only in order to open an Actor for him
    }
}

