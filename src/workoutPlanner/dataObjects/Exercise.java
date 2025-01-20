package workoutPlanner.dataObjects;

import workoutPlanner.dataObjects.enums.ExerciseNames;
import workoutPlanner.dataObjects.enums.MovementType;
import workoutPlanner.dataObjects.enums.Muscle;

public class Exercise {
	
	ExerciseNames exercise;
	MovementType movementType;
	Muscle primaryMuscle;
	Muscle secondaryMuscle;
	
	public Exercise(ExerciseNames exercise, MovementType movementType, Muscle primaryMuscle, Muscle secondaryMuscle){
		this.exercise = exercise;
		this.movementType = movementType;
		this.primaryMuscle = primaryMuscle;
		this.secondaryMuscle = secondaryMuscle;
	}
	  
    public ExerciseNames getExercise() {
	    return exercise;
    }
  
    public Muscle getPrimaryMuscle() {
	    return primaryMuscle;
    }
  
    public Muscle getSecondaryMuscle() {
	    return secondaryMuscle;
    }
    
    public MovementType getMovementType() {
	    return movementType;
    }
    
    @Override
    public String toString() {
        return this.exercise.toString();
    }
}