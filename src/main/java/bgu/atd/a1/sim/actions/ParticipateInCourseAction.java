package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

public class ParticipateInCourseAction extends Action<String> {

    Integer student;
    String course;
    Integer[] grades;

    public ParticipateInCourseAction(Integer student, String course, Integer[] grades) {
        this.student = student;
        this.course = course;
        this.grades = grades;
        setActionName("Participate In Course");
    }

    public Integer getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }

    public Integer[] getGrades() {
        return grades;
    }

    @Override
    protected void start() {

    }
}
