package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

public class AddSpacesAction extends Action<String> {
    String course;
    Integer number;

    public AddSpacesAction(String course, Integer number) {
        this.course = course;
        this.number = number;
        setActionName("Add Spaces");
    }

    @Override
    protected void start() {

    }
}
