package workoutPlanner.dataObjects;

import java.util.ArrayList;

public class Day {
	ArrayList<Exercise> exercises;
	
	public Day(){
		this.exercises = new ArrayList<>();
	}
	
	Day(ArrayList<Exercise> exercises){
		this.exercises = new ArrayList<>();
	}
	
	public void setExercises (ArrayList<Exercise> exercises) {
		this.exercises = exercises;
	}
	
	public void addExercise (Exercise e) {
		this.exercises.add(e);
	}
	
	// Getters
	public ArrayList<Exercise> getExercises() {
	    return exercises;
	}
	
	@Override
    public String toString() {
        return this.exercises.toString().toLowerCase();
    }
}