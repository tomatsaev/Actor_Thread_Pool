package bgu.atd.a1.sim.privateStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


import bgu.atd.a1.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState{

	private HashMap<String, Integer> grades;
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState() {
		grades = new HashMap<>();
	}

	public HashMap<String, Integer> getGrades() {
		return grades;
	}
	public Set<String> getCourses(){
		return grades.keySet();
	}

	public List<String> getGradesSer() {
		List<String> l = new ArrayList<>();
		for (String key : grades.keySet()) {
			l.add(String.format("(%s, %s)", key, grades.get(key)));
		}
		return l;
	}
	public long getSignature() {
		return signature;
	}

	public void setGrades(HashMap<String, Integer> grades) {
		this.grades = grades;
	}

	public void setSignature(long signature) {
		this.signature = signature;
	}
}
