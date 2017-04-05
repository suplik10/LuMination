package cz.lumination.fitapp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.lumination.fitapp.entity.Workout;

/**
 * Created by mkasl on 03.04.2017.
 */

public class ExerciseDto implements Serializable{

    private int exerciseId;

    private String name;

    //TODO add to enum
    private String type;

    private int sets;

    private int reps;

    private int restBetweenSetsInSeconds;

    private int liftingWeight;

    private List<WorkoutDto> workouts;

    private boolean checked;

    public ExerciseDto() {
    }

    public ExerciseDto(int exerciseId, String name, String type, int sets, int reps, int restBetweenSetsInSeconds, int liftingWeight, List<WorkoutDto> workouts, boolean checked) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.type = type;
        this.sets = sets;
        this.reps = reps;
        this.restBetweenSetsInSeconds = restBetweenSetsInSeconds;
        this.liftingWeight = liftingWeight;
        this.workouts = workouts;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRestBetweenSetsInSeconds() {
        return restBetweenSetsInSeconds;
    }

    public void setRestBetweenSetsInSeconds(int restBetweenSetsInSeconds) {
        this.restBetweenSetsInSeconds = restBetweenSetsInSeconds;
    }

    public int getLiftingWeight() {
        return liftingWeight;
    }

    public void setLiftingWeight(int liftingWeight) {
        this.liftingWeight = liftingWeight;
    }

    public List<WorkoutDto> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<WorkoutDto> workouts) {
        this.workouts = workouts;
    }
}
