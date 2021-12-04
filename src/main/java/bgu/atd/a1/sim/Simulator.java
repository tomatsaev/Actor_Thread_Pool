/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.atd.a1.sim;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.actions.*;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import com.google.gson.*;
import javafx.util.Pair;
// TODO: change pair to AbstractMap.simpleEntry
/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	private static Integer nthreads;
	private static Map<Action, Pair<String, PrivateState>> phase1Actions;
    private static Map<Action, Pair<String, PrivateState>> phase2Actions;
    private static Map<Action, Pair<String, PrivateState>> phase3Actions;


    /**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
    }
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool = myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	
	
	public static void main(String [] args){
		parse(args[0]);
        actorThreadPool = new ActorThreadPool(nthreads);
        actorThreadPool.start();
        System.out.println(phase1Actions.keySet());

        // TODO: check inserting by order (phases)
        for(Action action : phase1Actions.keySet()) {
            actorThreadPool.submit(action, phase1Actions.get(action).getKey(), phase1Actions.get(action).getValue());
        }

        for(Action action : phase2Actions.keySet()) {
            actorThreadPool.submit(action, phase2Actions.get(action).getKey(), phase2Actions.get(action).getValue());
        }

        for(Action action : phase2Actions.keySet()) {
            actorThreadPool.submit(action, phase2Actions.get(action).getKey(), phase2Actions.get(action).getValue());
        }

	}

	private static void parse(String s){
		File input = new File(s);
		try {
		    FileReader fileReader = new FileReader(input);
			JsonElement fileElement  = JsonParser.parseReader(fileReader);
			JsonObject fileObject = fileElement.getAsJsonObject();

			// Extracting nthreads field
            nthreads = fileObject.get("threads").getAsInt();

            // Extracting Computers
            JsonArray jsonArrayOfComputers = fileObject.get("Computers").getAsJsonArray();
            List<Computer> computers = new ArrayList<>();
            for(JsonElement computerElement : jsonArrayOfComputers){
                JsonObject computerJsonObject = computerElement.getAsJsonObject();

                // Extracting data
                String computerType = computerJsonObject.get("Type").getAsString();
                long successSig = computerJsonObject.get("Sig Success").getAsLong();
                long failSig = computerJsonObject.get("Sig Fail").getAsLong();

                Computer computer = new Computer(computerType, successSig, failSig);
                computers.add(computer);
            }

            // Extracting Phase 1
            JsonArray jsonArrayOfPhase1 = fileObject.get("Phase 1").getAsJsonArray();
            phase1Actions = new HashMap<>();
            for(JsonElement phase1ActionElement : jsonArrayOfPhase1){
                JsonObject phase1ActionObject = phase1ActionElement.getAsJsonObject();

                // Find the Action Type
                extractActionFromJson(phase1ActionObject, phase1Actions);
            }

            // Extracting Phase 2
            JsonArray jsonArrayOfPhase2 = fileObject.get("Phase 2").getAsJsonArray();
            phase2Actions = new HashMap<>();
            for(JsonElement phase2ActionElement : jsonArrayOfPhase2){
                JsonObject phase2ActionObject = phase2ActionElement.getAsJsonObject();

                // Find the Action Type
                extractActionFromJson(phase2ActionObject, phase2Actions);
            }

            // Extracting Phase 3
            JsonArray jsonArrayOfPhase3 = fileObject.get("Phase 3").getAsJsonArray();
            phase3Actions = new HashMap<>();
            for(JsonElement phase3ActionElement : jsonArrayOfPhase3){
                JsonObject phase3ActionObject = phase3ActionElement.getAsJsonObject();

                // Find the Action Type
                extractActionFromJson(phase3ActionObject, phase3Actions);
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void extractActionFromJson(JsonObject actionObject, Map phaseMap){
        String actionName = actionObject.get("Action").getAsString();
        Action action = null;
        String actorID = null;
        PrivateState privateState = null;
        switch (actionName){
            case "Open Course":
                String department = actionObject.get("Department").getAsString();
                String course = actionObject.get("Course").getAsString();
                Integer space = actionObject.get("Space").getAsInt();
                JsonArray prerequisitesJsonArray = actionObject.get("Prerequisites").getAsJsonArray();
                String[] prerequisites = new String[prerequisitesJsonArray.size()];
                for(int i = 0; i< prerequisitesJsonArray.size(); i++){
                    prerequisites[i] = prerequisitesJsonArray.get(i).getAsString();
                }
                action = new OpenCourseAction(department, course, space, prerequisites);
                actorID = department;
                privateState = new DepartmentPrivateState();
                break;

            case "Add Student":
                department = actionObject.get("Department").getAsString();
                String student = actionObject.get("Student").getAsString();
                action = new AddStudentAction(department, student);
                actorID = department;
                privateState = new DepartmentPrivateState();
                break;

            case "Participate In Course":
                student = actionObject.get("Student").getAsString();
                course = actionObject.get("Course").getAsString();
                JsonArray gradesJsonArray = actionObject.get("Grade").getAsJsonArray();
                Integer[] grades = new Integer[gradesJsonArray.size()];
                for(int i = 0; i< gradesJsonArray.size(); i++){
                    grades[i] = gradesJsonArray.get(i).getAsInt();
                }
                action = new ParticipateInCourseAction(student, course, grades);
                actorID = course;
                privateState = new CoursePrivateState();
                break;

            case "Unregister":
                student = actionObject.get("Student").getAsString();
                course = actionObject.get("Course").getAsString();
                action = new UnregisterAction(student, course);
                actorID = course;
                privateState = new CoursePrivateState();
                break;

            case "Close Course":
                department = actionObject.get("Department").getAsString();
                course = actionObject.get("Course").getAsString();
                action = new CloseCourseAction(department, course);
                actorID = department;
                privateState = new DepartmentPrivateState();
                break;

            case "Add Spaces":
                course = actionObject.get("Course").getAsString();
                Integer number = actionObject.get("Number").getAsInt();
                action = new AddSpacesAction(course, number);
                actorID = course;
                privateState = new CoursePrivateState();
                break;

            case "Administrative Check":
                department = actionObject.get("Department").getAsString();
                JsonArray studentsJsonArray = actionObject.get("Students").getAsJsonArray();
                String[] students = new String[studentsJsonArray.size()];
                for(int i = 0; i< studentsJsonArray.size(); i++){
                    students[i] = studentsJsonArray.get(i).getAsString();
                }
                String computerType = actionObject.get("Computer").getAsString();
                JsonArray conditionsJsonArray = actionObject.get("Conditions").getAsJsonArray();
                String[] conditions = new String[conditionsJsonArray.size()];
                for(int i = 0; i< conditionsJsonArray.size(); i++){
                    conditions[i] = conditionsJsonArray.get(i).getAsString();
                }
                action = new AdministrativeCheckAction(department, students, computerType, conditions);
                actorID = department;
                privateState = new DepartmentPrivateState();
                break;

            case "Register With Preferences":
                student = actionObject.get("Student").getAsString();
                JsonArray coursesJsonArray = actionObject.get("Conditions").getAsJsonArray();
                String[] courses = new String[coursesJsonArray.size()];
                for(int i = 0; i< coursesJsonArray.size(); i++){
                    courses[i] = coursesJsonArray.get(i).getAsString();
                }
                action = new RegisterWithPreferanceAction(student, courses);
                actorID = student;
                privateState = new StudentPrivateState();
                break;

        }
        phaseMap.put(action, new Pair<>(actorID, privateState));
    }
}
