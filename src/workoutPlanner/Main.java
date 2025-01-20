package workoutPlanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import workoutPlanner.dataObjects.Day;
import workoutPlanner.dataObjects.Exercise;
import workoutPlanner.dataObjects.enums.ExerciseNames;
import workoutPlanner.dataObjects.enums.MovementType;
import workoutPlanner.dataObjects.enums.Muscle;

public class Main {
	public static void main(String[] args) {
		
		ArrayList<Exercise> exercises = getExercises();
		Random rand = new Random();
		
		List<Exercise> compounds = exercises.stream().filter(x -> x.getMovementType().equals(MovementType.COMPOUND)).collect(Collectors.toList());
		ArrayList<Exercise> isolations = (ArrayList<Exercise>) exercises.stream().filter(x -> x.getMovementType().equals(MovementType.ISOLATION)).collect(Collectors.toList());
		
		ArrayList<Day> days = createDays(3);
		
		
		
		
		int retries = 0;
		
		boolean complete = false;
		while(!complete) {
			
			//-- add 1 random compound and 2 random isolations per day. 
			for(Day eachDay : days) {
				eachDay.addExercise(compounds.get(rand.nextInt(compounds.size())));
				eachDay.addExercise(isolations.get(rand.nextInt(isolations.size())));
				eachDay.addExercise(isolations.get(rand.nextInt(isolations.size())));
			}
			
			if(validateDays(days)) {
//				System.out.println("looks good!"); 
				complete = true;
			}
			else 
			{
//				System.out.println("retry: " + ++retries); 
				for(Day day : days) {
					day.getExercises().clear();
				}
			}
		}
		
		
		
		
		printWorkoutPlan(days);		
	}

	private static void printWorkoutPlan(ArrayList<Day> days) {
		for(int i = 0, j = i+1; i < days.size(); i++, j++) {
			System.out.println("Day " + j + ": " + days.get(i)); 
		}
	}

	private static ArrayList<Day> createDays(int workoutsPerWeek) {
		ArrayList<Day> days =  new ArrayList<>();
		for(int i = 0; i < workoutsPerWeek; i++) {
			days.add(new Day());
		}
		return days;
	}

	private static boolean validateDays(ArrayList<Day> days) {
		
		//exercises are unique
		List<Exercise> DailyExercises = days.stream().map(x -> x.getExercises()).flatMap(List::stream).toList();
		
		if(!allUniqueExercises(DailyExercises)) {
			return false;
		}
		
		
		HashSet<Muscle> allDaysCompoundMuscleSet = new HashSet<>();
		HashMap<Muscle, Integer> muscleFrequencyMap = new HashMap<>();

		for(Day day : days) {
			 ArrayList<Exercise> exercisesPerDay = day.getExercises();
			 boolean compoundIsNewMuscleGroup = allDaysCompoundMuscleSet.add(exercisesPerDay.get(0).getPrimaryMuscle());
			 if(!compoundIsNewMuscleGroup) {
				 return false;
			 }
			 //-- build primary muscle frequency map
			 for(Exercise exercise : exercisesPerDay) {
				 Integer returnValue = muscleFrequencyMap.putIfAbsent(exercise.getPrimaryMuscle(), 1);
				 if(returnValue!=null) {
					 if(1==returnValue) {
						 muscleFrequencyMap.put(exercise.getPrimaryMuscle(), ++returnValue);
					 }
					 else if(returnValue > 1) {
						 return false;
					 }
				 }
			 }
			 
			 //-- build secondary muscle frequency map
			 buildSecondaryMuscleFrequencyMap(muscleFrequencyMap, exercisesPerDay);
		 }
		
		printMuscleFrequency(muscleFrequencyMap);
		
		return true;
	}

	private static void buildSecondaryMuscleFrequencyMap(HashMap<Muscle, Integer> muscleFrequencyMap,
			ArrayList<Exercise> exercisesPerDay) {
		for(Exercise exercises : exercisesPerDay) {
			 Integer returnValue = muscleFrequencyMap.putIfAbsent(exercises.getSecondaryMuscle(), 1);
			 if(returnValue!=null) {
				 muscleFrequencyMap.put(exercises.getPrimaryMuscle(), ++returnValue);
			 }
		 }
	}

	private static void printMuscleFrequency(HashMap<Muscle, Integer> muscleFrequencyMap) {
		System.out.println("this workout hits: ");
		for (Map.Entry<Muscle,Integer> entry : muscleFrequencyMap.entrySet()) {
			if(!entry.getKey().equals(Muscle.NA)) {
				System.out.print("the " + entry.getKey().toString().toLowerCase() + " " + entry.getValue());
				if(entry.getValue() == 1) 
					System.out.println(" time ");
				else
					System.out.println(" times ");
			}
		}
	}

	private static boolean allUniqueExercises(List<Exercise> DailyExercises) {
		if(DailyExercises.stream().count() == DailyExercises.stream().distinct().count()) {
			return true;
		}
		else {
			return false;
		}
	}

	private static ArrayList<Exercise> getExercises() {
		ArrayList<Exercise> exercises = new ArrayList<>();
		final String fileName="src/resources/exercises.csv";
	    String line = "";
        String delimiter = ",";
       
        
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(delimiter);
                Exercise exercise = new Exercise(ExerciseNames.valueOf(data[0]), MovementType.valueOf(data[1]), Muscle.valueOf(data[2]), Muscle.valueOf(data[3]));
                exercises.add(exercise);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return exercises;
	}
}

