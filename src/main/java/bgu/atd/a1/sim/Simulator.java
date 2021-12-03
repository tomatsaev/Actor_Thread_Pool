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

import bgu.atd.a1.Action;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.actions.*;
import com.google.gson.*;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	
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
	}

	private static void parse(String s){
		File input = new File(s);
		try {
		    FileReader fileReader = new FileReader(input);
			JsonElement fileElement  = JsonParser.parseReader(fileReader);
			JsonObject fileObject = fileElement.getAsJsonObject();

			// Extracting nthreads field
			Integer nthreads = fileObject.get("threads").getAsInt();

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
            List<Action> phase1Actions = new ArrayList<>();
            for(JsonElement phase1ActionElement : jsonArrayOfPhase1){
                JsonObject phase1ActionObject = phase1ActionElement.getAsJsonObject();

                // Find the Action class
                Action action = extractActionFromJson(phase1ActionObject, fileReader);
                phase1Actions.add(action);
            }


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static Action extractActionFromJson(JsonObject actionObject, FileReader input){
        String actionName = actionObject.get("Action").getAsString();
        Action action = null;
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
                break;

            case "Add Student":
                department = actionObject.get("Department").getAsString();
                Integer student = actionObject.get("Student").getAsInt();
                action = new AddStudentAction(department, student);
                break;

            case "Participate In Course":
                student = actionObject.get("Student").getAsInt();
                course = actionObject.get("Course").getAsString();
                JsonArray gradesJsonArray = actionObject.get("Prerequisites").getAsJsonArray();
                Integer[] grades = new Integer[gradesJsonArray.size()];
                for(int i = 0; i< gradesJsonArray.size(); i++){
                    grades[i] = gradesJsonArray.get(i).getAsInt();
                }
                action = new ParticipateInCourseAction(student, course, grades);
                break;

            case "Unregister":
                student = actionObject.get("Student").getAsInt();
                course = actionObject.get("Course").getAsString();
                action = new UnregisterAction(student, course);
                break;

            case "Close Course":
                department = actionObject.get("Department").getAsString();
                course = actionObject.get("Course").getAsString();
                action = new CloseCourseAction(department, course);
                break;

            case "Add Spaces":
                course = actionObject.get("Course").getAsString();
                Integer number = actionObject.get("Number").getAsInt();
                action = new AddSpacesAction(course, number);
                break;

            case "Administrative Check":
                department = actionObject.get("Department").getAsString();
                JsonArray studentsJsonArray = actionObject.get("Students").getAsJsonArray();
                Integer[] students = new Integer[studentsJsonArray.size()];
                for(int i = 0; i< studentsJsonArray.size(); i++){
                    students[i] = studentsJsonArray.get(i).getAsInt();
                }
                String computerType = actionObject.get("Computer").getAsString();
                JsonArray conditionsJsonArray = actionObject.get("Conditions").getAsJsonArray();
                String[] conditions = new String[conditionsJsonArray.size()];
                for(int i = 0; i< conditionsJsonArray.size(); i++){
                    conditions[i] = conditionsJsonArray.get(i).getAsString();
                }
                action = new AdministrativeCheckAction(department, students, computerType, conditions);
                break;

            case "Register With Preferences":
                student = actionObject.get("Student").getAsInt();
                JsonArray coursesJsonArray = actionObject.get("Conditions").getAsJsonArray();
                String[] courses = new String[coursesJsonArray.size()];
                for(int i = 0; i< coursesJsonArray.size(); i++){
                    courses[i] = coursesJsonArray.get(i).getAsString();
                }
                action = new RegisterWithPreferanceAction(student, courses);
                break;

        }
        return action;
    }
}
