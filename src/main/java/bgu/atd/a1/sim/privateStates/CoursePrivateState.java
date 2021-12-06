package bgu.atd.a1.sim.privateStates;

import java.util.ArrayList;
import java.util.List;


import bgu.atd.a1.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots = 0;
	private Integer registered = 0;
	private List<String> regStudents = new ArrayList();
	private List<String> prerequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		//TODO: replace method body with real implementation
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrerequisites() {
		return prerequisites;
	}

	public void setAvailableSpots(Integer availableSpots) {
		this.availableSpots = availableSpots;
	}

	public void setRegistered(Integer registered) {
		this.registered = registered;
	}

	public void setRegStudents(List<String> regStudents) {
		this.regStudents = regStudents;
	}

	public void setPrerequisites(List<String> prerequisites) {
		this.prerequisites = prerequisites;
	}

	public void addStudent(String student){
		registered++;
		availableSpots--;
		regStudents.add(student);
	}

	public void removeStudent(String student){
		registered--;
		availableSpots++;
		regStudents.remove(student);
	}
}
