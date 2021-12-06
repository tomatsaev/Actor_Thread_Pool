package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

public class AddSpacesAction extends Action<String> {
    String course;
    Integer number;

    public AddSpacesAction(String course, Integer number) {
        this.course = course;
        this.number = number;
        setActionName("Add Spaces");
    }

    public String getCourse() {
        return course;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    protected void start() {
        CoursePrivateState coursePrivateState = ((CoursePrivateState) pool.getPrivateState(actorID));
        coursePrivateState.setAvailableSpots(coursePrivateState.getAvailableSpots() + number);
        coursePrivateState.addRecord(getActionName());
        complete("Successfully added " + number + " more spaces to course " + course + ".");
        System.out.println("Successfully added " + number + " more spaces to course " + course + ".");
    }
}
