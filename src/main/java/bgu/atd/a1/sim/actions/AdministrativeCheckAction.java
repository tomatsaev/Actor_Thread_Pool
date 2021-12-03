package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Computer;

public class AdministrativeCheckAction extends Action<String> {
    String department;
    String[] students;
    String computerType;
    String[] conditions;

    public AdministrativeCheckAction(String department, String[] students, String computerType, String[] conditions) {
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

    public String[] getConditions() {
        return conditions;
    }

    @Override
    protected void start() {

    }
}
