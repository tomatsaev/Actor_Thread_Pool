package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;

public class OpenCourseAction extends Action {

    String department;
    String course;
    Integer space;
    String[] prerequisites;

    public OpenCourseAction(String department, String course, Integer space, String[] prerequisites) {
        this.department = department;
        this.course = course;
        this.space = space;
        this.prerequisites = prerequisites;
        setActionName("Open Course");
    }

    public void setDepartment(String department) {
        department = department;
    }

    public void setCourse(String course) {
        course = course;
    }

    public void setspace(String space) {
        space = space;
    }

    public void setPrerequisites(String[] prerequisites) {
        prerequisites = prerequisites;
    }

    public String[] getPrerequisites() {
        return prerequisites;
    }

    public Integer getspace() {
        return space;
    }

    public String getcourse() {
        return course;
    }

    public String getDepartment() {
        return department;
    }

    public void start(){

    }
}
