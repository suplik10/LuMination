package cz.lumination.fitapp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.lumination.fitapp.entity.Exercise;

/**
 * Created by mkasl on 03.04.2017.
 */

public class WorkoutDto implements Serializable {

    private int workoutId;
    private String name;
    private List<ExerciseDto> exercises;

    public WorkoutDto() {
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExerciseDto> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDto> exercises) {
        this.exercises = exercises;
    }
}
