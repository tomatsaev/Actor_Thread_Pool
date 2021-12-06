package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.actions.messages.GetComputerMessage;
import bgu.atd.a1.sim.actions.messages.MeetsObligationsMessage;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import bgu.atd.a1.sim.privateStates.WarehousePrivateState;

import java.util.ArrayList;
import java.util.List;

public class AdministrativeCheckAction extends Action<String> {
    String department;
    String[] students;
    String computerType;
    List<String> conditions;

    public AdministrativeCheckAction(String department, String[] students, String computerType, List<String> conditions) {
        this.department = department;
        this.students = students;
        this.computerType = computerType;
        this.conditions = conditions;
        setActionName("Administrative Check");
    }

    public String getDepartment() {
        return department;
    }

    public String[] getStudents() {
        return students;
    }

    public String getComputerType() {
        return computerType;
    }

    public List<String> getConditions() {
        return conditions;
    }

    @Override
    protected void start() {
        DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState) pool.getPrivateState(actorID);
        List<GetComputerMessage> actions = new ArrayList<>();
        GetComputerMessage getComputerMessage = new GetComputerMessage(computerType);
        actions.add(getComputerMessage);
        then(actions, () -> {
            Computer computer = actions.get(0).getResult().get();
            List<MeetsObligationsMessage> studentsActions = new ArrayList<>();
            for (String student : students) {
                MeetsObligationsMessage meetsObligationsMessage = new MeetsObligationsMessage(conditions, computer);
                studentsActions.add(meetsObligationsMessage);
            }
            then(studentsActions, () -> {
                complete("Administrative checks finished");
                System.out.println("Administrative checks finished");
                pool.warehouse.release(computerType);
                departmentPrivateState.addRecord(getActionName());
            });

            for (int i = 0; i < actions.size(); i++) {
                sendMessage(actions.get(i), departmentPrivateState.getStudentList().get(i), new StudentPrivateState());
            }
        });
        sendMessage(getComputerMessage, "WareHouse", new WarehousePrivateState());
    }
}
