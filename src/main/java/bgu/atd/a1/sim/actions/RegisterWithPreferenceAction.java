package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.messages.RegisterWithPreferenceMessage;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterWithPreferenceAction extends Action<Boolean> {
    String student;
    List<String> courses;
    String[] grade;

    public RegisterWithPreferenceAction(String student, List courses, String[] grade) {
        this.student = student;
        this.courses = courses;
        this.grade = grade;
        setActionName("Register With Preferences");
    }

    public String getStudent() {
        return student;
    }

    public List<String> getCourses() {
        return courses;
    }

    @Override
    protected void start() {
        if(courses.isEmpty()) {
            complete(false);
            return;
        }
        String course = courses.remove(0);
        StudentPrivateState privateState = (StudentPrivateState) pool.getPrivateState(actorID);
        List<String> studentPrereqs = new ArrayList<>(privateState.getGrades().keySet());
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> registerWithPreferenceMessage = new RegisterWithPreferenceMessage(student, course, studentPrereqs);
        actions.add(registerWithPreferenceMessage);
        then(actions, () -> {
            if(actions.get(0).getResult().get()) {
                complete(true);
                privateState.addRecord(getActionName());
            }
            else {
                RegisterWithPreferenceAction nextCourse = new RegisterWithPreferenceAction(student, courses, grade);
                sendMessage(nextCourse, course, new StudentPrivateState());
            }
        });
    }
}
