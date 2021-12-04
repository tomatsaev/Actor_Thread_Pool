package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.actions.messages.ComputerPromise;
import bgu.atd.a1.sim.actions.messages.MeetsObligationsMessage;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.Collection;
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
        if(pool.warehouse.acquire(computerType)) {
            sendMeetsObligationsMessage();
        }
        else{
            ComputerPromise computerPromise = new ComputerPromise(computerType);
            computerPromise.getResult().subscribe(this::sendMeetsObligationsMessage);
            sendMessage(computerPromise, actorID, pool.getPrivateState(actorID));
        }

    }

    /**
     * send MeetsObligationsMessage to all students register to the department
     * and release the Computer lock
     */
    private void sendMeetsObligationsMessage (){
        DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState) pool.getPrivateState(actorID);
        List<MeetsObligationsMessage> actions = new ArrayList<>();
        for (String student : students) {
            MeetsObligationsMessage meetsObligationsMessage = new MeetsObligationsMessage(conditions, computerType);
            actions.add(meetsObligationsMessage);
        }
        then(actions, () -> {
            for (MeetsObligationsMessage action : actions) {
                // TODO
            }
        });
        for (int i = 0; i < actions.size(); i++) {
            sendMessage(actions.get(i), departmentPrivateState.getStudentList().get(i), new StudentPrivateState());
        }
        pool.warehouse.release(computerType);
    }
}
