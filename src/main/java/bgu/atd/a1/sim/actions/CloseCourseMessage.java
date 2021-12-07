package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.List;

public class CloseCourseMessage extends Action<Boolean> {
    String course;

    public CloseCourseMessage(String course) {
        this.course = course;
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = (CoursePrivateState) pool.getPrivateState(actorID);
        List<Action<Boolean>> actions = new ArrayList<>();
        for (String ignored : coursePrivateState.getRegStudents()) {
            Action<Boolean> removeGrade = new RemoveCourseGradeMessage(course);
            actions.add(removeGrade);
        }
        if(actions.isEmpty()){
            closeCourseAndComplete(coursePrivateState);
            return;
        }
        then(actions, () -> {
            closeCourseAndComplete(coursePrivateState);
        });
        for (int i = 0; i < actions.size(); i++) {
            sendMessage(actions.get(i), coursePrivateState.getRegStudents().get(i), new StudentPrivateState());
        }

    }

    private void closeCourseAndComplete(CoursePrivateState coursePrivateState){
        coursePrivateState.setAvailableSpots(-1);
        coursePrivateState.setRegStudents(new ArrayList<>());
        coursePrivateState.setRegistered(0);
        complete(true);
        coursePrivateState.addRecord(getActionName());
    }

}
