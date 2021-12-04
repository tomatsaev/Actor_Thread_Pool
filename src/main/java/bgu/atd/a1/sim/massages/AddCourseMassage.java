package bgu.atd.a1.sim.massages;

import bgu.atd.a1.Action;

public class AddCourseMassage extends Action<Boolean> {

    Integer space;
    String[] prerequisites;

    public AddCourseMassage(Integer space, String[] prerequisites) {
        this.space = space;
        this.prerequisites = prerequisites;
    }

    @Override
    protected void start() {

    }
}
