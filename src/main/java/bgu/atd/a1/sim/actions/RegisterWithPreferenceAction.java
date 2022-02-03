package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterWithPreferenceAction extends Action<Boolean> {
    String student;
    List<String> courses;
    String[] grade;

    public RegisterWithPreferenceAction(String student, List<String> courses, String[] grade) {
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
        StudentPrivateState privateState = (StudentPrivateState) pool.getPrivateState(actorID);
        if(courses.isEmpty()) {
            complete(false);
            return;
        }
        String course = courses.remove(0);
        String[] grade = {this.grade[0]};
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> registerWithPreferenceMessage = new RegisterWithPreferenceMessage(student, course, grade);
        actions.add(registerWithPreferenceMessage);
        then(actions, () -> {
            if(actions.get(0).getResult().get()) {
                complete(true);
                privateState.addRecord(getActionName());
            }
            else {
                RegisterWithPreferenceAction nextCourse = new RegisterWithPreferenceAction(student, courses, Arrays.copyOfRange(this.grade, 1, this.grade.length));
                sendMessage(nextCourse, actorID, privateState);
                complete(false);
            }
        });
        sendMessage(registerWithPreferenceMessage, course, new CoursePrivateState());
    }
}
