package bgu.atd.a1.sim.massages;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class ParticipateMassage extends Action<Boolean> {
    String student;

    public ParticipateMassage(String student) {
        this.student = student;
    }

    @Override
    protected void start() {

    }
}
